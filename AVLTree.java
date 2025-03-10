public class AVLTree {

    // private nested Node class
    private class Node {
        int value, height;
        Node left, right;

        Node(int value) {
            this.value = value;
            this.height = 1;
        }
    }

    // declaration of root
    private Node root;

    /*
     * This method takes a Node and returns the height data of the node. If the node is null, it returns a 0.
     * @param node, node to be analyzed
     * @return height of the node
     */
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    /*
     * This function gets the balance factor of a node. If the node is null, it returns a 0.
     * @param node, node to be analyzed
     * @return the balance factor of the node
     */
    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    /*
     * This function rotates a Node and its subsequent subtrees to the right.
     * @param node, node to be right rotated
     * @return new root node of the subtree
     */
    private Node rightRotate(Node y) {
        Node x = y.left;   // assign x as y's left node
        Node T2 = x.right;  // assign T2 as x's right node which is y.left.right

        // right rotating code
        x.right = y;  // set x right as the initial node y
        y.left = T2; // set the left of y to y.left.right

        // calculating heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        // returns the new root node right rotated
        return x;
    }

    /*
     * This function rotates a Node and its subsequent subtrees to the left
     * @param node, node to be left rotated
     * @return new root node of the subtree
     */
    private Node leftRotate(Node x) {
        Node y = x.right; // assign y as x's right node
        Node T2 = y.left; // assign T2 as y's left node which is x.right.left

        // left rotating code
        y.left = x; // set y left as the initial node x
        x.right = T2; // set the right of x to x.right.left

        // calculating heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        // returns the new root node left rotated
        return y;
    }

    /*
     * This function serves as a helper method to start the insert
     * @param value to be inserted
     */
    public void insert(int value) {
        root = insertNode(root, value);
    }

    /*
     * This function inserts a value at an existing Node. If there is no node, a new node with the value is inserted there.
     * It uses recursive functions to compare and find its way to the correct spot in the tree.
     */
    private Node insertNode(Node node, int value) {
        if (node == null) return new Node(value);

        // comparison to find spot in tree
        if (value < node.value) node.left = insertNode(node.left, value);
        else if (value > node.value) node.right = insertNode(node.right, value);
        else return node; // <--- means no duplicates

        // update the height
        node.height = Math.max(height(node.left), height(node.right)) + 1;

        // rebalance the tree to make sure insert is correct
        return rebalance(node, value);
    }

    /*
     * This function is a helper method to delete a value in the tree
     * @param value to be deleted
     */
    public void delete(int value) {
        root = deleteNode(root, value);
    }

    /*
     This function is a helper method to delete a value in the tree
    * @param node The current node being checked
    * @param value The value to be deleted
    * @return The new root of the subtree after deletion and rebalancing
     */
    private Node deleteNode(Node node, int value) {
            // Base case: Node not found (empty tree or value doesn't exist)
    if (node == null) return null;

    // Recursive search for the value to delete
    if (value < node.value) {
        node.left = deleteNode(node.left, value); // Search in the left subtree
    } else if (value > node.value) {
        node.right = deleteNode(node.right, value); // Search in the right subtree
    } else {
        // Node with the matching value found

        // Case 1: Node has at most one child
        if (node.left == null || node.right == null) {
            node = (node.left != null) ? node.left : node.right;
        } 
        // Case 2: Node has two children
        else {
            // Find the smallest value in the right subtree (in-order successor)
            Node minNode = getMinValueNode(node.right);

            // Replace the current node's value with the successor's value
            node.value = minNode.value;

            // Delete the successor node from the right subtree
            node.right = deleteNode(node.right, minNode.value);
        }
    }

    // If the tree is now empty, return null
    if (node == null) return null;

    // Update the node's height
    node.height = Math.max(height(node.left), height(node.right)) + 1;

    // Rebalance the tree if needed
    return rebalance(node, value);
    }

    private Node getMinValueNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Rebalancing logic
    private Node rebalance(Node node, int value) {
        int balance = getBalance(node);

        if (balance > 1 && value < node.left.value) return rightRotate(node); // Left Left
        if (balance < -1 && value > node.right.value) return leftRotate(node); // Right Right
        if (balance > 1 && value > node.left.value) {
            node.left = leftRotate(node.left); 
            return rightRotate(node); // Left Right
        }
        if (balance < -1 && value < node.right.value) {
            node.right = rightRotate(node.right); 
            return leftRotate(node); // Right Left
        }

        return node;
    }

    // Serialize method
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        return sb.toString().replaceAll(",$", "");
    }

    private void serializeHelper(Node node, StringBuilder sb) {
        if (node == null) {
            sb.append("X,");
            return;
        }
        sb.append(node.value).append(",");
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }

    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
    
        // Test 1: Insert elements and check balance after each insertion
        System.out.println("Test 1: Insert elements 3, 4, 5, 6");
        tree.insert(3);
        tree.insert(4);
        tree.insert(5);
        tree.insert(6);
        System.out.println("Serialized tree after insertions: " + tree.serialize());
        System.out.println();
    
        // Test 2: Delete a leaf node
        System.out.println("Test 2: Delete leaf node 6");
        tree.delete(6);
        System.out.println("Serialized tree after deleting 6: " + tree.serialize());
        System.out.println();
    
        // Test 3: Delete a node with one child (node 4)
        System.out.println("Test 3: Delete node with one child (4)");
        tree.delete(4);
        System.out.println("Serialized tree after deleting 4: " + tree.serialize());
        System.out.println();
    
        // Test 4: Insert a node that causes a Left-Right rotation
        System.out.println("Test 4: Insert nodes 2, 1 (should trigger a Left-Right rotation)");
        tree.insert(2);
        tree.insert(1);
        System.out.println("Serialized tree after Left-Right rotation: " + tree.serialize());
        System.out.println();
    
        // Test 5: Insert a node that causes a Right-Left rotation
        System.out.println("Test 5: Insert nodes 7, 8 (should trigger a Right-Left rotation)");
        tree.insert(7);
        tree.insert(8);
        System.out.println("Serialized tree after Right-Left rotation: " + tree.serialize());
        System.out.println();
    
        // Test 6: Delete a node with two children (node 3)
        System.out.println("Test 6: Delete node with two children (3)");
        tree.delete(3);
        System.out.println("Serialized tree after deleting 3: " + tree.serialize());
        System.out.println();
    
        // Test 7: Insert a series of nodes and check final structure
        System.out.println("Test 7: Insert nodes 10, 20, 30, 25");
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(25);
        System.out.println("Serialized tree after final insertions: " + tree.serialize());
        System.out.println();
    }
}