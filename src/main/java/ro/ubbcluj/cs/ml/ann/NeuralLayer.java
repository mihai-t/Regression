package ro.ubbcluj.cs.ml.ann;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A NeuralLayer is an ordered set of Neurons
 * Multiple NeuralLayers forms an ANN
 */
final class NeuralLayer implements Iterable<Neuron> {

    /**
     * Number of neurons composing this layer
     */
    private int numberOfNeurons;

    /**
     * Actual ordered set of Neurons
     */
    private List<Neuron> neuronList = new ArrayList<>();

    private NeuralLayer() {
    }

    /**
     * @param numberOfNeurons
     * @param numberOfInputsPerNeuron
     */
    public NeuralLayer(int numberOfNeurons, int numberOfInputsPerNeuron) {
        this.numberOfNeurons = numberOfNeurons;
        for (int i = 0; i < this.numberOfNeurons; ++i) {
            neuronList.add(new Neuron(numberOfInputsPerNeuron));
        }
    }

    @Override
    public Iterator<Neuron> iterator() {
        return neuronList.iterator();
    }

}
