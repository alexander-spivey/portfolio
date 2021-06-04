import java.util.Iterator;
import java.util.NoSuchElementException;

// Generic Class LinkedList
public class LinkedList<E> {
	private Node head, tail;
	int size;

	// O(1)
	private class Node {
		E value;
		Node next;

		Node(E initialValue) {
			value = initialValue;
			next = null;
		}
	}

	// O(1)
	public LinkedList() {
		head = tail = null;
		size = 0;
	}

	// O(1)
	public boolean addFirst(E item) {
		Node newNode = new Node(item);
		if (head == null) {
			head = tail = newNode;
		} else {
			newNode.next = head;
			head = newNode;
		}
		size++;
		return true;
	}

	// O(1)
	public boolean addLast(E item) {
		Node newNode = new Node(item);
		if (head == null) {
			head = tail = newNode;
		} else {
			tail.next = newNode;
			tail = newNode;
		}
		size++;
		return true;
	}

	// O(1)
	public E getFirst() {
		if (head == null)
			throw new NoSuchElementException();
		return head.value;
	}

	// O(1)
	public E getLast() {
		if (head == null)
			throw new NoSuchElementException();
		return tail.value;
	}
	
	public E get(int index) {
		if (head == null) 
			throw new NoSuchElementException();
		Node current = head;
		for(int i = 0; i < index; i++) {
			if(current.next != null) {
				current = current.next;
			} else {
				throw new IndexOutOfBoundsException();
			}
		}
		return current.value;
	}
	
	public void set(int index, E item) {
		if (head == null) 
			throw new NoSuchElementException();
		Node current = head;
		for(int i = 0; i < index; i++) {
			if(current.next != null) {
				current = current.next;
			} else {
				throw new IndexOutOfBoundsException();
			}
		}
		current.value = item;
	}

	// O(1)
	public boolean removeFirst() {
		if (head == null)
			throw new NoSuchElementException();
		head = head.next;
		if (head == null)
			tail = null;
		size--;
		return true;
	}

	// O(n)
	public boolean removeLast() {
		if (head == null)
			throw new NoSuchElementException();
		if (size == 1)
			return removeFirst();
		Node current = head;
		Node previous = null;
		while (current.next != null) {
			previous = current;
			current = current.next;
		}
		previous.next = null;
		tail = previous;
		size--;
		return true;
	}

	// O(1)
	public String toString() {
		String output = "";
		Node current = head;
		while (current != null) {
			output += current.value + " ";
			current = current.next;
		}
		return output;
	}

	// O(1)
	public boolean add(E item) {
		return addLast(item);
	}

	// O(1)
	public void clear() {
		head = tail = null;
		size = 0;
	}

	// O(1)
	public boolean isEmpty() {
		return (size == 0);
	}

	// O(1)
	public int size() {
		return size;
	}

	// O(1)
	public Iterator<E> iterator() {
		return new LinkedListIterator();
	}

	private class LinkedListIterator implements Iterator<E> {
		private Node current = head;

		// O(1)
		public boolean hasNext() {
			return (current != null);
		}

		// O(1)
		public E next() {
			if (current == null)
				throw new NoSuchElementException();
			E value = current.value;
			current = current.next;
			return value;
		}
	}

	// O(n)
	public E contains(E item) {
		Node current = head;
		while (current != null) {
			if (current.value.equals(item)) {
				return current.value;
			}
			current = current.next;
		}
		return null;
	}
}
