//ALexander Spivey
//Yahtzee, game, no reroll, or selection
//Tueday, February 18th, 2020

import java.util.Scanner; //input
import java.util.Arrays; //array library
public class Yahtzee{
	public static void main(String args[])
	{
		int x = 0;
		while(x==0) //loop for program
		{
			Scanner myScanner = new Scanner(System.in);
			System.out.print("Would you rather roll or input values - [R/I]?: ");
			String input = myScanner.nextLine();
			int num1;
			int num2;
			int num3;
			int num4;
			int num5;
			
			//RANDOM OR INPUT
			if(input.equals("R"))
			{
				System.out.println("You choose to roll.");
				num1 = (int)(Math.random()*(6))+1;
				System.out.println(" Your 1st roll: "+num1);
				num2 = (int)(Math.random()*(6))+1;
				System.out.println(" Your 2nd roll: "+num2);
				num3 = (int)(Math.random()*(6))+1;
				System.out.println(" Your 3rd roll: "+num3);
				num4 = (int)(Math.random()*(6))+1;
				System.out.println(" Your 4th roll: "+num4);
				num5 = (int)(Math.random()*(6))+1;
				System.out.println(" Your 5th roll: "+num5);
			} else {
				System.out.println("You choose to input values.");
				System.out.print("What is your input value? - [5 digits]: ");
				String list = myScanner.nextLine();
				char[] array = list.toCharArray();
				if (array.length < 5 || array.length > 5)
				{
					System.out.println("Impossible value input"); //over 5 or under 5 digit input
					return;
				}
				
				num1 = Integer.parseInt(String.valueOf(array[0])); //cancel run if value is over 6 or under 1... this goes w/ all the others
				if (num1 > 6 || num1 < 1)
				{
					System.out.println("Impossible value input");
					return;
				}
				num2 = Integer.parseInt(String.valueOf(array[1]));
				if (num2 > 6 || num2 < 1)
				{
					System.out.println("Impossible value input");
					return;
				}
				num3 = Integer.parseInt(String.valueOf(array[2]));
				if (num3 > 6 || num3 < 1)
				{
					System.out.println("Impossible value input");
					return;
				}
				num4 = Integer.parseInt(String.valueOf(array[3]));
				if (num4 > 6 || num4 < 1)
				{
					System.out.println("Impossible value input");
					return;
				}
				num5 = Integer.parseInt(String.valueOf(array[4]));
				if (num5 > 6 || num5 < 1)
				{
					System.out.println("Impossible value input");
					return;
				}
			}
			
			//Array
			int[] valArray = new int[]{num1,num2,num3,num4,num5,10}; 
			
			//ACES
			int ctA = 0;
			for (int i = 0; i < 5; i++) //loop to find repition, same for all others
			{
				if(valArray[i] == 1)
				{
					ctA++; 
				}	
			}
			int valAces = ctA * 1;
			System.out.println("Aces: "+valAces);

			//TWOS
			int ct2 = 0;
			for (int i = 0; i < 5; i++)
			{
				if(valArray[i] == 2)
				{
					ct2++;
				}	
			}
			int valTwos = ct2 * 2;
			System.out.println("Twos: "+valTwos);

			//THREES
			int ct3 = 0;
			for (int i = 0; i < 5; i++)
			{
				if(valArray[i] == 3)
				{
					ct3++;
				}	
			}
			int valThrees = ct3 * 3;
			System.out.println("Threes: "+valThrees);

			//FOURS
			int ct4 = 0;
			for (int i = 0; i < 5; i++)
			{
				if(valArray[i] == 4)
				{
					ct4++;
				}	
			}
			int valFours = ct4 * 4;
			System.out.println("Fours: "+valFours);

			//FIVES
			int ct5 = 0;
			for (int i = 0; i < 5; i++)
			{
				if(valArray[i] == 5)
				{
					ct5++;
				}	
			}
			int valFives = ct5 * 5;
			System.out.println("Fives: "+valFives);

			//SIX
			int ct6 = 0;
			for (int i = 0; i < 5; i++)
			{
				if(valArray[i] == 6)
				{
					ct6++;
				}	
			}
			int valSixes = ct6 * 6;
			System.out.println("Sixes: "+valSixes);

			//Upper Summation
			int UpperSum = valAces + valTwos + valThrees + valFours + valFives + valSixes;
			if (UpperSum > 63)
			{
				UpperSum += 35;
			}
			System.out.println("Total Upper Sum: "+UpperSum);

			//Three of A Kind
			int ThreeOfAKind = 0; //check if there are any threeofakind
			if (ctA == 3){
				ThreeOfAKind = 1 * 3;
			}
			if (ct2 == 3){
				ThreeOfAKind = 2 * 3;
			}
			if (ct3 == 3){
				ThreeOfAKind = 3 * 3;
			}
			if (ct4 == 3){
				ThreeOfAKind = 4 * 3;
			}
			if (ct5 == 3){
				ThreeOfAKind = 5 * 3;
			}
			if (ct6 == 3){
				ThreeOfAKind = 6 * 3;
			}
			System.out.println("Three of a Kind: "+ThreeOfAKind);

			//Four of a Kind 
			int FourOfAKind = 0; //check if there is any 4 of a kind
			if (ctA == 4){
				FourOfAKind = 1 * 4;
			}
			if (ct2 == 4){
				FourOfAKind = 2 * 4;
			}
			if (ct3 == 4){
				FourOfAKind = 4 * 4;
			}
			if (ct4 == 4){
				FourOfAKind = 3 * 4;
			}
			if (ct5 == 4){
				FourOfAKind = 5 * 4;
			}
			if (ct6 == 4){
				FourOfAKind = 6 * 4;
			}
			System.out.println("Four of a Kind: "+FourOfAKind);

			//Yahtzee
			int Yahtze = 0;
			if (ctA == 5 || ct2 == 5 || ct3 == 5 || ct4 == 5 || ct5 == 5 || ct6 == 5) //if there is any 5 of a kind
			{
				Yahtze = 50;
			}
			System.out.println("Yahtzee: "+Yahtze);

			//Full House
			int FullHouse = 0;
			if ((ctA == 2 || ct2 == 2 || ct3 == 2 || ct4 == 2 || ct5 == 2 || ct6 == 2) && (ctA == 3 || ct2 == 3 || ct3 == 3 || ct4 == 3 || ct5 == 3 || ct6 == 3)) //if there is any with 3 of a kind and 2 of another kind
			{
				FullHouse = 25;
			}
			System.out.println("Full House: "+FullHouse);

			//Small Straight
			Arrays.sort(valArray); 
			int straightCT = 0;
			int SmallStraight = 0;
			for (int i = 0; i < 5; i++) //loop to check array to see if there is incremental for 4 
			{
				if(valArray[i] + 1 == valArray[i+1])
				{
					straightCT++;
				}
			}
			if(straightCT==3)
			{
				SmallStraight = 30;
			}
			System.out.println("Small Straight: "+SmallStraight);

			//Large Straight
			int LargeStraight = 0; //loop to check array to see if there is incremental for 5 
			if(straightCT==4)
			{
				LargeStraight = 40;
			}
			System.out.println("Large Straight: "+LargeStraight);

			//Chance
			int Chance = 0;
			for (int i = 0; i < 5; i++)
			{
				Chance = Chance + valArray[i]; 
			}
			System.out.println("Chance: "+Chance);

			//Lower Sum
			int LowerSum = Chance + LargeStraight + SmallStraight + FullHouse + Yahtze + FourOfAKind + ThreeOfAKind;
			System.out.println("Lower Sum: "+LowerSum);

			//Total Sum
			int TotalSum =  LowerSum + UpperSum;
			System.out.println("Total Sum: "+TotalSum);
			
			//Loop
			System.out.print("Want to try again? - [Y/N]: ");
			input = myScanner.nextLine();
			if (input.equals("Y"))
			{
				x = 0;
			} 
			else 
			{
				x = 1;
			}
		}
	}
}