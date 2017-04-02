package ro.ubbcluj.cs.ml.app;


import ro.ubbcluj.cs.ml.ann.ChooseBy;
import ro.ubbcluj.cs.ml.ann.NeuralNetwork;
import ro.ubbcluj.cs.ml.ann.Problem;
import ro.ubbcluj.cs.ml.io.TrainingExampleExcelReader;
import ro.ubbcluj.cs.ml.util.Validator;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Mihai Teletin
 */
public class Main {
    public static void main(String[] args) throws IOException {
        TrainingExampleExcelReader trainingExampleExcelReader = new TrainingExampleExcelReader(new FileInputStream(ClassLoader.getSystemResource("data.xls").getPath()), Problem.Regression);
        int input = trainingExampleExcelReader.getInputSize();
        int output = trainingExampleExcelReader.getOutputSize();
        int hidden = (int) Math.sqrt(input * output);
        NeuralNetwork neuralNetwork = new NeuralNetwork(input, output, 1, hidden);
        for (int i = 0; i < 5000; ++i) {
            trainingExampleExcelReader.getFullTrainingSet().forEach(neuralNetwork::backPropagation);
        }
        System.out.println(Validator.validate(neuralNetwork, trainingExampleExcelReader.getFullTrainingSet(), ChooseBy.MAE));
    }
}
