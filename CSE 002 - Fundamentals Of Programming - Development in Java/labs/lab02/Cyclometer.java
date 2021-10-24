////Made by Alexander Spivey
////CSE 02-210 Lab02: Cyclometer
////Feburary 1st, 2020

public class Cyclometer{
  public static void main(String args[])
  {
    int secsTrip1=480;  // Trip one in secomnds
    int secsTrip2=3220;  // Trip 2 in secomds
		int countsTrip1=1561;  // Count of trip 1
		int countsTrip2=9037; // Count of trip 2
    
    double wheelDiameter=27.0,  //The Diameter of wheel
  	PI=3.14159, //these names explain themselves
  	feetPerMile=5280,  //
  	inchesPerFoot=12,   //
  	secondsPerMinute=60;  //
	  double distanceTrip1, distanceTrip2,totalDistance;  //
    
    System.out.println("Trip 1 took "+
       	     (secsTrip1/secondsPerMinute)+" minutes and had "+
       	      countsTrip1+" counts.");
	  System.out.println("Trip 2 took "+
       	     (secsTrip2/secondsPerMinute)+" minutes and had "+
       	      countsTrip2+" counts.");
    
    //run the calculations; store the values. Document your
		//calculation here. What are you calculating?
		//The above states the duration of trip 1 and 2 and how many counts each, aka the value of our
		//secsTrip1=480; 
    //secsTrip2=3220
		//countsTrip1=1561
		//countsTrip2=9037
    
	distanceTrip1=countsTrip1*wheelDiameter*PI;
    	// Above gives distance in inches
    	//(for each count, a rotation of the wheel travels
    	//the diameter in inches times PI)
	distanceTrip1/=inchesPerFoot*feetPerMile; // Gives distance in miles
	distanceTrip2=countsTrip2*wheelDiameter*PI/inchesPerFoot/feetPerMile;
	totalDistance=distanceTrip1+distanceTrip2;
    
    //Print out the output data.
  System.out.println("Trip 1 was "+distanceTrip1+" miles");
	System.out.println("Trip 2 was "+distanceTrip2+" miles");
	System.out.println("The total distance was "+totalDistance+" miles");
  }
}