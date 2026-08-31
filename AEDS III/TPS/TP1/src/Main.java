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
        }catch(Exception e){

        }
    }
}