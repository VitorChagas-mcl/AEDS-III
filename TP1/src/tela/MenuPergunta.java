package tela;

import java.util.ArrayList;

import auxiliar.Leitura;
import entidades.Pergunta;
import entidades.Usuario;
import arquivos.ArquivoPergunta;

public class MenuPergunta {

    private ArquivoPergunta arqPergunta;

    public MenuPergunta() throws Exception {
        arqPergunta = new ArquivoPergunta();
    }

    public void telaMinhasPerguntas(Usuario usuarioLogado) throws Exception {
        String op;
        do {
            ArrayList<Pergunta> perguntas = arqPergunta.readByUsuarioPerguntas(usuarioLogado.getId());
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Inicio > Minha área > Minhas perguntas");
            System.out.println("\n(A) Incluir pergunta");
            System.out.println("(B) Listar minhas perguntas");
            System.out.println("(C) Alterar Pergunta");
            System.out.println("(D) Excluir Pergunta");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");
            op = Leitura.Teclado().trim().toUpperCase();

            switch (op) {
                case "A":
                    telaIncluirPergunta(usuarioLogado);
                    break;

                case "B":
                    telaListarPerguntas(usuarioLogado, perguntas);
                    break;
                case "C":
                    telaAtualizarPergunta(usuarioLogado, perguntas);
                    break;

                case "D":
                    telaApagarPergunta(usuarioLogado, perguntas);
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

    public void telaIncluirPergunta(Usuario usuarioLogado) throws Exception {
        try {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\nINCLUIR PERGUNTA");
            System.out.print("Digite a pergunta: ");
            String pergunta = Leitura.Teclado();
            System.out.print("Digite as palavras chave (separadas por ';'): ");
            String palavrasChave = Leitura.Teclado();
            Pergunta novaPergunta = new Pergunta(usuarioLogado.getId(), System.currentTimeMillis(),
                    System.currentTimeMillis(), (short) 0, pergunta, palavrasChave, true);
            int idPergunta = arqPergunta.create(novaPergunta);
            System.out.println(idPergunta);
            if (idPergunta != -1) {
                System.out.println("Pergunta cadastrada com sucesso! ID: " + idPergunta);
            } else {
                System.out.println("Erro ao cadastrar pergunta!");
            }
            System.out.println("Pressione qualquer tecla para continuar...");
            Leitura.Teclado();
        } catch (Exception e) {
            System.out.print("erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void telaListarPerguntas(Usuario usuarioLogado, ArrayList<Pergunta> perguntas) throws Exception {
        try {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\nMINHAS PERGUNTAS");
            if (perguntas.isEmpty()) {
                System.out.println("Nenhuma pergunta cadastrada!");
            } else {
                System.out.println("Perguntas cadastradas:");
                for (Pergunta p : perguntas) {
                    System.out.println(p.toString());
                }
            }
            System.out.println("Pressione qualquer tecla para continuar...");
            Leitura.Teclado();
            telaMinhasPerguntas(usuarioLogado);
        } catch (Exception e) {
            System.out.print("erro: " + e.getMessage());
        }
    }

    public void telaApagarPergunta(Usuario usuarioLogado, ArrayList<Pergunta> perguntas) throws Exception {
        try {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\nAPAGAR PERGUNTA");
            System.out.println("Perguntas: ");
            for (Pergunta p : perguntas) {
                if (p.getAtiva() == true) {
                    System.out.println(p.toString());
                }
            }
            System.out.print("Digite o ID da pergunta que deseja apagar: ");
            int idPergunta = Integer.parseInt(Leitura.Teclado());
            Pergunta pergunta = arqPergunta.read(idPergunta);
            if (pergunta == null) {
                System.out.println("Pergunta não encontrada!");
            } else {
                if (pergunta.getIdUsuario() != usuarioLogado.getId()) {
                    System.out.println("Você não tem permissão para apagar essa pergunta!");
                } else {
                    pergunta.setAtiva(false);
                    if (arqPergunta.update(pergunta)) {
                        System.out.println("Pergunta apagada com sucesso!");
                    } else {
                        System.out.println("Erro ao apagar pergunta!");
                    }
                }
            }
            System.out.println("Pressione qualquer tecla para continuar...");
            Leitura.Teclado();
        } catch (Exception e) {
            System.out.print("erro: " + e.getMessage());
        }
    }

    public void telaAtualizarPergunta(Usuario usuarioLogado, ArrayList<Pergunta> perguntas) throws Exception {
        try {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\nATUALIZAR PERGUNTA");
            for (Pergunta p : perguntas) {
                if (p.getAtiva() == true) {
                    System.out.println(p.toString());
                }
            }
            System.out.print("Digite o ID da pergunta que deseja atualizar: ");
            int idPergunta = Integer.parseInt(Leitura.Teclado());
            Pergunta pergunta = arqPergunta.read(idPergunta);
            if (pergunta == null) {
                System.out.println("Pergunta não encontrada!");
            } else {
                if (pergunta.getIdUsuario() != usuarioLogado.getId()) {
                    System.out.println("Você não tem permissão para atualizar essa pergunta!");
                } else {
                    System.out.print("Digite a nova pergunta (vazio cancela): ");
                    String novaPergunta = Leitura.Teclado();
                    System.out.print("Digite as novas palavras-chave (vazio cancela): ");
                    String novasPalavrasChave = Leitura.Teclado();
                    if (!novaPergunta.isEmpty()) {
                        pergunta.setPergunta(novaPergunta);
                        pergunta.setAlteracao(System.currentTimeMillis());
                        if (arqPergunta.update(pergunta)) {
                            System.out.println("Pergunta atualizada com sucesso!");
                        } else {
                            System.out.println("Erro ao atualizar pergunta!");
                        }
                    }
                    if(!novasPalavrasChave.isEmpty()) {
                        pergunta.setPalavrasChave(novasPalavrasChave);
                        arqPergunta.update(pergunta);
                    }
                }
            }
            System.out.println("Pressione qualquer tecla para continuar...");
            Leitura.Teclado();
        } catch (Exception e) {
            System.out.print("erro: " + e.getMessage());
        }
    }
}