import java.util.ArrayList;

public class Sort {
	public static int[] iterations = new int[6];

	public static <E> void swap(ArrayList<E> list, int i, int j) {
		E temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
	}

	public static <E extends Comparable<E>> void selectionSort(ArrayList<E> list) {
		int minIndex;
		for (int i = 0; i < list.size() - 1; i++) {
			iterations[0]++;
			E min = list.get(i);
			minIndex = i;
			for (int j = i; j < list.size(); j++) {
				iterations[0]++;
				if (list.get(j).compareTo(min) < 0) {
					min = list.get(j);
					minIndex = i;
				}
			}
			E temp = list.get(i);
			list.set(i, list.get(minIndex));
			list.set(minIndex, temp);
		}
	}

	public static <E extends Comparable<E>> void insertionSort(ArrayList<E> list) {
		for (int i = 1; i < list.size(); i++) {
			iterations[1]++;
			// Insert element i in the sorted sub-list
			E currentVal = list.get(i);
			int j = i;
			while (j > 0 && currentVal.compareTo(list.get(i-1)) < 0) {
				iterations[1]++;
				// Shift element (j-1) into element (j)
				list.set(j, list.get(j-1));
				j--;
			}
			// Insert currentVal at position i
			list.set(j, currentVal);
		}
	}

	public static <E extends Comparable<E>> void bubbleSort(ArrayList<E> list) {
		boolean sorted = false;
		for (int k = 1; k < list.size() && !sorted; k++) {
			sorted = true;
			iterations[2]++;
			for (int i = 0; i < list.size() - k; i++) {
				iterations[2]++;
				if (list.get(i).compareTo(list.get(i+1)) > 0) {
					// swap
					swap(list, i, i + 1);
					sorted = false;
				}
			}
		}
	}

	public static <E extends Comparable<E>> void mergeSort(ArrayList<E> list) {
		iterations[3]++;
		if (list.size() > 1) { // base case
			ArrayList<E>  firstHalf = subList(list, 0, list.size()/2);
			ArrayList<E> secondHalf = subList(list, list.size()/2, list.size()); // from index size/2 to last index
			mergeSort(firstHalf);
			mergeSort(secondHalf);
			merge(firstHalf, secondHalf, list);
		}
	}

	public static <E> ArrayList<E> subList(ArrayList<E> list, int start, int end) {
		ArrayList<E> list2 = new ArrayList<E>(list.size());
		for(int i = start; i < end; i++) {
			list2.add(list.get(i));
		}
		return list2;
	}

	public static <E extends Comparable<E>> void merge(ArrayList<E> firstHalf, ArrayList<E> secondHalf, ArrayList<E> list) {
		int list1Index = 0;
		int list2Index = 0;
		int listIndex = 0;
		while (list1Index < firstHalf.size() && list2Index < secondHalf.size()) {
			iterations[3]++;
			if (firstHalf.get(list1Index).compareTo(secondHalf.get(list2Index)) < 0)
				list.set(listIndex++, firstHalf.get(list1Index++));
			else
				list.set(listIndex++, secondHalf.get(list2Index++));
		}
		while (list1Index < firstHalf.size()) {
			iterations[3]++;
			list.set(listIndex++, firstHalf.get(list1Index++));
		}
		while (list2Index < secondHalf.size()) {
			iterations[3]++;
			list.set(listIndex++, secondHalf.get(list2Index++));
		}
	}

	public static <E extends Comparable<E>> void quickSort(ArrayList<E> list) {
		quickSort(list, 0, list.size() - 1);
	}

	public static <E extends Comparable<E>> void quickSort(ArrayList<E> list, int first, int last) {
		iterations[4]++;
		if (last > first) {
			int pivotIndex = partition(list, first, last);
			quickSort(list, first, pivotIndex - 1);
			quickSort(list, pivotIndex + 1, last);
		}
	}

	public static <E extends Comparable<E>> int partition(ArrayList<E> list, int first, int last) {
		E pivot;
		int index, pivotIndex;
		pivot = list.get(first);// pivot is the first element
		pivotIndex = first;
		for (index = first + 1; index <= last; index++) {
			iterations[4]++;
			if (list.get(index).compareTo(pivot) < 0) {
				pivotIndex++;
				swap(list, pivotIndex, index);
			}
		}
		swap(list, first, pivotIndex);
		return pivotIndex;
	}

	public static <E extends Comparable<E>> void heapSort(ArrayList<E> list) {
		Heap<E> heap = new Heap<>(list);// add()
		for (int i = list.size() - 1; i >= 0; i--) {
			iterations[5]++;
			list.set(i, heap.remove());
		}
	}
}
