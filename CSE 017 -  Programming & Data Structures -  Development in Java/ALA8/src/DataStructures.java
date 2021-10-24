import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataStructures {

	public static void main(String[] args) {
		Stack<Integer> postfixStack = new Stack<>();
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		boolean loop = true;
		while(loop) {
			System.out.println("Please enter a postfix expression: ");
			String pfinput = input.nextLine();
			String[] pfSplit = pfinput.split(" ");
			for (int i = 0; i < pfSplit.length; i++) {
				if(pfSplit[i].length() > 1) {
					postfixStack.push(Integer.parseInt(pfSplit[i]));
				} else {
					char x = pfSplit[i].charAt(0);
					 if(Character.isDigit(x)) { //if it a number
						 postfixStack.push(x - '0'); //convert to number
						 continue; //reset to start
					}
					int v1 = postfixStack.pop();
					int v2 = postfixStack.pop();
					switch (x) {
					case '+':
						postfixStack.push(v2 + v1);
						break;
					case '-':
						postfixStack.push(v2 - v1);
						break;
					case '/':
						postfixStack.push(v2 / v1);
						break;
					case '*':
						postfixStack.push(v2 * v1);
						break;
					}
				}
				System.out.println(postfixStack.toString());
			}
			if(postfixStack.isEmpty()) {
				System.out.println("Result: " + postfixStack.pop());
			} else {
				System.out.println("Malformed expression.");
			}
			System.out.println("Would you like to enter another expression [Y/N]");
			String temp = input.next();
			if(temp.equals("Y") || temp.equals("y")) {
				loop = true;
			} else {
				loop = false;
			}
			input.nextLine();
		}
		
		//Testing linkedlist and arraybasedlist
		ArrayBasedList<String> animalArrayList = new ArrayBasedList<>();
		LinkedList<String> animalLinkedList = new LinkedList<>();
		File file = new File("animals.txt");
		Scanner readFile = null;
		try {
			readFile = new Scanner(file);
		} catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		while(readFile.hasNext()) {
			String name = readFile.nextLine();
			animalArrayList.add(name);
			animalLinkedList.addLast(name);
		}
		int Lavg = 0;
        int Aavg = 0;
		Random r = new Random();
		for(int i = 0; i < 10; i++) {			
			int rand = r.nextInt(animalArrayList.size());
			String name = animalArrayList.get(rand);
			int alist = animalArrayList.searchIterations(name);
			int llist = animalLinkedList.searchIterations(name);
			Aavg += alist;
            Lavg += llist;
			System.out.printf("%-30s\t%-20d\t%-20d\n",name, alist, llist);
		}
		Aavg = Aavg/10;
        Lavg = Lavg/10;
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-32s%-24d%d\n", "Average # iterations", Aavg, Lavg);
	}
}
