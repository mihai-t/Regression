package ro.ubbcluj.cs.ml.ann;

import org.junit.Assert;
import org.junit.Test;
import ro.ubbcluj.cs.ml.domain.TrainingExample;
import ro.ubbcluj.cs.ml.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NeuralNetworkTest {

    @Test
    public void testBackPropagationOnXor() {
        NeuralNetwork neuralNetwork = new NeuralNetwork(2, 1, 1, 2);
        List<TrainingExample> list = new ArrayList<>();
        TrainingExample t1 = new TrainingExample(Arrays.asList(0.0, 0.0), Arrays.asList(0.1));
        TrainingExample t2 = new TrainingExample(Arrays.asList(1.0, 0.0), Arrays.asList(0.9));
        TrainingExample t3 = new TrainingExample(Arrays.asList(0.0, 1.0), Arrays.asList(0.9));
        TrainingExample t4 = new TrainingExample(Arrays.asList(1.0, 1.0), Arrays.asList(0.1));
        list.addAll(Arrays.asList(t1, t2, t3, t4));
        int iteration = 0;
        while (true) {
            for (TrainingExample t : list) {
                neuralNetwork.backPropagation(t);
            }
            if (Validator.validateMax(neuralNetwork, list) == 100) {
                System.out.println(iteration);
                return;
            }
            if ((++iteration) > 10000000) {
                break;
            }
        }
        Assert.fail("Couldn't reach 100%");

    }

    @Test
    public void testBackPropagationOnIdentity() {
        NeuralNetwork neuralNetwork = new NeuralNetwork(8, 8, 1, 3);
        List<TrainingExample> list = new ArrayList<>();
        TrainingExample t1 = new TrainingExample(Arrays.asList(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), Arrays.asList(0.9, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1));
        TrainingExample t2 = new TrainingExample(Arrays.asList(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), Arrays.asList(0.1, 0.9, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1));
        TrainingExample t3 = new TrainingExample(Arrays.asList(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0), Arrays.asList(0.1, 0.1, 0.9, 0.1, 0.1, 0.1, 0.1, 0.1));
        TrainingExample t4 = new TrainingExample(Arrays.asList(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0), Arrays.asList(0.1, 0.1, 0.1, 0.9, 0.1, 0.1, 0.1, 0.1));
        TrainingExample t5 = new TrainingExample(Arrays.asList(0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0), Arrays.asList(0.1, 0.1, 0.1, 0.1, 0.9, 0.1, 0.1, 0.1));
        TrainingExample t6 = new TrainingExample(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0), Arrays.asList(0.1, 0.1, 0.1, 0.1, 0.1, 0.9, 0.1, 0.1));
        TrainingExample t7 = new TrainingExample(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0), Arrays.asList(0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.9, 0.1));
        TrainingExample t8 = new TrainingExample(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0), Arrays.asList(0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.9));
        list.addAll(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8));
        for (int i = 0; i < 10000; ++i) {
            for (TrainingExample t : list) {
                neuralNetwork.backPropagation(t);
            }
        }
        Assert.assertEquals(Validator.validate(neuralNetwork, list, ChooseBy.Accuracy), 100.0, 0.0);
    }
}
