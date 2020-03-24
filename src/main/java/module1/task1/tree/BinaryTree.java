package module1.task1.tree;

public class BinaryTree {
    Node root;

    public boolean isBalanced() {
        if (root == null){
            throw new IllegalArgumentException("Tree is empty!!!");
        }
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        int leftHeight;
        int rightHeight;
        if (node == null) return true;

        leftHeight = height(node.left);
        rightHeight = height(node.right);
        return Math.abs(leftHeight - rightHeight) <= 1;
    }

    private int height(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }


}

