import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> {
	// Data members
	private Node head, tail;
	int size;
	// Inner class Node
	private class Node{ 
		E value;
	    Node next;
	    Node (E initialValue){
	    	value = initialValue; 
	    	next = null; 
	    }
	}
	   // Constructor
	public LinkedList() { 	//creating an empty linklist
		head = tail = null; 
		size = 0;
	}
	
	// Adding an item to the list
	public boolean addFirst(E item) {
		Node newNode = new Node(item);
		if(head == null) { // if true, then it's the first add elements
			head = tail = newNode; 
		} 
		else{ // if it is not the first
			newNode.next = head;
			head = newNode;
		}
		size++; 
		return true;
	}
	public boolean addLast(E item) {
		Node newNode = new Node(item);
		if(head == null) { 
			head = tail = newNode; 
		} 
		else { 
			tail.next = newNode; 
			tail = newNode; 
		} 
		size++; return true;
	}
	public boolean add(E item) {
	     return addFirst(item);
	}
	// Retrieving an item from the list
	public E getFirst() { 
		if (head == null)
			throw new NoSuchElementException(); // imported above
		return head.value;
	}
	public E getLast() { 
		if (head == null)
			throw new NoSuchElementException(); 
		return tail.value;
	}
	// Removing an item from the list
	public boolean removeFirst() {
		if (head == null) 
			throw new NoSuchElementException();
		head = head.next;
		if(head == null) 
			tail=null; // elements removed
		size--; 
		return true;
	}
	public boolean removeLast() {
		if (head == null) 
			throw new NoSuchElementException(); 
		if(size == 1) 
			return removeFirst();
		Node current = head;
		Node previous = null;
		while(current.next != null) {
			previous = current; current = current.next;
		}
		previous.next = null; 
		tail = previous; 
		size = size-1; 
		return true;
	}
	// toString() method
	public String toString() { 
		String output = "["; 
		Node current = head; 
		while(current != null) {
			output += current.value + " ";
			current = current.next; 
		}
		output += "]";
	    return output;
	}
	// clear, check if empty, and size of the list
	public void clear() { 
		head = tail = null; 
		size = 0; } 
	public boolean isEmpty() { 
		return (size == 0); 
	} 
	public int size() { 
		return size; 
	}
	// Generating an iterator for the list
	public Iterator<E> iterator(){ // an interface inside a class(inner interface?)
		return new LinkedListIterator();
	}
	private class LinkedListIterator implements Iterator<E>{
		private Node current = head; 
		public boolean hasNext() {
			return (current != null); 
		}
		public E next() { 
			if(current == null)
				throw new NoSuchElementException(); 
			E value = current.value;
			current = current.next; 
			return value;
		}
	}
	public boolean add(int index, E item) { 
		if(index > size || index < 0)
			throw new NoSuchElementException(); 
		if(index == 0) { 
			return addFirst(item); 
		} 
		if(index == size) { 
			return addLast(item); 
		} 
		Node current = head;
		Node previous = null;
		int i = 0;
		while(i < index) {
			previous = current; 
			current = current.next; i++;
		}
		Node newNode = new Node(item); 
		previous.next = newNode; 
		newNode.next = current; 
		size++;
		return true;
	}
	public int searchIterations(E item) {
		int iterations = 0;
		Node current = head;
		while(current != null) {
			iterations++;
			if(current.value.equals(item)) {
				return iterations;
			}
			current = current.next;
		}
		return iterations;
	}
}
