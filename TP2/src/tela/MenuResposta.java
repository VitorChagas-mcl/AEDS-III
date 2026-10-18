package tela;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import arquivos.ArquivoPergunta;
import arquivos.ArquivoResposta;
import arquivos.ArquivoUsuario;
import auxiliar.Leitura;
import entidades.Pergunta;
import entidades.Resposta;
import entidades.Usuario;
import arquivos.ArquivoAvaliacao;
import entidades.Avaliacao;

public class MenuResposta {

    private static final int PERGUNTAS_POR_PAGINA = 5;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter
            .ofPattern("dd/MM/yyyy 'às' HH:mm")
            .withZone(ZoneId.of("America/Sao_Paulo"));

    private final ArquivoPergunta arqPergunta;
    private final ArquivoResposta arqResposta;
    private final ArquivoAvaliacao arqAvaliacao;
    private final ArquivoUsuario arqUsuario;
    private final Usuario usuarioLogado;

    public MenuResposta(Usuario usuarioLogado) throws Exception {
        arqPergunta = new ArquivoPergunta();
        arqResposta = new ArquivoResposta();
        arqAvaliacao = new ArquivoAvaliacao();
        arqUsuario = new ArquivoUsuario();
        this.usuarioLogado = usuarioLogado;
    }

    public void telaBuscarPergunta() throws Exception {
        String opcao;
        do {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Início > Buscar perguntas");
            System.out.println("\n(A) Perguntas mais recentes");
            System.out.println("(B) Busca por palavras-chave");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");

            opcao = Leitura.Teclado().trim().toUpperCase();
            switch (opcao) {
                case "A":
                    telaListarPerguntas();
                    break;
                case "B":
                    System.out.println("\nBusca por palavra-chave ainda não implementada.");
                    break;
                case "R":
                    break;
                default:
                    System.out.println("\nOpção inválida!");
            }
        } while (!opcao.equals("R"));
    }

    public void telaListarPerguntas() throws Exception {
        String opcao;
        int paginaAtual = 0;
        ArrayList<Pergunta> perguntas = arqPergunta.readPerguntasRecentes();

        do {
            System.out.println("\n\nAJUDA AÍ 1.0");
            System.out.println("------------");
            System.out.println("\n> Início > Buscar perguntas > Perguntas mais recentes\n");

            int inicio = paginaAtual * PERGUNTAS_POR_PAGINA;
            int fim = Math.min(inicio + PERGUNTAS_POR_PAGINA, perguntas.size());
            if (perguntas.isEmpty()) {
                System.out.println("Nenhuma pergunta cadastrada.");
            } else {
                for (int i = inicio; i < fim; i++) {
                    Pergunta pergunta = perguntas.get(i);
                    System.out.println("(" + (i + 1) + ") " + pergunta.getPergunta()
                            + "\n" + "Criada por " + nomeUsuario(pergunta.getIdUsuario())
                            + " em " + formatarData(pergunta.getCriacao()) + "\n");
                }
            }

            System.out.println("(A) Página anterior");
            System.out.println("(B) Próxima página");
            System.out.println("(R) Retornar");
            System.out.print("\nOpção: ");
            opcao = Leitura.Teclado().trim().toUpperCase();

            if (opcao.matches("\\d+")) {
                int indice = Integer.parseInt(opcao) - 1;
                if (indice >= inicio && indice < fim) {
                    telaPergunta(perguntas.get(indice));
                } else {
                    System.out.println("\nNúmero fora da página atual.");
                }
            } else {
                switch (opcao) {
                    case "A":
                        if (paginaAtual > 0) {
                            paginaAtual--;
                        } else {
                            System.out.println("\nVocê já está na primeira página.");
                        }
                        break;
                    case "B":
                        if ((paginaAtual + 1) * PERGUNTAS_POR_PAGINA < perguntas.size()) {
                            paginaAtual++;
                        } else {
                            System.out.println("\nVocê já está na última página.");
                        }
                        break;
                    case "R":
                        break;
                    default:
                        System.out.println("\nOpção inválida!");
                }
            }
        } while (!opcao.equals("R"));
    }

    private void telaPergunta(Pergunta pergunta) throws Exception {
        String opcao;
        do {
            System.out.println("\n\nAJUDA AÍ 1.0 \n------------\n> Início > Buscar perguntas > "
                    + "Perguntas mais recentes > Pergunta\n");
            imprimirPergunta(pergunta);

            ArrayList<Resposta> respostasAtivas = new ArrayList<>();
            for (Resposta resposta : arqResposta.readByPerguntaRespostas(pergunta.getId())) {
                if (resposta.isAtiva()) {
                    respostasAtivas.add(resposta);
                }
            }
            respostasAtivas.sort(Comparator
                    .comparingInt(Resposta::getNota)
                    .reversed()
                    .thenComparing(Comparator.comparingLong(Resposta::getCriacao).reversed()));

            System.out.println("\nRESPOSTAS\n---------\n");
            for (int i = 0; i < respostasAtivas.size(); i++) {
                Resposta resposta = respostasAtivas.get(i);
                System.out.println("(" + (i + 1) + ") " + resposta.getResposta());
                System.out.println("Respondido por " + nomeUsuario(resposta.getIdUsuario())
                        + " em " + formatarData(resposta.getCriacao()));
                System.out.println("Nota: " + resposta.getNota() + "  ("
                        + resposta.getAvaliacoes() + " avaliações)");
            }

            System.out.println("\n(A) Responder à pergunta");
            System.out.println("(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");
            opcao = Leitura.Teclado().trim().toUpperCase();

            if (opcao.equals("A")) {
                telaResponder(pergunta);
            } else if (opcao.matches("\\d+")) {
                int indice = Integer.parseInt(opcao) - 1;
                if (indice >= 0 && indice < respostasAtivas.size()) {
                    telaVisualizarResposta(pergunta, respostasAtivas.get(indice));
                } else {
                    System.out.println("\nResposta inválida.");
                }
            } else if (!opcao.equals("R")) {
                System.out.println("\nOpção inválida!");
            }
        } while (!opcao.equals("R"));
    }

