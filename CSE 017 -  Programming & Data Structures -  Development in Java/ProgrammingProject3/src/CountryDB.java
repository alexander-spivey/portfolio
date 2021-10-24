/**
 * 11/23/2020 Programming Project 3
 * @author Alexander Spivey
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CountryDB {
	// Initializing static arrays to track iterations of search, delete, and insert
	static int[][] performance_insert = new int[2][228];
	static int[][] performance_search = new int[2][10];
	static int[][] performance_delete = new int[2][10];

	// Main class
	public static void main(String[] args) {
		// Create Arraylist of Country
		ArrayList<Country> countryList = new ArrayList<>(228);
		File file = new File("countries.txt");
		Random rand = new Random();
		Scanner readFile = null;
		try {
			readFile = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			System.exit(0);
		}
		// Split and insert string into Country
		while (readFile.hasNext()) {
			String foo = readFile.nextLine();
			String[] split = foo.split("\\|");
			String c = split[0]; 
			String n = split[1]; 
			Double a = Double.parseDouble(split[2]);
			Country temp = new Country(c, n, a);
			countryList.add(temp);
		}

		// Instantiate the class BST for the type Country and name the class instance
		// CountryBST
		BST<Country> countryBST = new BST<>(countryList);
		for (int i = 0; i < countryList.size(); i++) {
			performance_insert[0][i] = countryBST.insert(countryList.get(i));
			// inserts list.get(i) into BST and saving iterations into insert
		}
		print(countryBST);

		// Choosing random countries and saving to static lists
		for (int i = 0; i < 10; i++) {
			int r = rand.nextInt(countryList.size() - 1);
			performance_search[0][i] = countryBST.search(countryList.get(r));
			performance_delete[0][i] = countryBST.delete(countryList.get(r));
		}

		// Shuffling and resetting
		java.util.Collections.shuffle(countryList);
		countryBST.clear();
		for (int i = 0; i < countryList.size(); i++) {
			performance_insert[1][i] = countryBST.insert(countryList.get(i));
		}
		print(countryBST);

		// Choosing random countries and saving to static lists
		for (int i = 0; i < 10; i++) {
			int r = rand.nextInt(countryList.size() - 1);
			performance_search[1][i] = countryBST.search(countryList.get(r));
			performance_delete[1][i] = countryBST.delete(countryList.get(r));
		}

		// Printing static lists
		System.out.printf("%-50s%s\n", "Sorted List", "Shuffled List");
		System.out.printf("%-15s%-15s%-20s%-15s%-15s%s\n", "Insert", "Search", "Delete", "Insert", "Search", "Delete");
		for (int i = 0; i < 10; i++) {
			int r = rand.nextInt(countryList.size() - 1);
			String output = String.format("%-15d%-15d%-20d%-15d%-15d%d\n", performance_insert[0][r],
					performance_search[0][i], performance_delete[0][i], performance_insert[1][r],
					performance_search[1][i], performance_delete[1][i]);
			System.out.print(output);
		}
	}
	/*
	 * Question L: The reason the countries were taking so long when it was first
	 * inserted in order, is that all the nodes were stacked on one side, making it
	 * highly ineffecient. By shuffling it, the tree was created more equally and
	 * made more effeceint, which lowered the number of iterations by comparison.
	 * Question M: Insert displayed the number of iterations to insert the node
	 * Search and delete returned the iterations to find a node, so they should
	 * always spit out the same number of iterations.
	 */

	/**
	 * Print statement for type BST
	 * 
	 * @param countryBST: list that is being printed
	 */
	private static void print(@SuppressWarnings("rawtypes") BST countryBST) {
		System.out.println("Sorted Country List");
		System.out.printf("%-10s%-50s%s\n", "Code", "Name", "Area");
		System.out.print(" ");
		countryBST.inorder();
		System.out.println("");
		System.out.println("# of LeafNodes: " + countryBST.leafNodes());
		System.out.println("Height: " + countryBST.height());
		System.out.println("Balanced: " + countryBST.isBalanced());
		System.out.println("");
	}
}