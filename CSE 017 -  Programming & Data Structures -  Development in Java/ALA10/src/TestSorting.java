import java.util.*;

public class TestSorting {
	public static final int SIZE = 10000;
	public static void main(String[] args) {
		Integer[] list = new Integer[SIZE];
		Random r = new Random();
		for(int i = 0; i < list.length; i++) {
			list[i] = r.nextInt(99999) + 1;
		}
		Sort.selectionSort(list);
		shuffle(list);
		Sort.insertionSort(list);
		shuffle(list);
		Sort.bubbleSort(list);
		shuffle(list);
		Sort.mergeSort(list);
		shuffle(list);
		Sort.quickSort(list);
		shuffle(list);
		Sort.heapSort(list);
		String[] al = {"Selection", "Insertion" , "Bubble", "Merge", "Quick", "Heap"};
		for(int i=0; i < Sort.iterations.length; i++) {
			System.out.printf("%-30s\t%-20s\n", al[i], Sort.iterations[i]);
		}
	}
	public static <E extends Comparable<E>> void shuffle(E[] list) {
		Random r = new Random();
		for(int i = 0; i < list.length; i++) {
			int rIndex = r.nextInt(list.length);
			Sort.swap(list, rIndex, i);
		}
	}
}
