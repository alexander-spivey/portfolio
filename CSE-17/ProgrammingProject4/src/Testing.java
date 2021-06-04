/**
 * 12/6/2020 Programming Project 4
 * @author Alexander Spivey
 * This project was worked on with Tyler Hagmann
 */
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Testing {
	public static final int SIZE = 10000;
	public static void main(String[] args) {
		ArrayList<Integer> randomList = new ArrayList<Integer>(SIZE);
		ArrayList<Integer> sortedList = new ArrayList<Integer>(SIZE);
		ArrayList<Integer> reversedList = new ArrayList<Integer>(SIZE);
		//randomList
		Random r = new Random();
		for (int i = 0; i < 10000; i++) {
            randomList.add(i, r.nextInt(99999) + 1);
        }
        java.util.Collections.shuffle(randomList);
        //sortedList
        for (int i = 0; i < 10000; i++) {
            sortedList.add(i, randomList.get(i));
        }
        //reversedList
        java.util.Collections.sort(sortedList);
        for (int i = 0; i < 10000; i++) {
            reversedList.add(i, sortedList.get(i));
        }
		java.util.Collections.reverse((reversedList));
		
		//randomList
		Sort.selectionSort(randomList);
		java.util.Collections.shuffle(randomList);
		Sort.insertionSort(randomList);
		java.util.Collections.shuffle((randomList));
		Sort.bubbleSort(randomList);
		java.util.Collections.shuffle((randomList));
		Sort.mergeSort(randomList);
		java.util.Collections.shuffle((randomList));
		Sort.quickSort(randomList);
		java.util.Collections.shuffle((randomList));
		Sort.heapSort(randomList);
		java.util.Collections.shuffle((randomList));
		int[] rITR = new int[6];
		for(int i = 0; i < rITR.length; i++) {
			rITR[i] = Sort.iterations[i];
			Sort.iterations[i] = 0;
		}
		
		//sortedList
		Sort.selectionSort(sortedList);
		java.util.Collections.sort((sortedList));
		Sort.insertionSort(sortedList);
		java.util.Collections.sort((sortedList));
		Sort.bubbleSort(sortedList);
		java.util.Collections.sort((sortedList));
		Sort.mergeSort(sortedList);
		java.util.Collections.sort((sortedList));
		Sort.quickSort(sortedList);
		java.util.Collections.sort((sortedList));
		Sort.heapSort(sortedList);
		java.util.Collections.sort((sortedList));
		int[] sITR = new int[6];
		for(int i = 0; i < sITR.length; i++) {
			sITR[i] = Sort.iterations[i];
			Sort.iterations[i] = 0;
		}
		
		
		//reversedList
		Sort.selectionSort(reversedList);
		java.util.Collections.reverse((reversedList));
		Sort.insertionSort(reversedList);
		java.util.Collections.reverse((reversedList));
		Sort.bubbleSort(reversedList);
		java.util.Collections.reverse((reversedList));
		Sort.mergeSort(reversedList);
		java.util.Collections.reverse((reversedList));
		Sort.quickSort(sortedList);
		java.util.Collections.reverse((reversedList));
		Sort.heapSort(reversedList);
		java.util.Collections.reverse((reversedList));
		int[] revITR = new int[6];
		for(int i = 0; i < revITR.length; i++) {
			revITR[i] = Sort.iterations[i];
			Sort.iterations[i] = 0;
		}
		
		//Printing
		System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\n", "Sorting Algorithm", "Random List", "Sorted List", "Reversed List");
		System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\n", "Selection Sort", rITR[0], sITR[0], revITR[0]);
		System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\n", "Insertion Sort", rITR[1], sITR[1], revITR[1]);
		System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\n", "Bubble Sort", rITR[2], sITR[2], revITR[2]);
		System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\n", "Merge Sort", rITR[3], sITR[3], revITR[3]);
		System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\n", "Quick Sort", rITR[4], sITR[4], revITR[4]);
		System.out.printf("%-20s\t%-20s\t%-20s\t%-20s\n", "Heap Sort ", rITR[5], sITR[5], revITR[5]);
	}
	
	/*
	 * Discussion of results:
	 * So with selection sort, it made sense thaty all lists hit 50014998 iterations, since the sort with randomList
	 * has to fix each one directly, sortedList had to check each one, and reversedList had to change each one as well.
	 * With insertion, it made since that Random List took less iterations, faster sort method, and the already sortedList
	 * only took 9999 iterations (just checking if everythign was right), and reverseList took 50014998 because each one
	 * had to be moved again like selection sort. Bubble sort was barely any better than selection sort for both random
	 * reversed, since those two would have to essentially move almost every value. Since sorted was already sorted, it had
	 * exactly the same iterations as the size. Merge sort was the most effective, with everyone getting 153615. Quick sort
	 * ran super well for an already randomized list, but functioned horribly for any list that was already sorted
	 * Heap sort also worked pretty well, a close second to merge, which makes sense since Heap uses binary tree
	 * to move the leaves and branches to eventually get everything sorted.
	 * 
	 * In the end, merge sort did the best, heap sort did 2nd, and if the list isn't sorted, quick sort got 3rd, insertion
	 * sort got 4th, bubble sort got 5th, and selection sort was the worst at 6th. The best one to choose (due to lack of code)
	 * and overall iterations) based on my opinion would be heap sort. 
	 */
}
