package pse;

import java.util.List;

import java.util.ArrayList;

public class Main {

    static class Point implements Locationable {

        private Double[] mCoords;

        Point(Double x, Double y) {
            mCoords = new Double[] {x, y};
        }

        @Override
        public Double[] getCoords() {
            return mCoords;
        }
    }

    public static void main(String[] args) {

        List<Point> points = new ArrayList<>();
        points.add(new Point(2.0, 3.0));
        points.add(new Point(5.0, 4.0));
        points.add(new Point(9.0, 6.0));
        points.add(new Point(4.0, 7.0));
        points.add(new Point(8.0, 1.0));
        points.add(new Point(7.0, 2.0));
        points.add(new Point(6.0, 3.0));
        points.add(new Point(1.0, 8.0));
        points.add(new Point(3.0, 9.0));
        points.add(new Point(5.0, 8.0));
        points.add(new Point(4.0, 1.0));
        points.add(new Point(8.0, 9.0));

        KdTree tree1 = new KdTree();
        tree1.buildRecursive(points);

        KdTree tree2 = new KdTree();
        tree2.buildIterative(points);

        // Node nearest = tree.nearestNeighbour(new Double[] { 27.0, 5.0 });
    }
}
