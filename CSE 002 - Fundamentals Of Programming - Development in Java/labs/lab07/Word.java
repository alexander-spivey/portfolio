import java.util.*;

public class Word{
	static String Adjectives() 
	{
		String adjword = "";
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(9);
		switch(randomInt)
		{
			case 0:
				adjword = "stinky";
				break;
			case 1:
				adjword = "hairy";
				break;
			case 2:
				adjword = "fat";
				break;
			case 3:
				adjword = "fast";
				break;
			case 4:
				adjword = "short";
				break;
			case 5:
				adjword = "determined";
				break;
			case 6:
				adjword = "confused";
				break;
			case 7:
				adjword = "lazy";
				break;
			case 8:
				adjword = "incompetent";
				break;
		}
		System.out.print(adjword);
		return adjword;
  }
	static String Verbs() 
	{
		String verbword = "";
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(9);
		switch(randomInt)
		{
			case 0:
				verbword = "tripped";
				break;
			case 1:
				verbword = "hugged";
				break;
			case 2:
				verbword = "ate";
				break;
			case 3:
				verbword = "shot";
				break;
			case 4:
				verbword = "carried";
				break;
			case 5:
				verbword = "fought";
				break;
			case 6:
				verbword = "studied";
				break;
			case 7:
				verbword = "chased";
				break;
			case 8:
				verbword = "climbed";
				break;
		}
		System.out.print(verbword);
		return verbword;
  }
	static String Obj() 
	{
		String objword = "";
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(9);
		switch(randomInt)
		{
			case 0:
				objword = "cat";
				break;
			case 1:
				objword = "dog";
				break;
			case 2:
				objword = "rat";
				break;
			case 3:
				objword = "bunny";
				break;
			case 4:
				objword = "shrimp";
				break;
			case 5:
				objword = "elephant";
				break;
			case 6:
				objword = "giraffe";
				break;
			case 7:
				objword = "donkey";
				break;
			case 8:
				objword = "liger";
				break;
		}
		System.out.print(objword);
		return objword;
  }
	static String Subj() 
	{
		String subjword = "";
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(9);
		switch(randomInt)
		{
			case 0:
				subjword = "horse";
				break;
			case 1:
				subjword = "chicken";
				break;
			case 2:
				subjword = "cow";
				break;
			case 3:
				subjword = "bull";
				break;
			case 4:
				subjword = "monkey";
				break;
			case 5:
				subjword = "hamster";
				break;
			case 6:
				subjword = "girl";
				break;
			case 7:
				subjword = "doctor";
				break;
			case 8:
				subjword = "man";
				break;
		}
		//System.out.print(subjword);
		return subjword;
  }
	public static void main (String [] args)
	{
		String word = "";
		int x = 0;
		while(x==0) //loop for program
		{
			word = Subj();
			Scanner myScanner = new Scanner(System.in);
			System.out.print("The ");
			Adjectives();
			System.out.print(" ");
			System.out.print(word);
			System.out.print(" ");
			Verbs();
			System.out.print(" ");
			System.out.print("the ");
			Adjectives();
			System.out.print(" ");
			Obj();
			System.out.println(".");
				
			System.out.print("Want to try again? - [Y/N]: ");
			String input = "";
			input = myScanner.nextLine();
			if (input.equals("Y"))
			{
				x = 0;
			} else {
				x = 1;
			}
		}
		System.out.print("The ");
		System.out.print(word);
		System.out.print(" ");
		System.out.print("was also ");
		Adjectives();
		System.out.print(" ");
		System.out.print("when it came to ");
		Obj();
		System.out.println("s.");
		
		System.out.print("The ");
		System.out.print(word);
		System.out.print(" ");
		System.out.print("favorite person to hang out with was the ");
		Adjectives();
		System.out.print(" ");
		Obj();
		System.out.println(".");
	}
}