import java.util.ArrayList;

public class Heap<E extends Comparable<E>> {
	private ArrayList<E> list;

	public Heap() {
		list = new ArrayList<>();
	}

	public Heap(E[] data) {
		list = new ArrayList<>();
		for (int i = 0; i < data.length; i++) {
			Sort.iterations[5]++;
			add(data[i]);
		}
	}
	
	/*public Heap(ArrayList<E> l) {
		list = new ArrayList<>();
		list.add((E) l);
	}*/

	public void add(E item) {
		list.add(item); // append item to the heap
		int currentIndex = list.size() - 1;
		// index of the last element
		while (currentIndex > 0) {
			Sort.iterations[5]++;
			int parentIndex = (currentIndex - 1) / 2;
			//swap if current is greater than its parent
			E current = list.get(currentIndex);
			E parent = list.get(parentIndex);
			if (current.compareTo(parent) > 0) {
				list.set(currentIndex, parent);
				list.set(parentIndex, current);
			} else
				break; // the tree is a heap
			currentIndex = parentIndex;
		}
	}

	public E remove() {
		if (list.size() == 0)
			return null;
		E removedItem = list.get(0); //value of the root
		list.set(0, list.get(list.size() - 1)); //setting root to last node
		list.remove(list.size() - 1);
		int currentIndex = 0;
		while (currentIndex < list.size()) {
			Sort.iterations[5]++;
			int left = 2 * currentIndex + 1;
			int right = 2 * currentIndex + 2;
			// find the maximum of the two children
			if (left >= list.size())
				break;// reached the end
			int maxIndex = left;
			E max = list.get(maxIndex);
			if (right < list.size())
				if (max.compareTo(list.get(right)) < 0)
					maxIndex = right;
			// swap if current is less than the maximum
			E current = list.get(currentIndex);
			max = list.get(maxIndex);
			if (list.get(currentIndex).compareTo(max) < 0) {
				list.set(maxIndex, current);
				list.set(currentIndex, max);
				currentIndex = maxIndex;
			} else
				break; // the tree is a heap
		}
		return removedItem;
	}
	public int size() {
		return list.size();
	}
	public boolean isEmpty() {
		return (list.size() == 0);
	}
	public void clear() {
		list.clear();
	}
}