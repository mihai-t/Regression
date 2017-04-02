package ro.ubbcluj.cs.ml.ann;

import ro.ubbcluj.cs.ml.domain.TrainingExample;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private static double eta = 0.05;//learning rate
    private static double alpha = 0.2;//momentum
    private static double omega = 0;//weight decay coefficient
    private int numberOfInputs, numberOfOutputs, numberOfHiddenLayers, numberOfNeuronPerHiddenLayer;
    private List<NeuralLayer> layerList = new ArrayList<>();
    private List<Double> inputMaxes, inputMinis;
    private Double outMax, outMin;
    private Problem problem;

    private NeuralNetwork() {
    }

    /**
     * @param numberOfInputs
     * @param numberOfOutputs
     * @param numberOfHiddenLayers
     * @param numberOfNeuronPerHiddenLayer
     */
    public NeuralNetwork(int numberOfInputs, int numberOfOutputs, int numberOfHiddenLayers, int numberOfNeuronPerHiddenLayer) {
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numberOfNeuronPerHiddenLayer = numberOfNeuronPerHiddenLayer;
        layerList.add(new NeuralLayer(this.numberOfNeuronPerHiddenLayer, numberOfInputs));
        for (int i = 1; i < numberOfHiddenLayers; ++i) {
            layerList.add(new NeuralLayer(this.numberOfNeuronPerHiddenLayer, numberOfNeuronPerHiddenLayer));
        }
        layerList.add(new NeuralLayer(this.numberOfOutputs, numberOfNeuronPerHiddenLayer));
    }

    /**
     * Creates a new ANN coping data from parameter
     *
     * @param neuralNetwork - ANN to be copied
     * @return - a new ANN
     */
    public static NeuralNetwork newForNetwork(NeuralNetwork neuralNetwork) {
        try {
            return (NeuralNetwork) neuralNetwork.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Propagate a list of inputs through ANN
     *
     * @param inputs - list of double
     * @return - list of output values
     * @throw IllegalArgumentException - if size of inputs != numberOfInputs
     */
    public List<Double> evaluate(List<Double> inputs) {
        List<Double> outputs = new ArrayList<>();
        if (inputs.size() != numberOfInputs) {
            throw new IllegalArgumentException("Illegal input size: " + inputs.size() + "!=" + numberOfInputs);
        }
        for (NeuralLayer neuralLayer : layerList) {
            for (Neuron neuron : neuralLayer) {
                outputs.add(neuron.function(inputs));
            }
            inputs = new ArrayList<>(outputs);
            outputs = new ArrayList<>();
        }
        return inputs;
    }

    /**
     * Propagate a list of inputs from a training example
     * through ANN and compute the quadratic error
     * E=(sum(Xi-Ti)^2)/2
     * i=1..numberOfOutputs
     * Xi=obtained value
     * Ti=target value
     *
     * @param trainingExample
     * @return E
     */
    public double computeError(TrainingExample trainingExample) {
        List<Double> targetList = trainingExample.getTargetValue();
        double error = 0;
        List<Double> values = this.evaluate(trainingExample.getFeatures());
        for (int i = 0; i < values.size(); ++i) {
            error += Math.pow(values.get(i) - targetList.get(i), 2);
        }
        error /= 2;
        return error;
    }

    /**
     * Apply back propagation for a given training example
     *
     * @param trainingExample
     */
    public void backPropagation(TrainingExample trainingExample) {
        this.evaluate(trainingExample.getFeatures());
        List<Double> targetList = trainingExample.getTargetValue();
        int index = 0;
        double delta, sum;
        for (Neuron neuron : layerList.get(numberOfHiddenLayers)) {//output units
            delta = neuron.getOutputValue() * (1 - neuron.getOutputValue()) * (targetList.get(index) - neuron.getOutputValue());
            neuron.setDelta(delta);
            index++;
        }
        for (int i = numberOfHiddenLayers - 1; i >= 0; i--) {//hidden layers
            index = 1;
            for (Neuron neuron : layerList.get(i)) {
                sum = 0;
                for (Neuron neuronO : layerList.get(i + 1)) {
                    sum += neuronO.getWeight(index) * neuronO.getDelta();
                }
                delta = neuron.getOutputValue() * (1 - neuron.getOutputValue()) * sum;
                neuron.setDelta(delta);
                index++;
            }
        }
        double deltaW, weight;
        for (NeuralLayer neuralLayer : layerList) {
            for (Neuron neuron : neuralLayer) {
                weight = neuron.getWeight(0);
                deltaW = NeuralNetwork.eta * neuron.getDelta() * 1 + alpha * neuron.getDeltaW(0) - omega * weight;
                neuron.setWeight(0, weight + deltaW);
                neuron.setDeltaW(0, deltaW);
                for (int i = 0; i < neuron.getNumberOfInputs(); ++i) {
                    weight = neuron.getWeight(i + 1);
                    deltaW = NeuralNetwork.eta * neuron.getDelta() * neuron.getInputValue(i) + alpha * neuron.getDeltaW(i + 1) - omega * weight;
                    neuron.setWeight(i + 1, weight + deltaW);
                    neuron.setDeltaW(i + 1, deltaW);
                }
            }
        }
    }

    public int getNumberOfOutputs() {
        return numberOfOutputs;
    }

}
