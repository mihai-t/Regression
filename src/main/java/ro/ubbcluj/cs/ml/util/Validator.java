package ro.ubbcluj.cs.ml.util;

import ro.ubbcluj.cs.ml.ann.ChooseBy;
import ro.ubbcluj.cs.ml.ann.NeuralNetwork;
import ro.ubbcluj.cs.ml.domain.TrainingExample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Validator {


    public static double validate(NeuralNetwork neuralNetwork, List<TrainingExample> list, ChooseBy chooseBy) {
        switch (chooseBy) {
            case Accuracy:
                return validateClassification(neuralNetwork, list);
            case AUC:
                return computeAUC(neuralNetwork, list);
            case Sensitivity:
                return computeMeasure(neuralNetwork, list, Arrays.asList(0));
            case Specificity:
                return computeMeasure(neuralNetwork, list, Arrays.asList(1));
            case MAE:
                return validateMAE(neuralNetwork, list);
            default:
                return 0;
        }
    }

    public static double computeMeanError(NeuralNetwork neuralNetwork, List<TrainingExample> list) {
        double error = 0;
        for (TrainingExample trainingExample : list) {
            error += neuralNetwork.computeError(trainingExample);
        }
        return error / list.size();
    }

    /**
     * Compute the accuracy of an ANN over a set of training examples
     *
     * @param neuralNetwork
     * @param list
     * @return - Computed accuracy
     */
    private static double validateClassification(NeuralNetwork neuralNetwork, List<TrainingExample> list) {
        double maxim;
        int good = 0;
        for (TrainingExample trainingExample : list) {
            maxim = 0;
            List<Double> outputs = neuralNetwork.evaluate(trainingExample.getFeatures());
            for (Double d : outputs) {
                if (d > maxim) {
                    maxim = d;
                }
            }
            if (outputs.indexOf(maxim) == trainingExample.getTargetValue().indexOf(0.9)) {
                good++;
            }
        }
        return (1.0 * good / list.size()) * 100;
    }

    /**
     * @param neuralNetwork
     * @param list
     * @return
     */
    private static double validateMAE(NeuralNetwork neuralNetwork, List<TrainingExample> list) {
        double result = 0.0;
        if (list.size() == 0) {
            return 0;
        }
        double min = TrainingExample.getMin();
        double max = TrainingExample.getMax();
        for (TrainingExample trainingExample : list) {
            double target = trainingExample.getTargetValue().get(0);
            List<Double> outputs = neuralNetwork.evaluate(trainingExample.getFeatures());
            result += Math.abs((max - min) * (outputs.get(0) - target));
        }
        return result / list.size();
    }

    public static double validateMax(NeuralNetwork neuralNetwork, List<TrainingExample> list) {
        int wrong = 0;
        for (TrainingExample trainingExample : list) {
            List<Double> outputs = neuralNetwork.evaluate(trainingExample.getFeatures());
            if (outputs.get(0) < 0.5) {
                if (trainingExample.getTargetValue().get(0) > 0.5) {
                    wrong++;
                }
            } else {
                if (trainingExample.getTargetValue().get(0) < 0.5) {
                    wrong++;
                }
            }
        }
        return 100 - ((1.0 * wrong / list.size()) * 100);
    }

    /**
     * Computes the Sensitivity or the Specificity for ANN evaluator
     *
     * @param neuralNetwork - given ANN
     * @param list          - list of training examples
     * @param which         - determines the class for which to compute the accuracy
     *                      which = 0 -> Sensitivity of ANN evaluator
     *                      which = 1 -> Specificity of ANN evaluator
     * @return
     */
    private static double computeMeasure(NeuralNetwork neuralNetwork, List<TrainingExample> list, List<Integer> which) {
        int total = 0, good = 0;
        for (TrainingExample trainingExample : list) {
            if (!which.contains(trainingExample.getTargetValue().indexOf(0.9))) {
                continue;
            }
            total++;
            double maxim = 0;
            List<Double> outputs = neuralNetwork.evaluate(trainingExample.getFeatures());
            for (Double d : outputs) {
                if (d > maxim) {
                    maxim = d;
                }
            }
            if (outputs.indexOf(maxim) == trainingExample.getTargetValue().indexOf(0.9)) {
                good++;
            }
        }
        if (total == 0) {
            return 1;
        }
        return (1.0 * good) / total;
    }

    /**
     * Computes the Area Under the Curve for sensitivity - specificity chart
     *
     * @param neuralNetwork
     * @param list
     * @return AUC
     */
    private static double computeAUC(NeuralNetwork neuralNetwork, List<TrainingExample> list) {
        int size = neuralNetwork.getNumberOfOutputs();
        double sensitivity, specificity;
        if (size < 2) {
            return 0;
        }
        if (size == 2) {
            sensitivity = computeMeasure(neuralNetwork, list, Arrays.asList(0));
            specificity = computeMeasure(neuralNetwork, list, Arrays.asList(1));
            return sensitivity * specificity + ((1 - specificity) * sensitivity) / 2 + ((1 - sensitivity) * specificity) / 2;
        } else {
            double auc = 0;
            for (int i = 0; i < size; ++i) {
                sensitivity = computeMeasure(neuralNetwork, list, Arrays.asList(i));
                List<Integer> rest = new ArrayList<>();
                for (int j = 0; j < size; ++j) {
                    if (i != j) {
                        rest.add(j);
                    }
                }
                specificity = computeMeasure(neuralNetwork, list, rest);
                auc += sensitivity * specificity + ((1 - specificity) * sensitivity) / 2 + ((1 - sensitivity) * specificity) / 2;
            }
            return (1.0 * auc) / size;
        }
    }


}
