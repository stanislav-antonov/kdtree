package pse;

import java.util.List;

import java.util.ArrayList;

public class Main {

    static class Item implements Dimensional {

        private Double[] mCoords;

        Item(Double x, Double y) {
            mCoords = new Double[] {x, y};
        }

        @Override
        public Double[] getCoords() {
            return mCoords;
        }
    }

    public static void main(String[] args) {

        List<Item> items = new ArrayList<>();
        items.add(new Item(2.0, 3.0));
        items.add(new Item(5.0, 4.0));
        items.add(new Item(9.0, 6.0));
        items.add(new Item(4.0, 7.0));
        items.add(new Item(8.0, 1.0));
        items.add(new Item(7.0, 2.0));
        items.add(new Item(6.0, 3.0));

        KdTree tree2 = new KdTree();
        tree2.buildIterative(items);

        Node nearest = tree2.nearestNeighbour(new Double[] { 7.0, 5.0 });
        List<Node> knearest = tree2.kNearestNeighbour(new Double[] { 7.0, 5.0 }, 3);
    }
}
