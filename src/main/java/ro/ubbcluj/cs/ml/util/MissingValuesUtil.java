package ro.ubbcluj.cs.ml.util;

import ro.ubbcluj.cs.ml.domain.TrainingExample;

import java.util.List;

/**
 * @author Mihai Teletin
 */
public class MissingValuesUtil {

    public static final double missingFlag = -1.0;

    /**
     * Replaces all the missing values with a constant
     *
     * @param trainingExamples - given list
     * @param constant         - to replace
     */
    public static void placeConstant(List<TrainingExample> trainingExamples, double constant) {
        for (TrainingExample trainingExample : trainingExamples) {
            for (int i = 0; i < trainingExample.getFeatures().size(); ++i) {
                if (trainingExample.getFeatures().get(i) == missingFlag) {
                    trainingExample.setFeature(i, constant);
                }
            }
        }
    }

    /**
     * Replaces all the missing values with the
     * average of the others existing values
     *
     * @param trainingExamples - to replace
     */
    public static void placeAverage(List<TrainingExample> trainingExamples) {
        if (trainingExamples == null || trainingExamples.size() == 0) {
            return;
        }
        final int size = trainingExamples.get(0).getFeatures().size();
        double[] averages = new double[size];
        int[] totals = new int[size];
        boolean used = false;
        for (TrainingExample trainingExample : trainingExamples) {
            for (int i = 0; i < size; ++i) {
                if (trainingExample.getFeatures().get(i) != missingFlag) {
                    averages[i] += trainingExample.getFeatures().get(i);
                    totals[i]++;
                } else {
                    used = true;
                }
            }
        }


        if (!used) {
            System.out.println("There are no missing values");
            return;
        }
        for (int i = 0; i < size; ++i) {
            if (totals[i] != 0) {
                averages[i] /= totals[i];
            }
        }

        for (TrainingExample trainingExample : trainingExamples) {
            for (int i = 0; i < trainingExample.getFeatures().size(); ++i) {
                if (trainingExample.getFeatures().get(i) == missingFlag) {
                    trainingExample.setFeature(i, averages[i]);
                }
            }
        }

    }
}
