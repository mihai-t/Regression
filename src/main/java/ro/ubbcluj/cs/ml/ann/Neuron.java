package ro.ubbcluj.cs.ml.ann;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A Neuron is the functional component of an Artificial
 * neural network (ANN).
 * Neurons are grouped in NeuralLayers
 */
@JsonIgnoreProperties({"deltaW", "outputValue", "delta", "inputValues"})
class Neuron {

    private static final Random random = new Random();

    /**
     * Number of neurons which send information to this neuron
     */
    private int numberOfInputs;

    /**
     * Used to store given values from inputs
     */
    private List<Double> inputValues;

    /**
     * Weights of this neuron
     * weights = {w[0],w[1],..,w[numberOfInputs]}
     */
    private List<Double> weights = new ArrayList<>();

    /**
     * Used to store the output of this neuron
     */
    private double outputValue;

    /**
     * Used to store the error term of this neuron
     */
    private double delta;

    /**
     * Used to store the error for each weight
     * deltaW[i] = eta * delta[j][i] * input[j][i]
     * where j is the corespondent neuron from the
     * previous layer
     */
    private List<Double> deltaW = new ArrayList<>();

    private Neuron() {
    }

    public Neuron(int numberOfInputs) {
        this.numberOfInputs = numberOfInputs;
        for (int i = -1; i < this.numberOfInputs; ++i) {
            //weight initialization (-0.5..0.5)
            weights.add((random.nextDouble() - 0.5));
            deltaW.add(0.0);
        }
    }

    /**
     * f=sigmoid(w0+w1*x1+..+wn*xn)
     *
     * @param inputs [x1..xn]
     * @return f
     */
    protected double function(List<Double> inputs) {
        if (inputs.size() != numberOfInputs) {
            throw new IllegalArgumentException("Illegal number of inputs" + inputs.size() + "!=" + numberOfInputs);
        }
        inputValues = new ArrayList<>(inputs);
        double output = weights.get(0);
        for (int i = 1; i < numberOfInputs + 1; ++i) {
            output += weights.get(i) * inputs.get(i - 1);
        }
        outputValue = sigmoid(output);
        return outputValue;
    }

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    public double getWeight(int i) {
        return weights.get(i);
    }

    protected void setWeight(int i, double value) {
        weights.set(i, value);
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    protected double getOutputValue() {
        return outputValue;
    }

    protected double getDelta() {
        return delta;
    }

    protected void setDelta(double delta) {
        this.delta = delta;
    }

    protected double getInputValue(int i) {
        return inputValues.get(i);
    }

    protected double getDeltaW(int i) {
        return deltaW.get(i);
    }

    protected void setDeltaW(int i, double deltaW) {
        this.deltaW.set(i, deltaW);
    }

}
