import java.io.*;
import java.util.*;

public class Catalog extends Title{
	Title[] titles;
	private int count;

	// Constructor
	Catalog() {
		titles = new Title[1000];
		count = 0;
	}

	// Main methods
	/**
	 * adds files into Title[] titles
	 * @param filename: loads file name;
	 */
	public void loadFromFile(String filename) {
        File file = new File(filename);
        Scanner readFile = null;
        try {
            readFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error, file doesnt exist");
            System.exit(0);
        }
        while(readFile.hasNext()){
            String cN = readFile.nextLine();
            //System.out.println(cN + "cN");
            String t = readFile.nextLine();
            //System.out.println(t + "t");
            String p = readFile.nextLine();
            //System.out.println(p+ "p");
            int y = readFile.nextInt();
            //System.out.println(y+ "y");
            int c = readFile.nextInt();
            //System.out.println(c+ "c");
            if(cN.charAt(0) == 'P') {
            	int i = readFile.nextInt();
            	//System.out.println(i+ "i");
            	int m = readFile.nextInt();
            	//System.out.println(m+ "m");
            	@SuppressWarnings("unused")
				String hoo = readFile.nextLine();
                titles[count] = new Periodical(cN,t,p,y,c,i,m);
            }
            else {
            	@SuppressWarnings("unused")
				String foo = readFile.nextLine();
            	String a = readFile.nextLine();
            	//System.out.println(a+ "a");
            	String isbn = readFile.nextLine();
            	//System.out.println(isbn+ "isbn");
                titles[count] = new Book(cN,t,p,y,c,a,isbn);
            }
            count++;
        }
        readFile.close();
    }

	/**
	 * saves the modified program infromation into titles.txt
	 * @param filename: name of file
	 */
	public void saveToFile(String filename) {
		File file = new File("titles.txt");
		PrintWriter write = null;
		try {
			write = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error, file not found");
		}
		for (int i = 0; i < 0; i++) {
			write.println(titles[i].toString());
		}
		write.close();
	}

	/**
	 * Adds a new title to index count
	 * @param t: Title(etc...)
	 */
	public void addItem(Title t) {
		titles[count] = t;
		count++;
	}

	/**
	 * Removes index if CN is found
	 * @param cN: Catalog Number of book
	 */
	public void removeItem(String cN) {
		for (int i = 0; i < count; i++) {
			if (cN.equals(titles[i].getCallNumber())) {
				for (int j = i; j < count; j++) {
					titles[j] = titles[j + 1];
				}
				titles[count] = new Title();
				count--;
			}
		}
	}

	/**
	 * Searches for title and copies all found ones into a new Title temp[]
	 * @param t: name of book we searching for
	 * @return temp = an array of Title with all books with the same name
	 */
	public Title[] searchTitle(String t) {
		Title[] temp = new Title[100];
		int foo = -1;
		for (int i = 0; i < count; i++) {
			if (titles[i].getTitle().equals(t)) {
				foo++;
				temp[foo] = titles[i];
			}
		}
		return temp;
	}

	/**
	 * looks through titles array for book with call number
	 * @param cN: call number being searched for
	 * @return titles[i]: titles index object stuff...
	 */
	public Title searchCallNumber(String cN) {
		for (int i = 0; i < count; i++) {
			if (titles[i].getCallNumber().equals(cN)) {
				return titles[i];
			}
		}
		return null;
	}

	/**
	 * Searches for year and copies all found ones into a new Title temp[]
	 * @param y: name of book we searching for
	 * @return temp = an array of Title with all books with the same y
	 */
	public Title[] searchYear(int y) {
		Title[] temp = new Title[100];
		int foo = -1;
		for (int i = 0; i < count; i++) {
			if (titles[i].getYear() == y) {
				foo++;
				temp[foo] = titles[i];
			}
		}
		return temp;
	}

	/**
	 * Prints out all items inside titles
	 */
	public void viewAll() {
		for (int i = 0; i < count; i++) {
			System.out.println((i + 1) + ".\t" + titles[i].getCallNumber() + "\t" + titles[i].getTitle() + "\t"
					+ titles[i].getPublisher() + "\t" + titles[i].getYear());
		}
	}
}
