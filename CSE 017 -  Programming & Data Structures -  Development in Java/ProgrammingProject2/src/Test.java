import java.io.*;
import java.util.*;

public class Test {
	public static void main(String[] args) {
		ArrayList<Country> countryList = new ArrayList<>(200);
		Scanner input = new Scanner(System.in);
		File file = new File("countries.txt");
		Scanner readFile = null;
		try {
			readFile = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			System.exit(0);
		}

		//copying files in
		String name = "", code = "";
		double area = 0;
		while (readFile.hasNext()) {
			String combined = readFile.nextLine();
			// System.out.println(combined);
			combined = combined.replaceAll("[^a-zA-Z0-9()\\s,'-.]", ":");
			// System.out.println(combined);
			String[] sC = combined.split(":");
			
			code = sC[0];
			// System.out.print(sC[0] + " ");
			name = sC[1];
			// System.out.print(sC[1] + " ");
			area = Double.parseDouble(sC[2]);
			// System.out.print(sC[2] + " ");
			// System.out.println();
			Country temp = new Country(code, name, area);
			countryList.add(temp);
		}
		readFile.close();
		java.util.Collections.shuffle(countryList);
		
		//user interface
		System.out.println("Please enter your new country's name: ");
		String in = input.nextLine();
		Country temp = new Country("N/A", in, 0.0);
		input.close();
		
		//search
		int index = search(countryList, temp, 0);
		if (index != -1) {
			Country fuck = countryList.get(index);
			System.out.println("Found Country - Code:" + fuck.getCode() + " Name:" + fuck.getName() + " Area:" + fuck.getArea());
		} else {
			System.out.println("ERROR, Country " + in + " was not found");
		}
		print(countryList);
		
		//sort
		System.out.println();
		System.out.println("Sorted version: ");
		sort(countryList);
		print(countryList);
	}

	// time complexity of search: O(n)
	public static <E> int search(ArrayList<E> list, E key, int val) {
        if(val < list.size()) {
        	Country current = (Country) list.get(val);
        	if (current.equals(key)) {
        		return val;
        	} else {
                return search(list, key, ++val);
            }
        }
        return -1;
    }

	//time complexity of sort: O(n^2)
	@SuppressWarnings("unchecked")
	public static <E extends Comparable<E>> void sort(ArrayList<E> list) {
		for (int i = 0; i < list.size(); i++) {
			Country currentValue = (Country) list.get(i);
			int j = i;
			while (j > 0 && currentValue.getArea() < ((Country) list.get(j - 1)).getArea()) {
				list.set(j, list.get(j - 1));
				j--;
			}
			list.set(j, (E) currentValue);
		}
	}

	public static void print(ArrayList<Country> countryList) {
		System.out.printf("%-10s%-55s%s\n", "Code", "Country Name", "Country Area");
		for (int i = 0; i < countryList.size(); i++) {
			Country fuck = countryList.get(i);
			System.out.println(fuck.toString());
		}
	}

}
