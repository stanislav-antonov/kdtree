package pse;

import java.util.*;

public class KdTree {

    private Node<? extends Locationable> mRoot;

    // There are 2 axis for 2-d space
    public final static int DIMENSIONS = 2;

    private static class LocationComparator implements Comparator<Locationable> {

        private final int mAxis;

        LocationComparator(int axis) {
            mAxis = axis;
        }

        @Override
        public int compare(Locationable o1, Locationable o2) {
            return o1.getCoords()[mAxis].compareTo(o2.getCoords()[mAxis]);
        }
    }

    private static class Partition<T extends Locationable> extends ArrayList<T> {
        char mPosition;
        Node mParent;

        Partition(List<T> list, char side, Node parent) {
            super(list);
            mPosition = side;
            mParent = parent;
        }
    }

    public void buildRecursive(List<? extends Locationable> points) {
        mRoot = buildRecursive(points, 0);
    }

    public Node nearestNeighbour(Double[] point) {
        if (mRoot == null) {
            throw new IllegalStateException();
        }

        NearestNeighbour nearestNeighbour = new NearestNeighbour();
        return nearestNeighbour.search(mRoot, point);
    }

    private Node<? extends Locationable> buildRecursive(List<? extends Locationable> points, int depth) {
        if (points.isEmpty()) {
            return null;
        }

        final int axis = depth % DIMENSIONS;

        Collections.sort(points, new LocationComparator(axis));

        final int length = points.size();
        final int medianIndex = length / 2;

        final Locationable medianPoint = points.get(medianIndex);

        final List<? extends Locationable> beforeMedianPoints = points.subList(0, medianIndex);
        final Node leftChild = buildRecursive(beforeMedianPoints, depth + 1);

        final List<? extends Locationable> afterMedianPoints = points.subList(medianIndex + 1, length);
        final Node rightChild = length > 1 ? buildRecursive(afterMedianPoints, depth + 1) : null;

        return new Node<>(null, leftChild, rightChild, medianPoint, depth);
    }

    public Node buildIterative(List<? extends Locationable> points) {
        if (points.isEmpty()) {
            return null;
        }

        Collections.sort(points, new LocationComparator(0));

        int length = points.size();
        int medianIndex = length / 2;
        Locationable median = points.get(medianIndex);

        mRoot = new Node<>(null, null, null, median, 0);

        Partition<? extends Locationable> partitionBeforeMedian = new Partition<>(points.subList(0, medianIndex), 'L', mRoot);
        Partition<? extends Locationable> partitionAfterMedian = new Partition<>(points.subList(medianIndex + 1, length), 'R', mRoot);

        final Stack<Partition<? extends Locationable>> stack = new Stack<>();

        stack.push(partitionBeforeMedian);
        stack.push(partitionAfterMedian);

        while (!stack.empty()) {
            Partition<? extends Locationable> partition = stack.pop();

            if (partition.isEmpty()) {
                continue;
            }

            final Node parentNode = partition.mParent;
            final int newDepth = parentNode.getDepth() + 1;

            if (partition.size() == 1) {
                // This is a leaf

                final Locationable newPoint = partition.get(0);
                final Node newNode = new Node<>(parentNode, null, null, newPoint, newDepth);

                if (partition.mPosition == 'L') {
                    parentNode.setLeftNode(newNode);
                } else {
                    parentNode.setRightNode(newNode);
                }
            } else {
                Collections.sort(partition, new LocationComparator(newDepth % DIMENSIONS));

                length = partition.size();
                medianIndex = length / 2;
                median = partition.get(medianIndex);

                final Node newNode = new Node<>(parentNode, null, null, median, newDepth);

                if (partition.mPosition == 'L') {
                    parentNode.setLeftNode(newNode);
                } else {
                    parentNode.setRightNode(newNode);
                }

                partitionBeforeMedian = new Partition<>(partition.subList(0, medianIndex), 'L', newNode);
                stack.push(partitionBeforeMedian);

                final List<? extends Locationable> subList = (medianIndex + 1 < length) ? partition.subList(medianIndex + 1, length)
                        : new ArrayList<Locationable>();

                partitionAfterMedian = new Partition<>(subList, 'R', newNode);
                stack.push(partitionAfterMedian);
            }
        }

        return mRoot;
    }
}
