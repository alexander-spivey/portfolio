public class HwkHelp{
	public static void main(String args[])
	{
		int i = 0;
		for( ; i < 49; i+=7){
			System.out.print(i%6 + "\n" );
		}




// 		int i, j, k;
// 		i = j = k = 2;
// 		if (i++ < ++j)
// 		{
// 				switch (j++)
// 				{
// 						case (1):
// 								System.out.printf("case 1 ");
// 								k %= 2;
// 								//break;   // [line 1]
// 						case (2):
// 								System.out.printf("case 2 ");
// 								k /= 3;
// 								//break;   // [line 2]
// 						case (3):
// 								System.out.printf("case 3 ");
// 								k -= 4;
// 								//break;   // [line 3]
// 						default:
// 								System.out.printf("default ");
// 								k *= 5;
// 								//break;   // [line 4]
// 				}
// 				System.out.printf("i+j+k = %2.1f", (double)(i - j + ++k));
// 		}
// 		else {
// 					i++;   // [line 5]
// 					System.out.printf("else ");
// 					System.out.printf("i+j+k = %2.1f", (double)(i - j + k++));
// 				}
	}
}