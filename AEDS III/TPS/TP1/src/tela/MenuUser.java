package tela;

import arquivos.ArquivoUsuario;
import auxiliar.Leitura;
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
            System.out.print("\nOpção: ");
        
            op = Leitura.Teclado().trim().toUpperCase();
            switch(op){
                case "A":
                    return telaLogin();

                case "B":
                    return telaNovoUsuario();
                    
                case "S":
                    return null;

                default:
                    System.out.println("Digite uma opção valida");
                break;
            }
        }while(true);
    }

    public Usuario telaLogin(){
        String login, senha;
        int senhaHash, respostaHash;
        Usuario usuarioLogado = null;
        do{
            System.out.println("\n\nLOGIN");
            System.out.println("-----");
            System.out.print("\nLogin: ");
            login = Leitura.Teclado().trim();
            System.out.print("Senha: ");
            senha = Leitura.Teclado().trim();
                }while(usuarioLogado == null);
        return usuarioLogado;
    }

    public Usuario telaNovoUsuario(){
        String nome, email, senha, perguntaSecreta, respostaPergunta;
        int senhaHash, respostaHash;
        Usuario usuarioLogado = null;

        do{
            System.out.println("\n\nNovo Usuário");
            System.out.println("----------------");
            System.out.println("\nDigite os dados do novo usuário:");
            System.out.println("\nDigite seu nome de usuário: ");
            nome = Leitura.Teclado().trim();
            System.out.println("\nDigite seu email: ");
            email = Leitura.Teclado().trim().toLowerCase();
            System.out.println("\nDigite seu senha: ");
            senha = Leitura.Teclado().trim();
            System.out.println("\nSelencione uma pergunta: ");
            System.out.println("1) Qual o nome do seu animal de estimação?");
            System.out.println("2) Qual o nome da sua mãe?");
            System.out.println("3) Qual a cidade que você nasceu?");
            switch(Leitura.Teclado().trim()){
                case "1":
                    perguntaSecreta = "Qual o nome do seu animal de estimação?";
                break;

                case "2":
                    perguntaSecreta = "Qual o nome da sua mãe?";
                break;

                case "3":
                    perguntaSecreta = "Qual a cidade que você nasceu?";
                break;
            }
            System.out.println("\nDigite sua Resposta: ");
            respostaPergunta = Leitura.Teclado().trim();
        }while(usuarioLogado == null);
        return usuarioLogado;
    }
}
