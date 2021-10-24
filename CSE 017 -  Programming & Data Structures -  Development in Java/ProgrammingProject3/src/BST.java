import java.util.ArrayList;

public class BST<E extends Comparable<E>> {
	// data memebers
	private TreeNode root;
	private int size;

	// Private class tree node
	private class TreeNode {
		E value;
		TreeNode left;
		TreeNode right;

		TreeNode(E val) {
			value = val;
			left = right = null;
		}
	}

	// Constructor
	BST() { // O(1)
		root = null;
		size = 0;
	}

	BST(ArrayList<E> list) { // O(n)
		for (int i = 0; i < list.size(); i++) {
			insert(list.get(i));
		}
	}

	/**
	 * Returns size of list
	 * 
	 * @return size: how big list is
	 */
	public int size() { // O(1)
		return size;
	}

	/**
	 * Check is list isEmpty or not
	 * 
	 * @return statment if size == 0
	 */
	public boolean isEmpty() { // O(1)
		return (size == 0);
	}

	/**
	 * Resets list and set size = 0 and root = null;
	 */
	public void clear() { // O(1)
		root = null;
		size = 0;
	}

	/**
	 * Search with iterations
	 * 
	 * @param item: item we looking for
	 * @return iteration: how many iterations it took to find item
	 */
	public int search(E item) { // O(log n)
		int iteration = 0;
		TreeNode current = root;
		while (current != null) {
			iteration++;
			if (item.compareTo(current.value) < 0)
				current = current.left;
			else if (item.compareTo(current.value) > 0)
				current = current.right;
			else // equals
				return iteration;
		}
		return iteration;
	}

	/**
	 * Insert with iterations
	 * 
	 * @param item: item we looking for
	 * @return iteration: how many iterations it took to insert item
	 */
	public int insert(E item) { // O(log n)
		int iterations = 0;
		if (root == null) { // first node to be inserted
			iterations++;
			root = new TreeNode(item);
		} else {
			TreeNode parent, current;
			parent = null;
			current = root;
			while (current != null) {// Looking for a leaf node
				iterations++;
				parent = current;
				if (item.compareTo(current.value) < 0) {
					current = current.left;
				} else if (item.compareTo(current.value) > 0) {
					current = current.right;
				} else
					return iterations; // duplicates are not allowed
			}
			if (item.compareTo(parent.value) < 0)
				parent.left = new TreeNode(item);
			else
				parent.right = new TreeNode(item);
		}
		size++;
		return iterations;
	}

	/**
	 * Delete with iterations
	 * 
	 * @param item: item we looking for
	 * @return iteration: how many iterations it took to find and delete item
	 */
	public int delete(E item) { // O(log n)
		int iterations = 0;
		TreeNode parent, current;
		parent = null;
		current = root;
		// Find item first
		while (current != null) {
			parent = current;
			if (item.compareTo(current.value) < 0) {
				current = current.left;
			} else if (item.compareTo(current.value) > 0) {
				current = current.right;
			} else
				break; // item found
			iterations++;
		}
		if (current == null) // item not in the tree
			return iterations;
		// Case 1: node found and has no left child
		if (current.left == null) { // No left child
			if (parent == null) // delete root
				root = current.right;
			else {// modify link from parent to current’s child
				if (item.compareTo(parent.value) < 0)
					parent.left = current.right;
				else
					parent.right = current.right;
			}
		} else { // Case 2: current has a left child
			TreeNode rightMostParent = current;
			TreeNode rightMost = current.left;
			// going right on left subtree
			while (rightMost.right != null) {
				rightMostParent = rightMost;
				rightMost = rightMost.right;
			}
			current.value = rightMost.value;
			// delete rigthMost node
			if (rightMostParent.right == rightMost)
				rightMostParent.right = rightMost.left;
			else
				rightMostParent.left = rightMost.left;
		}
		iterations++;
		size--;
		return iterations;
	}

	// Step method recursion
	public int leafNodes() { // O(2^n)
		return leafNodes(root);
	}

	/**
	 * Check to see if there are leaves
	 * 
	 * @param node: reference to node
	 * @return true it leaves, 0 if no node, and 1 if no leaves
	 */
	public int leafNodes(TreeNode node) {// O(2^n)
		if (node == null) { // if no node
			return 0;
		}
		if (node.left == null && node.right == null) { // if no leaves
			return 1;
		} else {
			return leafNodes(node.left) + leafNodes(node.right); // if leaves
		}
	}

	// Step method recursion
	public int height() { // O(2^n)
		return height(root);
	}

	/**
	 * Height of BST
	 * 
	 * @param node: reference to node
	 * @return height: height of tree
	 */
	public int height(TreeNode node) { // O(2^n)
		if (node == null) {
			return 0;
		}

		return 1 + (Math.max(height(node.left), height(node.right)));
	}

	// Step method recursion
	public boolean isBalanced() { // O(4^n)
		return isBalanced(root);
	}

	/**
	 * Checks to see if both side have same height
	 * 
	 * @param node: reference to node
	 * @return true if balanced, false if not
	 */
	public boolean isBalanced(TreeNode node) { // O(4^n)
		if (node == null) {
			return true;
		}
		int left, right;
		left = height(node.left); // get left height
		right = height(node.right); // get right height
		if (Math.abs(left - right) <= 1 && isBalanced(node.left) && isBalanced(node.right)) {
			return true; // if all nodes balanced
		} else {
			return false;
		}
	}

	// Step method recursion
	public void inorder() { // O(2^n)
		inorder(root);
	}
	//print in order
	private void inorder(TreeNode node) { // O(2^n)
		if (node != null) {
			inorder(node.left);
			System.out.print(node.value + " ");
			inorder(node.right);
		}
	}

	// Step method recursion
	public void preorder() { // O(2^n)
		preorder(root);
	}
	//print in preorder
	private void preorder(TreeNode node) { // O(2^n)
		if (node != null) {
			System.out.print(node.value + " ");
			preorder(node.left);
			preorder(node.right);
		}
	}

	// Step method recursion
	public void postorder() { // O(2^n)
		postorder(root);
	}
	//print in postorder
	private void postorder(TreeNode node) { // O(2^n)
		if (node != null) {
			postorder(node.left);
			postorder(node.right);
			System.out.print(node.value + " ");
		}
	}
}
