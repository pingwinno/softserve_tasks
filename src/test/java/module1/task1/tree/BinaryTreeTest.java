package module1.task1.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BinaryTreeTest {
    static BinaryTree balancedTree = new BinaryTree();
    static BinaryTree notBalancedTree = new BinaryTree();
    static BinaryTree emptyTree = new BinaryTree();

    @BeforeAll
    static void init() {
        balancedTree.root = new Node(1);
        balancedTree.root.left = new Node(2);
        balancedTree.root.right = new Node(3);
        balancedTree.root.right.left = new Node(3);
        balancedTree.root.left.left = new Node(4);
        balancedTree.root.left.right = new Node(5);
        balancedTree.root.left.right.left = new Node(6);

        notBalancedTree.root = new Node(1);
        notBalancedTree.root.left = new Node(2);
        notBalancedTree.root.right = new Node(3);
        notBalancedTree.root.right.left = new Node(3);
        notBalancedTree.root.left.left = new Node(4);
        notBalancedTree.root.left.right = new Node(5);
        notBalancedTree.root.left.right.left = new Node(6);
        notBalancedTree.root.left.right.left.left = new Node(7);
    }

    @Test
    void balancedTreeTest() {
        Assertions.assertTrue(balancedTree.isBalanced());
    }

    @Test
    void notBalancedTreeTest() {
        Assertions.assertFalse(notBalancedTree.isBalanced());
    }

    @Test
    void emptyTreeTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> emptyTree.isBalanced());
    }

}