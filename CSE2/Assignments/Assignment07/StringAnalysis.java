import java.util.*;

public class StringAnalysis{
	static boolean Simple(String str)
	{
		int j = str.length(), ct = 0;
		char foo;
		boolean hoo;
		for(int io = 0; io < j; io++) //loop to check every character
		{
			foo = str.charAt(io); //place holder
			hoo = Character.isLetter(foo);
			if (hoo == true){ //as long as positive
				ct = ct + 1; //cpounter on whcih letter is messed up
			}
			else{
				System.out.println("False, not all are letters. The first error is at character #"+(ct+1)); //first instance of non character
				return false;
			}
		}
		System.out.println("All are letters!");
		return true;
	}
	static boolean Complicated(String str,int length) //loop to find until certain value indicated by user
	{
		int j = str.length();
		int ct = 0;
		if(j < length){ //if length is longer than actual length, use the length of actual string
			length = j;
		}
		char foo;
		boolean hoo;
		for(int io = 0; io < length; io++)
		{
			foo = str.charAt(io); //same as earlier, checks each char at i and as long as its true, it won't return false
			hoo = Character.isLetter(foo);
			if (hoo == true){
				ct = ct + 1;
			}
			else{
				System.out.println("False, not all are letters. The first error is at character #"+(ct+1));
				return false;
			}
		}
		System.out.println("All are letters!");
		return true;
	}
	public static void main (String [] args){
		String input, resp;
		int leng;
		Scanner myScanner = new Scanner(System.in);
		System.out.print("Type test text: "); //ask for inoput text
		input = myScanner.nextLine();
		System.out.print("Would you like to check if all of that input was letters or just the first x characters? [All, Some]: "); //states whether all or some
		resp = myScanner.nextLine();
		if (resp.equals("All")){ //if response is all 
			Simple(input);
		}
		if (resp.equals("Some")){ //if response is some
			System.out.print("How many would you like to test?: "); //ask how many chars to check
			leng = myScanner.nextInt();
			Complicated(input, leng);
		}
	}
}