package ro.ubbcluj.cs.ml.training;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.ubbcluj.cs.ml.util.Statistics;

/**
 * @author Mihai Teletin
 */
public class StatisticsTest {

    private Statistics statistics;

    @Before
    public void setUp() {
        statistics = new Statistics();
    }

    @Test
    public void testMinMax() {
        statistics.addValue(1);
        Assert.assertEquals(1.0, statistics.getMin(), 0.0);
        Assert.assertEquals(1.0, statistics.getMax(), 0.0);
        statistics.addValue(3);
        Assert.assertEquals(1.0, statistics.getMin(), 0.0);
        Assert.assertEquals(3.0, statistics.getMax(), 0.0);
        statistics.addValue(0);
        Assert.assertEquals(0.0, statistics.getMin(), 0.0);
        Assert.assertEquals(3.0, statistics.getMax(), 0.0);
        statistics.addValue(5.1);
        Assert.assertEquals(0.0, statistics.getMin(), 0.0);
        Assert.assertEquals(5.1, statistics.getMax(), 0.0);
        statistics.addValue(-100);
        Assert.assertEquals(-100.0, statistics.getMin(), 0.0);
        Assert.assertEquals(5.1, statistics.getMax(), 0.0);
    }

    @Test
    public void testMean() {
        statistics.addValue(1);
        Assert.assertEquals(1.0, statistics.getMean(), 0.0);
        statistics.addValue(3);
        Assert.assertEquals(2.0, statistics.getMean(), 0.0);
        statistics.addValue(2);
        Assert.assertEquals(2.0, statistics.getMean(), 0.0);
        statistics.addValue(5.1);
        Assert.assertEquals(2.775, statistics.getMean(), 0.0);
        statistics.addValue(0.9);
        Assert.assertEquals(3.0, statistics.getMean(2), 0.0);
    }

    @Test
    public void testStandardDeviation() {
        statistics.addValue(1);
        Assert.assertEquals(0.0, statistics.getStandardDeviation(), 0.0);
        statistics.addValue(2);
        statistics.addValue(3);
        Assert.assertEquals(2.0, statistics.getMean(), 0.0);
        Assert.assertEquals(Math.sqrt(2) / Math.sqrt(3), statistics.getStandardDeviation(), 0.0000001);
        statistics.addValue(3);
        Assert.assertEquals(0, statistics.getStandardDeviation(2), 0.0);
        Assert.assertEquals(0, statistics.getStandardDeviation(1), 0.0);
    }
}
