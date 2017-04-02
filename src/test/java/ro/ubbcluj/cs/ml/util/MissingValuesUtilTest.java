package ro.ubbcluj.cs.ml.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.ubbcluj.cs.ml.domain.TrainingExample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class MissingValuesUtilTest {

    private List<TrainingExample> trainingExamples;

    @Before
    public void setUp() {
        trainingExamples = new ArrayList<>();
        trainingExamples.add(new TrainingExample(Arrays.asList(-1.0, 12.0, -1.0, -1.0), Arrays.asList()));
        trainingExamples.add(new TrainingExample(Arrays.asList(1.0, 1.0, -1.0, 1.0), Arrays.asList()));
        trainingExamples.add(new TrainingExample(Arrays.asList(2.0, 2.0, -1.0, 3.25), Arrays.asList()));
        trainingExamples.add(new TrainingExample(Arrays.asList(3.0, 3.5, -1.0, 4.0), Arrays.asList()));
        trainingExamples.add(new TrainingExample(Arrays.asList(-1.0, -1.0, -1.0, 1.0), Arrays.asList()));
    }

    @Test
    public void checkAverage() {
        MissingValuesUtil.placeAverage(trainingExamples);
        Assert.assertEquals(trainingExamples.get(0).getFeatures(), Arrays.asList(2.0, 12.0, 0.0, 2.3125));
        Assert.assertEquals(trainingExamples.get(1).getFeatures(), Arrays.asList(1.0, 1.0, 0.0, 1.0));
        Assert.assertEquals(trainingExamples.get(2).getFeatures(), Arrays.asList(2.0, 2.0, 0.0, 3.25));
        Assert.assertEquals(trainingExamples.get(3).getFeatures(), Arrays.asList(3.0, 3.5, 0.0, 4.0));
        Assert.assertEquals(trainingExamples.get(4).getFeatures(), Arrays.asList(2.0, 4.625, 0.0, 1.0));
    }

    @Test
    public void checkConstant() {
        MissingValuesUtil.placeConstant(trainingExamples, 4);
        Assert.assertEquals(trainingExamples.get(0).getFeatures(), Arrays.asList(4.0, 12.0, 4.0, 4.0));
        Assert.assertEquals(trainingExamples.get(1).getFeatures(), Arrays.asList(1.0, 1.0, 4.0, 1.0));
        Assert.assertEquals(trainingExamples.get(2).getFeatures(), Arrays.asList(2.0, 2.0, 4.0, 3.25));
        Assert.assertEquals(trainingExamples.get(3).getFeatures(), Arrays.asList(3.0, 3.5, 4.0, 4.0));
        Assert.assertEquals(trainingExamples.get(4).getFeatures(), Arrays.asList(4.0, 4.0, 4.0, 1.0));
    }
}
