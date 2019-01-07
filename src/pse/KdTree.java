package pse;

import java.util.*;

public class KdTree {

    private Node<? extends Dimensional> mRootNode;

    // There are 2 axis for 2-d space
    public final static int DIMENSIONS = 2;

    private static class LocationComparator implements Comparator<Dimensional> {

        private final int mAxis;

        LocationComparator(int axis) {
            mAxis = axis;
        }

        @Override
        public int compare(Dimensional o1, Dimensional o2) {
            return o1.getCoords()[mAxis].compareTo(o2.getCoords()[mAxis]);
        }
    }

    private static class Partition<T extends Dimensional> extends ArrayList<T> {
        char mPosition;
        Node mParentNode;

        Partition(List<T> items, char position, Node parentNode) {
            super(items);
            mPosition = position;
            mParentNode = parentNode;
        }
    }

    public void buildRecursive(List<? extends Dimensional> points) {
        mRootNode = buildRecursive(points, 0);
    }

    public Node nearestNeighbour(Double[] point) {
        if (mRootNode == null) {
            throw new IllegalStateException();
        }

        NearestNeighbour nearestNeighbour = new NearestNeighbour();
        return nearestNeighbour.search(mRootNode, point);
    }

    public Node buildIterative(List<? extends Dimensional> items) {
        if (items.isEmpty()) {
            return null;
        }

        Collections.sort(items, new LocationComparator(0));

        int partitionLength = items.size();
        int partitionMedianIndex = partitionLength / 2;
        Dimensional partitionMedianItem = items.get(partitionMedianIndex);

        mRootNode = new Node<>(null, null, null, partitionMedianItem, 0);

        Partition<? extends Dimensional> partitionBeforeMedian = new Partition<>(items.subList(0, partitionMedianIndex), 'L', mRootNode);
        Partition<? extends Dimensional> partitionAfterMedian = new Partition<>(items.subList(partitionMedianIndex + 1, partitionLength), 'R', mRootNode);

        final Stack<Partition<? extends Dimensional>> stack = new Stack<>();

        stack.push(partitionBeforeMedian);
        stack.push(partitionAfterMedian);

        while (!stack.empty()) {
            Partition<? extends Dimensional> partition = stack.pop();

            if (partition.isEmpty()) {
                continue;
            }

            final Node parentNode = partition.mParentNode;
            final int newDepth = parentNode.getDepth() + 1;

            if (partition.size() == 1) {
                // This is a leaf

                final Dimensional newItem = partition.get(0);
                final Node newNode = new Node<>(parentNode, null, null, newItem, newDepth);

                if (partition.mPosition == 'L') {
                    parentNode.setLeftNode(newNode);
                } else {
                    parentNode.setRightNode(newNode);
                }
            } else {
                Collections.sort(partition, new LocationComparator(newDepth % DIMENSIONS));

                partitionLength = partition.size();
                partitionMedianIndex = partitionLength / 2;
                partitionMedianItem = partition.get(partitionMedianIndex);

                final Node newNode = new Node<>(parentNode, null, null, partitionMedianItem, newDepth);

                if (partition.mPosition == 'L') {
                    parentNode.setLeftNode(newNode);
                } else {
                    parentNode.setRightNode(newNode);
                }

                partitionBeforeMedian = new Partition<>(partition.subList(0, partitionMedianIndex), 'L', newNode);
                stack.push(partitionBeforeMedian);

                final List<? extends Dimensional> subList = (partitionMedianIndex + 1 < partitionLength)
                        ? partition.subList(partitionMedianIndex + 1, partitionLength)
                        : new ArrayList<Dimensional>();

                partitionAfterMedian = new Partition<>(subList, 'R', newNode);
                stack.push(partitionAfterMedian);
            }
        }

        return mRootNode;
    }

    private Node<? extends Dimensional> buildRecursive(List<? extends Dimensional> items, int depth) {
        if (items.isEmpty()) {
            return null;
        }

        final int axis = depth % DIMENSIONS;

        Collections.sort(items, new LocationComparator(axis));

        final int length = items.size();
        final int medianIndex = length / 2;

        final Dimensional medianPoint = items.get(medianIndex);

        final List<? extends Dimensional> beforeMedianPoints = items.subList(0, medianIndex);
        final Node leftChild = buildRecursive(beforeMedianPoints, depth + 1);

        final List<? extends Dimensional> afterMedianPoints = items.subList(medianIndex + 1, length);
        final Node rightChild = length > 1 ? buildRecursive(afterMedianPoints, depth + 1) : null;

        return new Node<>(null, leftChild, rightChild, medianPoint, depth);
    }
}
