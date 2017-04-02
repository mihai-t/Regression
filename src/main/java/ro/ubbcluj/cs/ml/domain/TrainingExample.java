package ro.ubbcluj.cs.ml.domain;

import java.util.List;

public class TrainingExample {
    private static double min, max;
    private List<Double> features, targetValue;
    private Integer id = null;

    public TrainingExample(List<Double> features, List<Double> targetValue) {
        this.features = features;
        this.targetValue = targetValue;
    }

    public TrainingExample(int id, List<Double> features, List<Double> targetValue) {
        this.id = id;
        this.features = features;
        this.targetValue = targetValue;
    }

    public static double getMin() {
        return min;
    }

    public static void setMin(double min) {
        TrainingExample.min = min;
    }

    public static double getMax() {
        return max;
    }

    public static void setMax(double max) {
        TrainingExample.max = max;
    }

    public List<Double> getFeatures() {
        return features;
    }

    public List<Double> getTargetValue() {
        return targetValue;
    }

    public void setFeature(int position, double value) {
        features.set(position, value);
    }

    @Override
    public String toString() {
        return "TrainingExample{" + (id == null ? "" : "id=" + id + ", ") +
                "features=" + features +
                ", targetValue=" + targetValue +
                '}';
    }


}
