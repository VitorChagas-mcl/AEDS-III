package aux;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Leitura {
    private static final Scanner scan = new Scanner(System.in);
    public static String Teclado(){
        try{
            return scan.hasNextLine() ? scan.nextLine() : "";
        }catch(NoSuchElementException e){
            return "";
        }
    }
}
