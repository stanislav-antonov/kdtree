package pse.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pse.KdTree;
import pse.Node;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbourTest {

    private final List<Item> mItems = new ArrayList<>();
    private final KdTree mTree = new KdTree();

    @Before
    public void setUp() throws Exception {
        mItems.add(new Item(2.0, 3.0));
        mItems.add(new Item(5.0, 4.0));
        mItems.add(new Item(9.0, 6.0));
        mItems.add(new Item(4.0, 7.0));
        mItems.add(new Item(8.0, 1.0));
        mItems.add(new Item(7.0, 2.0));
        mItems.add(new Item(6.0, 3.0));

        mTree.buildIterative(mItems);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void nearest() throws Exception {
        final Node nearest = mTree.nearestNeighbour(new Double[] { 7.0, 5.0 });

        Assert.assertNotNull(nearest);
        Assert.assertArrayEquals(new Double[] { 6.0, 3.0 }, nearest.getCoords());
    }

    @Test
    public void kNearest() throws Exception {
        final int k = 3;
        final List<Node> kNearest = mTree.kNearestNeighbour(new Double[] { 7.0, 5.0 }, k);

        Assert.assertNotNull(kNearest);
        Assert.assertTrue(kNearest.size() == k);

        Assert.assertTrue(kNearest.contains(new Node<>(new Item(6.0, 3.0))));
        Assert.assertTrue(kNearest.contains(new Node<>(new Item(9.0, 6.0))));
        Assert.assertTrue(kNearest.contains(new Node<>(new Item(5.0, 4.0))));
    }
}