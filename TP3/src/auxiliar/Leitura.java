package auxiliar;

import java.io.Console;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Leitura {
    private static final Scanner scan = new Scanner(System.in, charsetDoTerminal());

    // Usa o mesmo charset do terminal (no Windows costuma ser cp850/cp1252, não UTF-8),
    // senão os acentos digitados viram U+FFFD ao serem decodificados.
    private static Charset charsetDoTerminal() {
        Console console = System.console();
        if (console != null) {
            return console.charset();
        }
        for (String propriedade : new String[] { "stdin.encoding", "native.encoding" }) {
            String nome = System.getProperty(propriedade);
            if (nome != null) {
                try {
                    return Charset.forName(nome);
                } catch (Exception e) {
                    // tenta a próxima opção
                }
            }
        }
        return Charset.defaultCharset();
    }

    public static String Teclado(){
        try{
            return scan.hasNextLine() ? scan.nextLine() : "";
        }catch(NoSuchElementException e){
            return "";
        }
    }
}