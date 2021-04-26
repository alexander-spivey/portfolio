////Made by Alexander Spivey
////CSE 02-210 HWK 2: Arithmetic
////Feburary 1st, 2020

import java.text.DecimalFormat; //importing so I can use decimals

public class Arithmetic{	
	private static DecimalFormat df2 = new DecimalFormat("#.##");
  public static void main(String args[])
  {
      //Number of pairs of pants
      int numPants = 3;
      //Cost per pair of pants
      double pantsPrice = 34.98;

      //Number of sweatshirts
      int numShirts = 2;
      //Cost per shirt
      double shirtPrice = 24.99;

      //Number of belts
      int numBelts = 1;
      //cost per belt
      double beltCost = 33.99;

      //the tax rate
      double paSalesTax = .06;

      //-----------------------------------------------------------------//

      //The total cost of pants
      double totalCostOfPants;
      totalCostOfPants = numPants * pantsPrice;

      //The total cost of shirts
      double totalCostOfShirts;
      totalCostOfShirts = numShirts * shirtPrice;

      //The total cost of belts
      double totalCostOfBelts;
      totalCostOfBelts = numBelts * beltCost;

      //-----------------------------------------------------------------//

      //Sales tax for all pants
      double salesTaxPants;
      salesTaxPants = totalCostOfPants * paSalesTax;

      //Sales tax for all shirts
      double salesTaxShirts;
      salesTaxShirts = totalCostOfShirts * paSalesTax;

      //Sales tax for all belts
      double salesTaxBelts;
      salesTaxBelts = totalCostOfBelts * paSalesTax;

      //-----------------------------------------------------------------//

      //The total cost of pants with sales tax
      double totalCostOfPantsWTax;
      totalCostOfPantsWTax = numPants * pantsPrice * (1 + paSalesTax);

      //The total cost of shirts with sales tax
      double totalCostOfShirtsWTax;
      totalCostOfShirtsWTax = numShirts * shirtPrice * (1 + paSalesTax);

      //The total cost of belts with sales tax
      double totalCostOfBeltsWTax;
      totalCostOfBeltsWTax = numBelts * beltCost * (1 + paSalesTax);

      //-----------------------------------------------------------------//

      //total cost of everything without tax
      double totalCostOfAll;
      totalCostOfAll = totalCostOfBelts + totalCostOfShirts + totalCostOfPants;

     //-----------------------------------------------------------------//

      //total cost of sales tax
      double totalCostOfSalesTax;
      totalCostOfSalesTax = salesTaxBelts + salesTaxShirts + salesTaxPants;
    
    //-----------------------------------------------------------------//
    
    //total paid with sales tax
    double totalCostOfAllWTax;
    totalCostOfAllWTax = totalCostOfBeltsWTax + totalCostOfPantsWTax + totalCostOfShirtsWTax;
      
    //-----------------------------------------------------------------//
		
		//prints out all totals to user
		System.out.println("You decided to buy "+numPants+" pants at the price of "+pantsPrice+".");
		System.out.println("You decided to buy "+numShirts+" shirts at the price of "+shirtPrice+".");
		System.out.println("You decided to buy "+numBelts+" belts at the price of "+beltCost+".");
		System.out.println("You paid "+totalCostOfPants+" for all your pants");
		System.out.println("You paid "+totalCostOfShirts+" for all your shirts");
		System.out.println("You paid "+totalCostOfBelts+" for all your belts");
		System.out.println("Your total before tax is "+ df2.format(totalCostOfAll));
		System.out.println("Your sales tax total is "+ df2.format(totalCostOfSalesTax));
		System.out.println("Your total with tax is "+ df2.format(totalCostOfAllWTax));
  }
}