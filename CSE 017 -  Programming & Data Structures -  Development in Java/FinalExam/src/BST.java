// Generc Class BST
public class BST<E extends Comparable<E>> {
	private TreeNode root;
	private int size;

	private class TreeNode {
		E value;
		TreeNode left;
		TreeNode right;

		TreeNode(E item) {
			value = item;
			left = right = null;
		}
	}

	// O(1)
	BST() {
		root = null;
		size = 0;
	}

	// O(1)
	BST(E[] dataArray) {
		for (int i = 0; i < dataArray.length; i++)
			insert(dataArray[i]);
	}

	// O(1)
	public int size() {
		return size;
	}

	// O(1)
	public boolean isEmpty() {
		return (size == 0);
	}

	// O(1)
	public void clear() {
		root = null;
	}

	// Method search()
	// O(n)
	public E search(E item) {
		TreeNode current = root;
		while (current != null) {
			if (item.compareTo(current.value) < 0)
				current = current.left;
			else if (item.compareTo(current.value) > 0)
				current = current.right;
			else
				return current.value;
		}
		return null;
	}

	// Method insert()
	// O(n)
	public boolean insert(E item) {
		if (root == null)
			root = new TreeNode(item);
		else {
			TreeNode parent, current;
			parent = null;
			current = root;
			while (current != null) {
				parent = current;
				if (item.compareTo(current.value) < 0) {
					current = current.left;
				} else if (item.compareTo(current.value) > 0) {
					current = current.right;
				} else
					return false;
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
	// O(n^2)
	public boolean delete(E item) {
		TreeNode parent, current;
		parent = null;
		current = root;
		while (current != null) {
			parent = current;
			if (item.compareTo(current.value) < 0) {
				current = current.left;
			} else if (item.compareTo(current.value) > 0) {
				current = current.right;
			} else
				break; // item found
		}
		if (current == null)
			return false;
		@SuppressWarnings("unused")
		E value = current.value;
		if (current.left == null) {
			if (parent == null)
				root = current.right;
			else {
				if (item.compareTo(parent.value) < 0)
					parent.left = current.right;
				else
					parent.right = current.right;
			}
		} else {
			TreeNode rightMostParent = current;
			TreeNode rightMost = current.left;
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
		size--;
		return true;
	}

	// Recursive inorder()
	// O(1)
	public void inorder() {
		inorder(root);
	}

	// O(1)
	private void inorder(TreeNode node) {
		if (node != null) {
			inorder(node.left);
			System.out.print(node.value + "  ");
			inorder(node.right);
		}
	}

	// Recursive preorder()
	// O(1)
	public void preorder() {
		preorder(root);
	}

	// O(1)
	private void preorder(TreeNode node) {
		if (node != null) {
			System.out.print(node.value + " ");
			preorder(node.left);
			preorder(node.right);
		}
	}

	// Recursive postorder()
	// O(1)
	public void postorder() {
		postorder(root);
	}

	// O(1)
	private void postorder(TreeNode node) {
		if (node != null) {
			postorder(node.left);
			postorder(node.right);
			System.out.print(node.value + " ");
		}
	}

	// Recursive Method toString()
	public String toString() {
		return toString(root);
	}

	public String toString(TreeNode node) {
		String out = "";
		if (node != null) {
			out += toString(node.left);
			out += node.value;
			out += toString(node.right);
		}
		return out;
	}
}
