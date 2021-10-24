public class BST<E extends Comparable<E>> {
	// data memebers
	private TreeNode root;
	private int size;

	// inner class we are going to use inside BST
	private class TreeNode {
		E value;
		TreeNode left; // left side of the node (pointer)
		TreeNode right; // right side of the node (pointer)

		TreeNode(E val) {
			value = val;
			left = right = null;
		}
	}

	// the constructors:
	BST() {
		root = null;
		size = 0;
	}

	BST(E[] dataArray) {
		for (int i = 0; i < dataArray.length; i++)
			insert(dataArray[i]);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return (size == 0);
	}

	public void clear() {
		root = null;
		size = 0;
	}

	// Method search()
	public boolean search(E item) {
		TreeNode current = root;
		while (current != null) {
			if (item.compareTo(current.value) < 0)
				current = current.left;
			else if (item.compareTo(current.value) > 0)
				current = current.right;
			else // equals
				return true;
		}
		return false;
	}

	// Method insert()
	public boolean insert(E item) {
		if (root == null) // first node to be inserted
			root = new TreeNode(item);
		else {
			TreeNode parent, current;
			parent = null;
			current = root;
			while (current != null) {// Looking for a leaf node
				parent = current;
				if (item.compareTo(current.value) < 0) { // item is smaller than the current value(for the first time
															// smaller than the root value)
					current = current.left;
				} else if (item.compareTo(current.value) > 0) {
					current = current.right;
				} else
					return false; // duplicates are not allowed
			}
			if (item.compareTo(parent.value) < 0)
				parent.left = new TreeNode(item);
			else
				parent.right = new TreeNode(item);
		}
		size++;
		return true;
	}

	// Method delete()
	public boolean delete(E item) {
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
		}
		if (current == null) // item not in the tree
			return false;
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
			current.value = rightMost.value; // delete rigthMost node
			if (rightMostParent.right == rightMost)
				rightMostParent.right = rightMost.left;
			else
				rightMostParent.left = rightMost.left;
		}
		size--;
		return true;
	}

	// Recursive method inorder()
	public void inorder() {
		inorder(root);
	}

	private void inorder(TreeNode node) {
		if (node != null) {
			inorder(node.left);
			System.out.print(node.value + " ");
			inorder(node.right);
		}
	}

	// Recursive method postorder()
	public void postorder() {
		postorder(root);
	}

	private void postorder(TreeNode node) {
		if (node != null) {
			postorder(node.left);
			postorder(node.right);
			System.out.print(node.value + " ");
		}
	}

	// the search iteration method
	// Time Compexlity : O(logn)
	public int searchIteration(E item) {
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
}