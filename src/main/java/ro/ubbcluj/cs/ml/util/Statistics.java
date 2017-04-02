package ro.ubbcluj.cs.ml.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihai Teletin on 24-Jan-16.
 */
public class Statistics {
    private List<Double> values = new ArrayList<>();
    private Double min;
    private Double max;


    public void addValue(double value) {
        if (min == null) {
            min = value;
            max = value;
        } else {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        values.add(value);
    }

    public double getMean() {
        return values.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    }

    /**
     * Computes mean of last k elements
     *
     * @param k
     * @return
     */
    public double getMean(int k) {
        return values.stream().skip(Math.max(0, values.size() - k)).mapToDouble(Double::doubleValue).average().getAsDouble();
    }

    public double getStandardDeviation() {
        double sum = 0;
        double mean = getMean();
        for (double v : values) {
            sum += Math.pow(v - mean, 2);
        }
        sum /= values.size();
        return Math.sqrt(sum);
    }

    /**
     * Computes stdev of last k elements
     *
     * @param k
     * @return
     */
    public double getStandardDeviation(int k) {
        double sum = 0;
        double mean = getMean(k);
        for (int i = values.size() - k; i < values.size(); ++i) {
            sum += Math.pow(values.get(i) - mean, 2);
        }
        sum /= k;
        return Math.sqrt(sum);
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }
}