    private void telaResponder(Pergunta pergunta) throws Exception {
        System.out.println("\n\nAJUDA AÍ 1.0\n------------\n> Início > Buscar perguntas > "
                + "Perguntas mais recentes > Pergunta > Responder\n");
        imprimirPergunta(pergunta);
        System.out.print("\nResposta: ");
        String texto = Leitura.Teclado().trim();
        if (texto.isEmpty()) {
            System.out.println("\nA resposta não pode ser vazia.");
            return;
        }

        Resposta resposta = new Resposta(pergunta.getId(), usuarioLogado.getId(), System.currentTimeMillis(),
                texto, (short) 0, (short) 0, true);
        arqResposta.create(resposta);
        System.out.println("\nResposta cadastrada com sucesso!");
    }

    private void telaVisualizarResposta(Pergunta pergunta, Resposta resposta) throws Exception {
        String opcao;
        boolean respostaDoUsuario = resposta.getIdUsuario() == usuarioLogado.getId();

        do {
            boolean jaAvaliou = !respostaDoUsuario
                    && arqAvaliacao.readByUsuarioResposta(
                            usuarioLogado.getId(), resposta.getId()) != null;

            System.out.println("\n\nAJUDA AÍ 1.0\n------------\n> Início > Buscar perguntas > "
                    + "Perguntas mais recentes > Pergunta > Resposta\n");
            imprimirPergunta(pergunta);

            System.out.println("\nRESPOSTA\n--------\n");
            System.out.println(resposta.getResposta());

            String autorResposta = respostaDoUsuario
                    ? "você"
                    : nomeUsuario(resposta.getIdUsuario());

            System.out.println("Respondido por " + autorResposta
                    + " em " + formatarData(resposta.getCriacao()));
            System.out.println("Nota: " + resposta.getNota()
                    + " (" + resposta.getAvaliacoes() + " avaliações)");

            if (respostaDoUsuario) {
                System.out.println("\n(A) Arquivar resposta");
            } else if (jaAvaliou) {
                System.out.println("\nVocê já avaliou esta resposta.");
            } else {
                System.out.println("\n(A) Concordar com a resposta");
                System.out.println("(B) Discordar da resposta");
            }

            System.out.println("(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");
            opcao = Leitura.Teclado().trim().toUpperCase();

            if (respostaDoUsuario && opcao.equals("A")) {
                resposta.setAtiva(false);

                if (arqResposta.update(resposta)) {
                    System.out.println("\nResposta arquivada com sucesso!");
                    return;
                }

                throw new Exception("Não foi possível arquivar a resposta.");
            } else if (!respostaDoUsuario
                    && !jaAvaliou
                    && (opcao.equals("A") || opcao.equals("B"))) {
                boolean positiva = opcao.equals("A");
                registrarAvaliacao(resposta, positiva);
                System.out.println("\nAvaliação registrada.");
                return;
            } else if (!respostaDoUsuario
                    && jaAvaliou
                    && (opcao.equals("A") || opcao.equals("B"))) {
                System.out.println("\nVocê já avaliou esta resposta.");
            } else if (!opcao.equals("R")) {
                System.out.println("\nOpção inválida!");
            }
        } while (!opcao.equals("R"));
    }

    private void imprimirPergunta(Pergunta pergunta) throws Exception {
        System.out.println(pergunta.getPergunta());
        System.out.println("Criada por " + nomeUsuario(pergunta.getIdUsuario())
                + " em " + formatarData(pergunta.getCriacao()));
    }

    private String nomeUsuario(int idUsuario) throws Exception {
        Usuario usuario = arqUsuario.read(idUsuario);
        return usuario == null ? "usuário desconhecido" : usuario.getNome();
    }

    private String formatarData(long data) {
        return FORMATO_DATA.format(Instant.ofEpochMilli(data));
    }

    private void registrarAvaliacao(Resposta respostaSelecionada, boolean positiva)
            throws Exception {
        Resposta resposta = arqResposta.read(respostaSelecionada.getId());

        if (resposta == null || !resposta.isAtiva()) {
            throw new Exception("Resposta não encontrada ou arquivada.");
        }

        if (resposta.getIdUsuario() == usuarioLogado.getId()) {
            throw new Exception("Você não pode avaliar sua própria resposta.");
        }

        if (arqAvaliacao.readByUsuarioResposta(
                usuarioLogado.getId(), resposta.getId()) != null) {
            throw new Exception("Você já avaliou esta resposta.");
        }

        short notaAnterior = resposta.getNota();
        short avaliacoesAnteriores = resposta.getAvaliacoes();

        int variacao = positiva ? 1 : -1;
        resposta.setNota((short) (notaAnterior + variacao));
        resposta.setAvaliacoes((short) (avaliacoesAnteriores + 1));

        if (!arqResposta.update(resposta)) {
            throw new Exception("Não foi possível atualizar a resposta.");
        }

        try {
            arqAvaliacao.create(new Avaliacao(
                    usuarioLogado.getId(),
                    resposta.getId(),
                    positiva));
        } catch (Exception erro) {
            resposta.setNota(notaAnterior);
            resposta.setAvaliacoes(avaliacoesAnteriores);

            if (!arqResposta.update(resposta)) {
                throw new Exception(
                        "Falha ao registrar a avaliação e ao restaurar a resposta.",
                        erro);
            }

            throw erro;
        }
    }
}
