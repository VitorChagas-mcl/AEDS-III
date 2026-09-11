package tela;

import arquivos.ArquivoUsuario;
import auxiliar.Leitura;
import entidades.Pergunta;
import entidades.Usuario;
import tela.MenuPergunta;

public class MenuUser {

    private ArquivoUsuario arqUsuario;
    private MenuPergunta menuPergunta;

    public MenuUser() throws Exception {
        arqUsuario = new ArquivoUsuario();
        menuPergunta = new MenuPergunta();
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

    public void telaMinhaArea(Usuario usuarioLogado) throws Exception {
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
            System.out.print("\nOpção: ");
            op = Leitura.Teclado().trim().toUpperCase();

            switch (op) {
                case "A":
                    telaMeusDados(usuarioLogado);
                    break;

                case "B":
                    menuPergunta.telaMinhasPerguntas(usuarioLogado);
                    break;
                case "C":
                    System.out.println("Tela de Minhas respostas");
                    break;

                case "D":
                    System.out.println("Tela de Meus votos");
                    break;

                case "R":
                    System.out.println("Retornando ao menu anterior...");
                    break;

                default:
                    System.out.println("Digite uma opção válida");
                    break;
            }
        } while (!op.equals("R"));
    }

    public void telaMeusDados(Usuario usuarioLogado) {
        String op;
        do {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Inicio > Minha área > Meus dados");

            System.out.println(usuarioLogado.toString());
            System.out.println("\n(A) Alterar Nome");
            System.out.println("(B) Alterar Email");
            System.out.println("(C) Alterar senha");
            System.out.println("(D) Alterar pergunta e resposta de recuperação de senha");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");
            op = Leitura.Teclado().trim().toUpperCase();

            switch (op) {
                case "A":
                    telaAlterarNome(usuarioLogado);
                    break;

                case "B":
                    telaAlterarEmail(usuarioLogado);
                    break;

                case "C":
                    telaAlterarSenha(usuarioLogado);
                    break;

                case "D":
                    telaAlterarPerguntaResposta(usuarioLogado);
                    break;

                case "R":
                    System.out.println("Retornando ao menu anterior...");
                    break;

                default:
                    System.out.println("Digite uma opção valida");
                    break;
            }
        } while (!op.equals("R"));
    }

    public void telaAlterarNome(Usuario usuarioLogado) {
        try {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Inicio > Minha área > Meus dados > Alterar Nome");

            if (usuarioLogado == null) {
                System.out.println("\nErro ao encontrar usuario");
                return;
            }

            System.out.print("\nDigite o novo nome: ");
            String novoNome = validarNome();
            System.out.println("\nDeseja alterar seu nome: (S/N)");
            if (!Leitura.Teclado().trim().equalsIgnoreCase("S")) {
                System.out.println("Alteração cancelada");
                return;
            }
            usuarioLogado.setNome(novoNome);
            boolean sucesso = arqUsuario.update(usuarioLogado);
            if (sucesso)
                System.out.println("Nome alterado com sucesso!");
            else
                System.out.println("Erro ao alterar nome!");
        } catch (Exception e) {
            System.out.print("erro: " + e.getMessage());
        }
    }

    public void telaAlterarEmail(Usuario usuarioLogado) {
        try {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Inicio > Minha área > Meus dados > Alterar email");

            if (usuarioLogado == null) {
                System.out.println("\nErro ao encontrar usuario");
                return;
            }

            System.out.print("\nDigite o novo email: ");
            String novoEmail = validarEmail();

            System.out.println("\nDeseja alterar seu email: (S/N)");
            if (!Leitura.Teclado().trim().equalsIgnoreCase("S")) {
                System.out.println("Alteração cancelada");
                return;
            }
            usuarioLogado.setEmail(novoEmail);
            boolean sucesso = arqUsuario.update(usuarioLogado);
            if (sucesso)
                System.out.println("Email alterado com sucesso!");
            else
                System.out.println("Erro ao alterar Email!");
        } catch (Exception e) {
            System.out.print("erro: " + e.getMessage());
        }
    }

    public void telaAlterarSenha(Usuario usuarioLogado) {
        try {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Inicio > Minha área > Meus dados > Alterar senha");

            if (usuarioLogado == null) {
                System.out.println("\nErro ao encontrar usuario");
                return;
            }

            System.out.print("\nDigite o novo senha: ");
            String novaSenha = validarSenha();
            System.out.println("Deseja alterar sua senha: (S/N)");
            if (!Leitura.Teclado().trim().equalsIgnoreCase("S")) {
                System.out.println("Alteração cancelada");
                return;
            }
            usuarioLogado.setHashSenha(novaSenha);
            boolean sucesso = arqUsuario.update(usuarioLogado);
            if (sucesso)
                System.out.println("Senha alterado com sucesso!");
            else
                System.out.println("Erro ao alterar senha!");

        } catch (Exception e) {
            System.out.print("erro: " + e.getMessage());
        }
    }

    public void telaAlterarPerguntaResposta(Usuario usuarioLogado) {
        try {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println(
                    "\n> Inicio > Minha área > Meus dados > Alterar pergunta e resposta de recuperação de senha");

            if (usuarioLogado == null) {
                System.out.println("\nErro ao encontrar usuario");
                return;
            }

            System.out.println("\nEscolha uma nova pergunta: ");
            String novaPergunta = null;
            switch (Leitura.Teclado().trim()) {
                case "1":
                    novaPergunta = "Qual o nome do seu animal de estimação?";
                    break;

                case "2":
                    novaPergunta = "Qual o nome da sua mãe?";
                    break;

                case "3":
                    novaPergunta = "Qual a cidade que você nasceu?";
                    break;

                case "":
                    return;

                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }

            System.out.println("Digite uma nova resposta secreta: ");
            String novaRespostaHash = validarResposta();
            System.out.println("\nDeseja alterar sua pergunta e sua resposta secreta: (S/N)");
            if (Leitura.Teclado().trim().equalsIgnoreCase("S")) {
                System.out.println("Alteração cancelada");
                return;
            }
            usuarioLogado.setPerguntaSecreta(novaPergunta);
            usuarioLogado.setHashRespostaSecreta(novaRespostaHash);
            boolean sucesso = arqUsuario.update(usuarioLogado);
            if (sucesso)
                System.out.println("Pergunta e resposta alterado com sucesso!");
            else
                System.out.println("Erro ao alterar nome!");

        } catch (Exception e) {
            System.out.print("erro: " + e.getMessage());
        }
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
