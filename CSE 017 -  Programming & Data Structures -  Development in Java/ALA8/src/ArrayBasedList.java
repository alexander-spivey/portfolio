import java.util.Iterator;

public class ArrayBasedList<E> {
	// data members
	private E[] elements;
	private int size;

	// Constructors
	public ArrayBasedList() {
		elements = (E[]) new Object[10];
		size = 0;
	}

	public ArrayBasedList(int capacity) {
		elements = (E[]) new Object[capacity];
		size = 0;
	}

	// Adding an item to the list (2 methods)
	public boolean add(int index, E item) {
		if (index > size || index < 0)
			throw new ArrayIndexOutOfBoundsException();
			ensureCapacity();
		for (int i = index; i < size - 1; i++)
			elements[i + 1] = elements[i];
		elements[index] = item;
		size++;
		return true;
	}

	public boolean add(E item) {
		return add(size, item);
	}

	// Getter and Setter
	public E get(int index) {
		checkIndex(index);
		return elements[index];
	}

	public E set(int index, E item) {
		checkIndex(index);
		E oldItem = elements[index];
		elements[index] = item;
		return oldItem;
	}

	// Size of the list
	public int size() {
		return size;
	}

	// Removing an item from the list
	public boolean remove(int index) {
		checkIndex(index);
		for (int i = index; i < size - 1; i++)
			elements[i] = elements[i + 1];
		size--;
		return true;
	}

	// Clear the list
	public void clear() {
		size = 0;
	}

	// Check if the list is empty
	public boolean isEmpty() {
		return (size == 0);
	}

	// Shrink the list to size
	public void trimToSize() {
		if (size != elements.length) {
			E[] newElements = (E[]) new Object[size];
			for (int i = 0; i < size; i++)
				newElements[i] = elements[i];
			elements = newElements;
		}
	}

	// Grow the list if needed
	private void ensureCapacity() {
		if (size >= elements.length) {
			int newSize = (int) (elements.length * 1.5);
			E[] newElements = (E[]) new Object[newSize];
			for (int i = 0; i < size; i++)
				newElements[i] = elements[i];
			elements = newElements;
		}
	}

	// Check if the index is valid
	private void checkIndex(int index) {
		if (index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index out of bounds. Must be between 0 and " + (size - 1));
	}

	// toString() method
	public String toString() {
		String output = "[";
		for (int i = 0; i < size - 1; i++)
			output += elements[i] + " ";
		output += elements[size - 1] + "]";
		return output;
	}

	//Iterator
	public Iterator<E> iterator() {
		return new ArrayIterator();
	}

	private class ArrayIterator implements Iterator<E> {
		private int current = -1;

		public boolean hasNext() {
			return current < size - 1;
		}

		public E next() {
			return elements[++current];
		}
	}
	
	//O(n)
	public int searchIterations(E item) {
		int iterations = 0;
		for(int i = 0; i < size; i++) {
			iterations++;
			if(elements[i].equals(item)) {
				return iterations;
			}
		}
		return iterations;
	}
}