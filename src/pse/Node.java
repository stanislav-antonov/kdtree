package pse;

import java.util.Arrays;

public class Node<T extends Dimensional> implements Dimensional {

    private Node mParentNode;
    private Node mLeftNode;
    private Node mRightNode;

    private final int mDepth;

    private final T mData;

    public Node(T data) {
        this(null, null, null, data, 0);
    }

    public Node(Node parentNode, Node leftNode, Node rightNode, T data, int depth) {
        mParentNode = parentNode;
        mLeftNode = leftNode;
        mRightNode = rightNode;
        mData = data;
        mDepth = depth;
    }

    public boolean isLeaf() {
        return mLeftNode == null && mRightNode == null;
    }

    public int getDepth() {
        return mDepth;
    }

    public Node getParentNode() {
        return mParentNode;
    }

    public Node getLeftNode() {
        return mLeftNode;
    }

    public Node getRightNode() {
        return mRightNode;
    }

    @Override
    public Double[] getCoords() {
        return mData.getCoords();
    }

    public void setParentNode(Node node) {
        mParentNode = node;
    }

    public void setLeftNode(Node node) {
        mLeftNode = node;
    }

    public void setRightNode(Node node) {
        mRightNode = node;
    }

    public T getData() {
        return mData;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            Double[] c1 = ((Node) o).getCoords();
            Double[] c2 = this.getCoords();

            return Arrays.equals(c1, c2);
        }

        return false;
    }
}
