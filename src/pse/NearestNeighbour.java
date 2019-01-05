package pse;

import static pse.KdTree.DIMENSIONS;

public class NearestNeighbour {

    private Node mClosestNode;
    private double mMinDist;

    public Node search(Node node, Double[] point) {
        mClosestNode = null;
        mMinDist = Double.MAX_VALUE;

        search(node, point, 0);

        return mClosestNode;
    }

    private void search(Node node, Double[] point, int depth) {
        if (node == null) {
            return;
        }

        System.out.println("node: " + node.getCoords()[0] + ", " + node.getCoords()[1]);

        final double distance = distance(point, node.getCoords());
        System.out.println("dist: " + distance + ", min dist: " + mMinDist);

        if (distance < mMinDist) {
            mMinDist = distance;
            mClosestNode = node;
        }

        final int axis = depth % DIMENSIONS;

        if (point[axis] < node.getCoords()[axis]) {
            search(node.getLeftNode(), point, depth + 1);

            if (point[axis] + mMinDist >= node.getCoords()[axis]) {
                search(node.getRightNode(), point, depth + 1);
            }
        } else {
            search(node.getRightNode(), point, depth + 1);

            if (point[axis] - mMinDist <= node.getCoords()[axis]) {
                search(node.getLeftNode(), point, depth + 1);
            }
        }
    }

    private double distance(Double[] point1, Double[] point2) {
        return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
    }
}
