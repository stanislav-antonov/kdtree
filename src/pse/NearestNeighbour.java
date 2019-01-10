package pse;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import static pse.KdTree.DIMENSIONS;

public class NearestNeighbour {

    private static class Distance {
        Node mNode;
        Double mValue;

        Distance(double distance, Node node) {
            mNode = node;
            mValue = distance;
        }
    }

    private static class MinPriorityQueue extends PriorityQueue<Distance> {
        private static Comparator<Distance> mComparator = new Comparator<Distance>() {
            @Override
            public int compare(Distance o1, Distance o2) {
                return o1.mValue.compareTo(o2.mValue);
            }
        };

        MinPriorityQueue() {
            super(11, mComparator);
        }
    }

    private static class MaxPriorityQueue extends PriorityQueue<Distance> {
        private static Comparator<Distance> mComparator = new Comparator<Distance>() {
            @Override
            public int compare(Distance o1, Distance o2) {
                return -1 * o1.mValue.compareTo(o2.mValue);
            }
        };

        MaxPriorityQueue() {
            super(11, mComparator);
        }
    }

    public static Node nearest(Node rootNode, Double[] point) {
        if (rootNode == null) {
            return null;
        }

        final MinPriorityQueue minPriorityQueue = new MinPriorityQueue();
        minPriorityQueue.add(new Distance(0, rootNode));

        Distance bestDistance = new Distance(Double.MAX_VALUE, rootNode);

        while (!minPriorityQueue.isEmpty()) {
            final Distance currentDistance = minPriorityQueue.poll();

            if (currentDistance.mValue >= bestDistance.mValue) {
                return bestDistance.mNode;
            }

            final Node currentNode = currentDistance.mNode;
            final double distanceFromCurrentNode = distance(point, currentNode.getCoords());

            if (distanceFromCurrentNode < bestDistance.mValue) {
                bestDistance.mNode = currentNode;
                bestDistance.mValue = distanceFromCurrentNode;
            }

            final int axis = currentNode.getDepth() % DIMENSIONS;
            double delta = point[axis] - currentNode.getCoords()[axis];

            Node away = currentNode.getLeftNode();
            Node near = currentNode.getRightNode();

            if (delta <= 0) {
                away = currentNode.getRightNode();
                near = currentNode.getLeftNode();
            }

            if (away != null) {
                minPriorityQueue.add(new Distance(delta, away));
            }

            if (near != null) {
                minPriorityQueue.add(new Distance(0, near));
            }
        }

        return bestDistance.mNode;
    }

    public static List<Node> kNearest(Node rootNode, Double[] point, int k) {
        if (rootNode == null) {
            return null;
        }

        final MaxPriorityQueue maxPriorityQueue = new MaxPriorityQueue();
        maxPriorityQueue.add(new Distance(Double.MAX_VALUE, rootNode));

        final MinPriorityQueue minPriorityQueue = new MinPriorityQueue();
        minPriorityQueue.add(new Distance(0, rootNode));

        while (!minPriorityQueue.isEmpty()) {
            final Distance currentDistance = minPriorityQueue.poll();

            if (currentDistance.mValue >= maxPriorityQueue.peek().mValue) {
                break;
            }

            final Node currentNode = currentDistance.mNode;
            final double distanceFromCurrentNode = distance(point, currentNode.getCoords());
            final double maxDistance = maxPriorityQueue.peek().mValue;

            if (maxPriorityQueue.size() < k || distanceFromCurrentNode <= maxDistance) {
                maxPriorityQueue.add(new Distance(distanceFromCurrentNode, currentNode));

                if (maxPriorityQueue.size() > k) {
                    maxPriorityQueue.poll();
                }
            }

            final int axis = currentNode.getDepth() % DIMENSIONS;
            double delta = point[axis] - currentNode.getCoords()[axis];

            Node away = currentNode.getLeftNode();
            Node near = currentNode.getRightNode();

            if (delta <= 0) {
                away = currentNode.getRightNode();
                near = currentNode.getLeftNode();
            }

            if (away != null) {
                minPriorityQueue.add(new Distance(delta, away));
            }

            if (near != null) {
                minPriorityQueue.add(new Distance(0, near));
            }
        }

        List<Node> result = new ArrayList<>();
        for (Distance distance : maxPriorityQueue) {
            result.add(distance.mNode);
        }

        return result;
    }

    private static double distance(Double[] point1, Double[] point2) {
        return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
    }
}
