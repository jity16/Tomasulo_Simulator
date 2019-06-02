import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tomasulo {
	public static void readFile() {
        String pathname = "test/test2.nel"; 
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public static void main(String []args){
    	readFile();
    }
}
