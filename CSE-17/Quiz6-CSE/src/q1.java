import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;


public class q1 {
    public static void main(String[]args) {
        Hashtable<Integer, String> hm = new Hashtable<Integer, String>();
        Scanner in = new Scanner(System.in);
        File file = new File("words_alpha.txt");
        Scanner readFile = null;
        FileWriter writer = null;
        try {
            writer = new FileWriter("numbers.txt");
            readFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
        while (readFile.hasNext()) {
            String word = readFile.next();
            int total = 0;
            for(int i=0;i<word.length();i++) {
                char c = word.charAt(i);
                int a = c;
                total+=a;
            }
            try {
                writer.write(total + "\n");
            } catch (IOException e) {
                System.out.println("L");
                e.printStackTrace();
            }
            hm.put(total, word);
        }
        in.close();
    }
}