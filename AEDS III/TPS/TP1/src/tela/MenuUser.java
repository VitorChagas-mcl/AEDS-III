package tela;

import arquivos.ArquivoUsuario;
import aux.Leitura;
import entidades.Usuario;

public class MenuUser {

    private ArquivoUsuario arqUsuario;
    
    public Usuario telaInicio(){
        String op;
        do{
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n(A) Login");
            System.out.println("(B) Novo usuário (primeiro acesso)");
            System.out.println("\n(S) Sair");
            System.out.print("\nOpação:");
        
            op = Leitura.Teclado().trim().toUpperCase();
            switch(op){
                case "S" -> { return null; }
                default -> System.out.println("Digite uma opção valida");
            }
        }while(true);
    }
}
