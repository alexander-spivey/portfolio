//card teller


public class Lab4{
	public static void main(String[]args){
		int number = (int)(Math.random()*(52))+1;//initialize random number from 0-51, add 1
		String suitName, cardIdentity = "";
		if(number/14 == 0){
			suitName = "Diamonds";
		}
		else if(number/14 == 1){
			suitName = "Clubs";
			number -= 13;//convert to 1-13
		}
		else if(number/27 == 1){
			suitName = "Hearts";
			number -= 26;//convert to 1-13
		}
		else{
			suitName = "Spades";
			number -= 39;//convert to 1-13
		}
		if(number < 11 && number > 1){
			cardIdentity = Integer.toString(number);//set card 2 to 10 to string
		}
		else{
			switch(number){
				case 1://set 1 to Ace
					cardIdentity = "Ace";
					break;
				case 11://set 11 to Jack
					cardIdentity = "Jack";
					break;
				case 12://set 12 to Queen
					cardIdentity = "Queen";
					break;
				case 13://set 13 to King
					cardIdentity = "King";
					break;
				}
		}
		System.out.println("You picked the " + cardIdentity + " of " + suitName);//print card
	}
} 
