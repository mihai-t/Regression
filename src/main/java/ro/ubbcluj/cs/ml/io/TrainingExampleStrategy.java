package ro.ubbcluj.cs.ml.io;


import ro.ubbcluj.cs.ml.ann.Problem;
import ro.ubbcluj.cs.ml.domain.TrainingExample;

import java.io.IOException;
import java.util.*;

public abstract class TrainingExampleStrategy {

    private final static int kFoldValidationSize = 10;
    /**
     * part of data set which will be used for training;
     * the rest will be used for validation
     */
    protected final double ratioOfTraining = 0.9;
    protected List<TrainingExample> validationSet, fullTrainingSet;
    protected List<Double> maxes, minis;
    protected Set<Integer> targetClasses = new HashSet<>();
    protected Map<Integer, List<TrainingExample>> foldMap;
    private Problem problem;

    {
        maxes = new ArrayList<>();
        minis = new ArrayList<>();
    }

    public TrainingExampleStrategy(Problem problem) {
        validationSet = new ArrayList<>();
        fullTrainingSet = new ArrayList<>();
        foldMap = new HashMap<>();
        this.problem = problem;
    }

    public static int getKFoldValidationSize() {
        return kFoldValidationSize;
    }

    public void shuffle() throws IOException {
        this.readData();
        Collections.shuffle(fullTrainingSet);
    }

    protected abstract List<Double> getTargetValue(Double value);

    public abstract void readData() throws IOException;

    public List<TrainingExample> getValidationSet() {
        return validationSet;
    }

    public List<TrainingExample> getFullTrainingSet() {
        return fullTrainingSet;
    }

    public List<TrainingExample> getFold(int k) {
        return foldMap.get(k);
    }

    public Problem getProblem() {
        return problem;
    }

    public int getOutputSize() {

        switch (problem) {
            case Regression:
                return 1;
            case BinaryClassification:
                return 2;
            case Classification:
                return targetClasses.size();
            default:
                throw new IllegalArgumentException("invalid problem");
        }
    }

    public List<Double> getMinis() {
        return new ArrayList<>(minis);
    }

    public List<Double> getMaxes() {
        return new ArrayList<>(maxes);
    }

    public abstract int getInputSize();

}
