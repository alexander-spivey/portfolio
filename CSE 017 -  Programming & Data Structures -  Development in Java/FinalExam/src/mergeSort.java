public class mergeSort{
	public static <E extends Comparable<E>> void mergesort(LinkedList<E> list) {
		if (list.size() > 1) { // base case: size = 1
		LinkedList<E> firstHalf = new LinkedList<>();
		LinkedList<E> secondHalf = new LinkedList<>();
		splitList(list, firstHalf, secondHalf);
		mergesort(firstHalf);
		mergesort(secondHalf);
		merge(firstHalf, secondHalf, list);
	  }
	}
	public static <E> void splitList(LinkedList<E> list, LinkedList<E> list1, LinkedList<E> list2) {
		for(int i=0; i<list.size()/2; i++) {
			list1.add(list.get(i));
		}
		for(int i=list.size()/2; i<list.size(); i++) {
			list2.add(list.get(i));
		}
	}
	public static <E extends Comparable<E>> void merge(LinkedList<E> list1,LinkedList<E> list2,LinkedList<E> list) {
		int list1Index = 0;
		int list2Index = 0;
		int listIndex = 0;
		while( list1Index < list1.size() && list2Index < list2.size()) {
			if (list1.get(list1Index).compareTo(list2.get(list2Index)) < 0) {
				list.set(listIndex++, list1.get(list1Index++));
			}
			else
				list.set(listIndex++, list2.get(list2Index++));
		}
		while(list1Index < list1.size())
			list.set(listIndex++,list1.get(list1Index++));
		while(list2Index < list2.size())
			list.set(listIndex++, list2.get(list2Index++));
	}
}
