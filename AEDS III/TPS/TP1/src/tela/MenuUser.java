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
            login = validarEmail();
            System.out.print("Senha: ");
            senha = validarSenha();
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
            nome = validarNome();
            System.out.println("\nDigite seu email: ");
            email = validarEmail();
            System.out.println("\nDigite sua senha: ");
            senha = validarSenha();
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

    private String validarNome(){
        while(true){
            System.out.print("Nome (min 3 caracteres, vazio cancela): ");
            String nome = Leitura.Teclado().trim();
            if(nome.isEmpty()) return null;
            if(nome.length() >= 4) return nome;
            System.out.println("Nome inválido, digite novamente.");
        }
    }

    private String validarEmail(){
        while(true){
            System.out.print("Email (Vazio cancela): ");
            String email = Leitura.Teclado().trim().toLowerCase();
            if(email.isEmpty()) return null;
            if(email.contains("@") && email.contains(".")) return email;
            System.out.println("Email inválido, digite novamente.");
        }
    }

    private String validarSenha(){
        while(true){
            System.out.print("Senha (min 6 caracteres, vazio cancela): ");
            String senha = Leitura.Teclado().trim();
            if(senha.isEmpty()) return null;
            if(senha.length() >= 6) return senha;
            System.out.println("Senha inválida, digite novamente.");
        }
    }
}
