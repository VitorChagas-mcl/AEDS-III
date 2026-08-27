package tela;

import arquivos.ArquivoUsuario;
import aux.Leitura;
import entidades.Usuario;

public class MenuUser {

    private ArquivoUsuario arqUsuario;
    public Usuario telaLogin(){
        String op;
        do{
            op = Leitura.LeituraTeclado().trim().toUpperCase();
            switch(op){
                
            }
        }while(true);
    }
}
