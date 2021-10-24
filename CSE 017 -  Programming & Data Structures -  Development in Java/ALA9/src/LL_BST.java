import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LL_BST {

	public static void main(String[] args) {
		// Testing linkedlist and arraybasedlist
		LinkedList<String> animalLL = new LinkedList<>();
		BST<String> animalBST = new BST<>();
		ArrayList<String> list = new ArrayList<>();
		File file = new File("animals.txt");
		Scanner readFile = null;
		try {
			readFile = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		while (readFile.hasNext()) {
			String name = readFile.nextLine();
			list.add(name);
		}
		readFile.close();
		//Unsorted List
		for(String name: list) {
			animalLL.add(name);
			animalBST.insert(name);
		}
		
		int Lavg = 0;
        int Aavg = 0;
		Random r = new Random();
		System.out.println("Unsorted list");
		for (int i = 0; i < 10; i++) {
			int rand = r.nextInt(list.size());
			String name = list.get(rand);
			int alist = animalLL.searchIterations(name);
			int blist = animalBST.searchIterations(name);
			System.out.printf("%-30s\t%-20d\t%-20d\n", name, alist, blist);
			Aavg += alist;
            Lavg += blist;
		}
		Aavg = Aavg/10;
        Lavg = Lavg/10;
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-32s%-24d%d\n", "Average # iterations", Aavg, Lavg);
		
        Aavg = 0; Lavg = 0;
		animalLL.clear();
		animalBST.clear();
		java.util.Collections.sort(list);
		for(String name: list) {
			animalLL.add(name);
			animalBST.insert(name);
		}
		System.out.println("\n"+"Sorted list");
		for (int i = 0; i < 10; i++) {
			int rand = r.nextInt(list.size());
			String name = list.get(rand);
			int flist = animalLL.searchIterations(name);
			int glist = animalBST.searchIterations(name);
			System.out.printf("%-30s\t%-20d\t%-20d\n", name, flist, glist);
			Aavg += flist;
            Lavg += glist;
		}
		Aavg = Aavg/10;
        Lavg = Lavg/10;
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-32s%-24d%d\n", "Average # iterations", Aavg, Lavg);
        //animalBST.inorder();
	}
	/* The reason that BST has different values of iteration is due to BST not adding, but inserting the nodes
	 * based on value comparsion. So automatically, by using insert, BST will create a list with the animals
	 * based open each animals value. As the program inserts more animals, it will check to see if the value is
	 * less than or greater than the current position, and move appropriatly in the right direction to keep values
	 * in a linear fashion. Left being the lowest value, and right being the higher
	 */
}
