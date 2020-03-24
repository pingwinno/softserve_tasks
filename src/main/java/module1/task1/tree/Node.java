package module1.task1.tree;

public class Node {
    Node left;
    Node right;
    private int data;

    Node(int d) {
        data = d;
        left = null;
        right = null;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}