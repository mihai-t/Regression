package ro.ubbcluj.cs.ml.io;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import ro.ubbcluj.cs.ml.ann.Problem;
import ro.ubbcluj.cs.ml.domain.TrainingExample;
import ro.ubbcluj.cs.ml.util.MissingValuesUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrainingExampleExcelReader extends TrainingExampleStrategy {
    private byte[] data;
    private int total = 0;
    private int inputs = 0;


    public TrainingExampleExcelReader(InputStream inputStream, Problem problem) throws IOException {
        super(problem);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, byteArrayOutputStream);
        data = byteArrayOutputStream.toByteArray();
        inputStream.close();
        byteArrayOutputStream.close();
        this.readData();
    }


    @Override
    protected List<Double> getTargetValue(Double value) {
        if (getProblem() == Problem.BinaryClassification) {
            if (value == 0) {
                return Arrays.asList(0.1, 0.9);
            } else {
                return Arrays.asList(0.9, 0.1);
            }
        } else if (getProblem() == Problem.Classification) {
            int n = targetClasses.size();
            List<Double> out = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                out.add(0.1);
            }
            int i = 0;
            for (int d : targetClasses) {
                if (d < Math.floor(value)) {
                    i++;
                }
            }
            out.set(i, 0.9);
            return out;
        } else {
            return Arrays.asList(value);//regression
        }
    }

    @Override
    public void readData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(data);
        HSSFWorkbook workbook;
        try {
            workbook = new HSSFWorkbook(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        HSSFSheet sheet = workbook.getSheetAt(0);
        total = 0;
        inputs = 0;
        for (Row row : sheet) {
            if (row == null) {
                continue;
            }

            int i = 0;
            for (Cell cell : row) {
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    if (i == 0) {
                        targetClasses.add((int) cell.getNumericCellValue());
                    }
                    addMinMax(i++, cell.getNumericCellValue());
                }
            }
            if (i > 0) {
                total++;
                inputs = i - 1;
            }
        }
        int id = 0;
        for (Row row : sheet) {
            if (row == null) {
                continue;
            }
            int i = 0;
            List<Double> features = new ArrayList<>();
            List<Double> target = new ArrayList<>();
            for (Cell cell : row) {
                double feature = cell.getNumericCellValue();

                if (i > inputs) {
                    continue;
                }
                if (feature != MissingValuesUtil.missingFlag) {
                    feature = (feature - minis.get(i)) / (maxes.get(i) - minis.get(i));
                }
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    if (i == 0) {
                        target = getTargetValue(feature);
                    } else {
                        features.add(feature);
                    }
                    i++;
                }
            }
            if (features.size() == inputs) {
                TrainingExample trainingExample = new TrainingExample(++id, features, target);
                fullTrainingSet.add(trainingExample);
            }
        }
        TrainingExample.setMax(maxes.get(0));
        TrainingExample.setMin(minis.get(0));
        inputStream.close();
    }

    @Override
    public void shuffle() throws IOException {
        super.shuffle();
        foldMap.clear();
        validationSet.clear();
        List<TrainingExample> trainingExamples = new ArrayList<>();

        for (int c = 0; c < total; c++) {
            if (c < ratioOfTraining * total) {
                trainingExamples.add(fullTrainingSet.get(c));
            } else {
                validationSet.add(fullTrainingSet.get(c));
            }
        }

        //System.out.println("Validation set size: " + validationSet.size());

        int kFoldValidationSize = super.getKFoldValidationSize();
        int n = trainingExamples.size() / kFoldValidationSize;
        for (int i = 1; i <= kFoldValidationSize; ++i) {
            List<TrainingExample> list = new ArrayList<>();
            for (int j = (i - 1) * n; j < i * n; ++j) {
                list.add(trainingExamples.get(j));
            }
            if (i == kFoldValidationSize) {
                for (int j = i * n; j < trainingExamples.size(); ++j) {
                    list.add(trainingExamples.get(j));
                }
            }
            //MissingValuesUtil.placeAverage(list);//complete missing values
            foldMap.put(i, list);
        }
    }

    private void addMinMax(int index, double value) {
        if (value == MissingValuesUtil.missingFlag) {
            return;
        }
        if (maxes.size() <= index) {
            maxes.add(value);
            minis.add(value);
        } else {
            if (value > maxes.get(index)) {
                maxes.set(index, value);
            }
            if (value < minis.get(index)) {
                minis.set(index, value);
            }
        }
    }


    @Override
    public int getInputSize() {
        return inputs;
    }
}
