package ro.ubbcluj.cs.ml.app;

import ro.ubbcluj.cs.ml.ann.ChooseBy;
import ro.ubbcluj.cs.ml.ann.NeuralNetwork;
import ro.ubbcluj.cs.ml.ann.Problem;
import ro.ubbcluj.cs.ml.domain.TrainingExample;
import ro.ubbcluj.cs.ml.io.TrainingExampleExcelReader;
import ro.ubbcluj.cs.ml.util.Statistics;
import ro.ubbcluj.cs.ml.util.Validator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mihai Teletin
 */
public class MainLoo {
    public static void main(String[] args) throws IOException {
        TrainingExampleExcelReader trainingExampleExcelReader = new TrainingExampleExcelReader(new FileInputStream(ClassLoader.getSystemResource("data.xls").getPath()), Problem.Regression);
        int input = trainingExampleExcelReader.getInputSize();
        int output = trainingExampleExcelReader.getOutputSize();
        List<TrainingExample> list = trainingExampleExcelReader.getFullTrainingSet();
        int n = list.size();
        int hidden = (int) Math.sqrt(input * output);
        Statistics statistics = new Statistics();
        System.out.println(n + " instances");
        for (int out = 0; out < n; ++out) {
            NeuralNetwork neuralNetwork = new NeuralNetwork(input, output, 1, hidden);
            for (int iteration = 0; iteration < 5000; ++iteration) {
                for (int i = 0; i < n; ++i) {
                    if (i != out) {
                        neuralNetwork.backPropagation(list.get(i));
                    }
                }
            }
            double value = Validator.validate(neuralNetwork, Arrays.asList(list.get(out)), ChooseBy.MAE);
            System.out.println((out + 1) + ": " + value);
            statistics.addValue(value);
        }
        System.out.println("mean: " + statistics.getMean());
        System.out.println("max: " + statistics.getMax());
        System.out.println("min: " + statistics.getMin());
        System.out.println("stdev: " + statistics.getStandardDeviation());
    }
}

