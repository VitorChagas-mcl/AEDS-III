package tela;

import arquivos.ArquivoUsuario;
import auxiliar.Leitura;
import entidades.Usuario;

public class MenuUser {

    private ArquivoUsuario arqUsuario;

    public MenuUser() throws Exception {
        arqUsuario = new ArquivoUsuario();
    }

    public Usuario telaInicio() {
        String op;
        do {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n(A) Login");
            System.out.println("(B) Novo usuário (primeiro acesso)");
            System.out.println("(C) Recuperar senha");
            System.out.println("\n(S) Sair");
            System.out.print("\nOpção: ");

            op = Leitura.Teclado().trim().toUpperCase();
            switch (op) {
                case "A":
                    return telaLogin();

                case "B":
                    telaNovoUsuario();
                    break;

                case "C":
                    telaRecuperarSenha();
                    break;

                case "S":
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Digite uma opção valida");
                    break;
            }
        } while (!op.equalsIgnoreCase("S"));
        return null;
    }

    public Usuario telaLogin() {
        System.out.println("\n\nLOGIN");
        System.out.print("\nEmail (vazio cancela): ");
        String email = Leitura.Teclado().trim().toLowerCase();
        if (email.isEmpty())
            return null;

        System.out.print("\nSenha (vazio cancela): ");
        String senha = Leitura.Teclado().trim();
        if (senha.isEmpty())
            return null;

        try {
            Usuario usuario = arqUsuario.readByEmail(email);

            if (usuario != null && usuario.getHashSenha() == senha.hashCode()) {
                System.out.println("Login feito com sucesso!");
                return usuario;
            }
        } catch (Exception e) {
            System.out.println("Erro ao fazer login: " + e.getMessage());
            return null;
        }

        System.out.println("Credenciais inválidas.");
        return null;
    }

    public void telaNovoUsuario() {
        String nome, email, senha, perguntaSecreta = null, respostaPergunta;

        System.out.println("\n\nNovo Usuário");
        System.out.println("----------------");
        System.out.println("\nDigite os dados do novo usuário:");
        System.out.println("\nDigite seu nome de usuário (min 3 caracteres, vazio cancela):");
        nome = validarNome();
        if (nome == null)
            return;
        System.out.println("\nDigite seu email (Vazio cancela): ");
        email = validarEmail();
        if (email == null)
            return;
        System.out.println("\nDigite sua senha (min 6 caracteres, vazio cancela): ");
        senha = validarSenha();
        if (senha == null)
            return;
        System.out.println("\nSelecione uma pergunta: (vazio cancela)");
        System.out.println("1) Qual o nome do seu animal de estimação?");
        System.out.println("2) Qual o nome da sua mãe?");
        System.out.println("3) Qual a cidade que você nasceu?");
        switch (Leitura.Teclado().trim()) {
            case "1":
                perguntaSecreta = "Qual o nome do seu animal de estimação?";
                break;

            case "2":
                perguntaSecreta = "Qual o nome da sua mãe?";
                break;

            case "3":
                perguntaSecreta = "Qual a cidade que você nasceu?";
                break;

            case "":
                return;

            default:
                System.out.println("Opção inválida, tente novamente.");
                break;
        }
        System.out.println("\nDigite sua Resposta (min 3 caracteres, vazio cancela): ");
        respostaPergunta = validarResposta();
        if (respostaPergunta == null)
            return;

        System.out.print("\nConfirma o cadastro? (S/N) ");
        if (!Leitura.Teclado().trim().equalsIgnoreCase("S")) {
            System.out.println("Cadastro cancelado.");
            return;
        }

        try {
            arqUsuario.create(new Usuario(nome, email, senha, perguntaSecreta, respostaPergunta));
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public void telaRecuperarSenha() {
        System.out.println("\n\nRECUPERAR SENHA");
        System.out.print("\nEmail: ");
        String email = Leitura.Teclado().trim().toLowerCase();

        if (email.isEmpty())
            return;

        try {
            Usuario usuario = arqUsuario.readByEmail(email);

            if (usuario == null) {
                System.out.println("Credenciais inválidas.");
                return;
            }

            System.out.println(usuario.getPerguntaSecreta());
            System.out.print("\nResposta: ");
            String resposta = Leitura.Teclado().trim();

            int hashResposta = Usuario.normalizarResposta(resposta).hashCode();

            if (hashResposta != usuario.getHashRespostaSecreta()) {
                System.out.println("Credenciais inválidas.");
                return;
            }

            System.out.print("\nDigite sua nova senha: ");
            String novaSenha = Leitura.Teclado().trim();

            if (novaSenha.length() < 6) {
                System.out.println("Senha inválida.");
                return;
            }

            usuario.setHashSenha(novaSenha);
            arqUsuario.update(usuario);

            System.out.println("Senha alterada com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro na recuperação: " + e.getMessage());
        }
    }

    public void telaMinhaArea(Usuario usuarioLogado) {
        String op;
        do {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Inicio > Minha área");
            System.out.println("\n(A) Meus dados");
            System.out.println("(B) Minhas perguntas");
            System.out.println("(C) Minhas respostas");
            System.out.println("(D) Meus votos");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.println("\nOpção: ");
            op = Leitura.Teclado().trim().toUpperCase();
        } while (!op.equals("R"));
    }

    public void telaMeusDados(Usuario usuarioLogado) {
        String op;
        do {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Inicio > Minha área > Meus dados");
            System.out.println("\n(A) Alterar Nome");
            System.out.println("(B) Alterar Email");
            System.out.println("(C) Alterar senha");
            System.out.println("(D) Alterar pergunta e resposta de recuperação de senha");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.println("\nOpção: ");
            op = Leitura.Teclado().trim().toUpperCase();
        } while (!op.equals("R"));
    }

    public void telaMinhasPerguntas(Usuario usuarioLogado) {
        System.out.println("\n\nAJUDA AÍ 1.0");
        System.out.println("------------");
        System.out.println("\nMINHAS PERGUNTAS");

        System.out.println("Pressione qualquer tecla para continuar...");
        Leitura.Teclado();
        telaMinhaArea(usuarioLogado);
    }

    private String validarNome() {
        while (true) {
            String nome = Leitura.Teclado().trim();
            if (nome.isEmpty()) {
                return null;
            }
            if (nome.length() >= 4)
                return nome;
            System.out.println("Nome inválido, digite novamente.");
        }
    }

    private String validarEmail() {
        while (true) {
            String email = Leitura.Teclado().trim().toLowerCase();
            if (email.isEmpty())
                return null;
            if (email.contains("@") && email.contains("."))
                return email;
            System.out.println("Email inválido, digite novamente.");
        }
    }

    private String validarSenha() {
        while (true) {
            String senha = Leitura.Teclado().trim();
            if (senha.isEmpty())
                return null;
            if (senha.length() >= 6)
                return senha;
            System.out.println("Senha inválida, digite novamente.");
        }
    }

    private String validarResposta() {
        while (true) {
            String resposta = Leitura.Teclado().trim();
            if (resposta.isEmpty())
                return null;
            if (resposta.length() >= 3)
                return resposta;
            System.out.println("resposta inválida, digite novamente.");
        }
    }
}
