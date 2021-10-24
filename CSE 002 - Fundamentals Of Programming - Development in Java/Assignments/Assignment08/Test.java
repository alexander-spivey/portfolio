import java.util.*;
public class Test{
public static void A( int[] a, int[] b){
int[] temp1 = new int[a.length];
int[] temp2 = new int[b.length];
for( int i = 0; i<a.length; i++ ){
temp1[i] = a[i];
temp2[i] = b[i];
}
a = temp2;
b = temp1;

}
    
public static int[] B(int[] a, int[] b){
int[] quack = new int[a.length];
for( int i = 0; i< a.length; i++){quack[i] = a[i];}
for( int j = 0; j< a.length; j++){a[j] = b[j];}
for( int k = 0; k< a.length; k++){b[k] = quack[k];}
return quack;
}

public static int [] C(int [] a, int[] b ){
for(int i = 0; i < a.length; i++)
b[i] = a[i] + b[i];
return b;
}

public static void printArray(int[] array){
System.out.print("[ ");
for( int i = 0; i<array.length; i++){
System.out.print( array[i] + ", "); 
} 
System.out.println("]");
}


///part 1a  
// public static void main( String[] args ){
//    int[] test1 = {1, 4, 7}; 
//    int[] test2 = {2, 5, 8};
//    A( test1, test2);
//    printArray(test1);
//    printArray(test2);
// }
 









///part 1b
// public static void main( String[] args ){
//    int[] test1 = {1, 4, 7}; 
//    int[] test2 = {2, 5, 8};
//    int[] test3; 
//    test3 = B( test1, test2);
//    printArray(test1);
//    printArray(test2); 
//    printArray(test3);
// }











///part 1c  
// public static void main( String[] args ){
//    int[] test1 = {1, 4, 7}; 
//    int[] test2 = {2, 5, 8};
//    int[] test3;
//    test3 = C( test1, test2);
//    printArray(test1);
//    printArray(test2);
//    printArray(test3);
// }
 










///part 1d
public static void main( String[] args ){
   int[] test1 = {1, 4, 7}; 
   int[] test2 = {2, 5, 8};
   int[] test3;
   test3 = C( test1, test2);
   test1[0] = 90;
   printArray(test1);
   printArray(test2);
   printArray(test3);
}

}