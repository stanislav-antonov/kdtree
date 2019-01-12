package pse.tests;

import pse.Dimensional;

class Item implements Dimensional {

    private Double[] mCoords;

    Item(Double x, Double y) {
        mCoords = new Double[] {x, y};
    }

    @Override
    public Double[] getCoords() {
        return mCoords;
    }
}
