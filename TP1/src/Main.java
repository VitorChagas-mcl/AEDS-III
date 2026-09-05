import auxiliar.Leitura;
import tela.MenuUser;
import entidades.Usuario;
public class Main{
    public static void main(String[] args){
        try{
            MenuUser menuUser = new MenuUser();
            
            Usuario usuarioLogado = menuUser.telaInicio();
            if(usuarioLogado == null){
                System.out.println("Saindo...");
                return;
            }

            String op;
            do{
                System.out.println("\n\nAJUDA AÍ 1.0");
                System.out.println("------------");
                System.out.println("\n > Inicio");
                System.out.println("\n(A) Minha área");
                System.out.println("(B) Buscar perguntas");
                System.out.println("\n(S) Sair");
                System.out.print("\nOpção: ");

                op = Leitura.Teclado().trim().toUpperCase();
                switch(op){
                    case "A":
                        System.out.println("Tela do Usuario");
                        break;

                    case "B":
                        System.out.println("Tela de Perguntas");
                        break;

                    case "S":
                        System.out.println("Até logo...");
                        return;

                    default:
                        System.out.println("Digite uma opção valida");
                        break;
                }
            }while(!op.equalsIgnoreCase("S"));
        }catch(Exception e){
            System.out.println("Erro do sistema: " + e);
            e.printStackTrace();
        }
    }
}