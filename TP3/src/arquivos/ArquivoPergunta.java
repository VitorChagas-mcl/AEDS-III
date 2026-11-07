package arquivos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.text.Normalizer;

import aed3.ArvoreBMais;
import aed3.ElementoLista;
import aed3.ListaInvertida;
import entidades.Pergunta;
import entidades.Resposta;
import aed3.ParIdUsuarioPergunta;
import aed3.ParIdPerguntaData;

public class ArquivoPergunta extends aed3.Arquivo<Pergunta> {

    private ArvoreBMais<ParIdUsuarioPergunta> IndiceUsuarioPergunta;
    private ArvoreBMais<ParIdPerguntaData> IndicePerguntaData;
    private ListaInvertida indicePalavras;

    private static final Set<String> STOP_WORDS = new HashSet<String>();
    static {
        String palavras = "a ao aos as ate com da das de dei do dos e em entre era eram "
                + "essa essas esse esses esta estas este estes eu foi foram ha "
                + "isso isto ja mas na nas no nos num numa o os ou para pela pelas "
                + "pelo pelos por que se sem seu seus sua suas tambem um uma uns umas "
                + "como quando onde porque qual quais quem quanto sobre";
        for (String palavra : palavras.split(" ")) {
            STOP_WORDS.add(palavra);
        }
    }

    public ArquivoPergunta() throws Exception {
        super("Pergunta", Pergunta.class.getConstructor());
        IndiceUsuarioPergunta = new ArvoreBMais<ParIdUsuarioPergunta>(ParIdUsuarioPergunta.class.getConstructor(),
                5, "./dados/Pergunta/indicePergunta.diretorio.db");
        IndicePerguntaData = new ArvoreBMais<ParIdPerguntaData>(ParIdPerguntaData.class.getConstructor(),
                5, "./dados/Pergunta/indicePerguntaData.diretorio.db");
        indicePalavras = new ListaInvertida(8, "./dados/Pergunta/indicePalavras.diretorio.db",
                "./dados/Pergunta/indicePalavras.cestos.db");

        int perguntasAtivas = 0;
        for (Pergunta pergunta : readAll()) {
            if (pergunta.getAtiva()) {
                perguntasAtivas++;
            }
        }
        if (indicePalavras.numeroEntidades() != perguntasAtivas) {
            reconstruirIndicePalavras();
        }
    }

    @Override
    public int create(Pergunta pergunta) throws Exception {
        if (new ArquivoUsuario().read(pergunta.getIdUsuario()) == null) {
            throw new Exception("O usuário autor da pergunta não existe.");
        }

        int id = super.create(pergunta);
        IndiceUsuarioPergunta.create(new ParIdUsuarioPergunta(pergunta.getIdUsuario(), id));
        IndicePerguntaData.create(new ParIdPerguntaData(id, pergunta.getCriacao()));
        indexarPergunta(pergunta);
        indicePalavras.incrementaEntidades();
        return id;
    }

    public ArrayList<Pergunta> readByUsuarioPerguntas(int idUsuario) throws Exception {
        ArrayList<Pergunta> perguntas = new ArrayList<>();
        ArrayList<ParIdUsuarioPergunta> perguntasIndice = IndiceUsuarioPergunta.read(null);
        for (ParIdUsuarioPergunta par : perguntasIndice) {
            if (par.getIdUsuario() == idUsuario) {
                Pergunta pergunta = super.read(par.getIdPergunta());
                if (pergunta != null) {
                    perguntas.add(pergunta);
                }
            }
        }
        return perguntas;
    }

    public ArrayList<Pergunta> readPerguntasRecentes() throws Exception {
        ArrayList<Pergunta> perguntas = new ArrayList<>();
        ArrayList<ParIdPerguntaData> perguntasIndice = IndicePerguntaData.read(null);

        for (ParIdPerguntaData par : perguntasIndice) {
            Pergunta pergunta = super.read(par.getIdPergunta());
            if (pergunta != null && pergunta.getAtiva()) {
                perguntas.add(pergunta);
            }
        }

        perguntas.sort(Comparator
                .comparingLong(Pergunta::getCriacao)
                .reversed()
                .thenComparing(Comparator.comparingInt(Pergunta::getId).reversed()));
        return perguntas;
    }


    @Override
    public boolean update(Pergunta PerguntaNova) throws Exception {
        Pergunta PerguntaAntigo = super.read(PerguntaNova.getId());
        if (PerguntaAntigo == null)
            return false;
        if (PerguntaAntigo.getAtiva() == false)
            return false;
        if (new ArquivoUsuario().read(PerguntaNova.getIdUsuario()) == null) {
            throw new Exception("O usuário autor da pergunta não existe.");
        }

        if (super.update(PerguntaNova)) {
            if (PerguntaAntigo.getIdUsuario() != PerguntaNova.getIdUsuario()) {
                this.IndiceUsuarioPergunta
                        .delete(new ParIdUsuarioPergunta(PerguntaAntigo.getIdUsuario(), PerguntaAntigo.getId()));
                this.IndiceUsuarioPergunta
                        .create(new ParIdUsuarioPergunta(PerguntaNova.getIdUsuario(), PerguntaNova.getId()));
            }
            if (PerguntaAntigo.getAtiva() && PerguntaNova.getAtiva()) {
                atualizarIndicePalavras(PerguntaAntigo, PerguntaNova);
            } else if (PerguntaAntigo.getAtiva() && !PerguntaNova.getAtiva()) {
                removerDoIndice(PerguntaAntigo);
                indicePalavras.decrementaEntidades();
            } else if (!PerguntaAntigo.getAtiva() && PerguntaNova.getAtiva()) {
                indexarPergunta(PerguntaNova);
                indicePalavras.incrementaEntidades();
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(int idPergunta) throws Exception {
        if (super.read(idPergunta) == null) {
            System.out.println("Pergunta não encontrada.");
            return false;
        }
        Pergunta Pergunta = super.read(idPergunta);
        if (Pergunta.getAtiva() == false) {
            System.out.println("Pergunta já está inativa.");
            return false;
        }

        ArquivoResposta arquivoResposta = new ArquivoResposta();
        ArrayList<Resposta> respostas = arquivoResposta.readByPerguntaRespostas(idPergunta);
        for (Resposta resposta : respostas) {
            if (!arquivoResposta.delete(resposta.getId())) {
                throw new Exception("Não foi possível excluir uma resposta relacionada à pergunta.");
            }
        }

        if (super.delete(idPergunta)) {
            this.IndiceUsuarioPergunta.delete(new ParIdUsuarioPergunta(Pergunta.getIdUsuario(), idPergunta));
            this.IndicePerguntaData.delete(new ParIdPerguntaData(idPergunta, Pergunta.getCriacao()));
            removerDoIndice(Pergunta);
            indicePalavras.decrementaEntidades();
            Pergunta.setAtiva(false);
            return true;
        }

        return false;
    }

    public ArrayList<Pergunta> readPerguntasPorPalavras(String consulta) throws Exception {
        ArrayList<Pergunta> resultado = new ArrayList<>();
        Map<Integer, Float> pontuacoes = new HashMap<>();
        Set<String> termos = new HashSet<>(normalizarTermos(consulta));
        int entidades = indicePalavras.numeroEntidades();
        if (entidades == 0) {
            return resultado;
        }

        for (String termo : termos) {
            ElementoLista[] ocorrencias = indicePalavras.read(termo);
            if (ocorrencias.length == 0) {
                continue;
            }
            double idf = Math.log10((double) entidades / ocorrencias.length);
            for (ElementoLista ocorrencia : ocorrencias) {
                Pergunta pergunta = read(ocorrencia.getId());
                if (pergunta != null && pergunta.getAtiva()) {
                    float pontuacao = (float) (ocorrencia.getFrequencia() * idf);
                    pontuacoes.put(ocorrencia.getId(),
                            pontuacoes.getOrDefault(ocorrencia.getId(), 0f) + pontuacao);
                }
            }
        }

        for (Integer id : pontuacoes.keySet()) {
            Pergunta pergunta = read(id);
            if (pergunta != null && pergunta.getAtiva()) {
                resultado.add(pergunta);
            }
        }
        resultado.sort(Comparator
                .comparingDouble((Pergunta p) -> pontuacoes.get(p.getId())).reversed()
                .thenComparingInt(Pergunta::getId));
        return resultado;
    }

    private void atualizarIndicePalavras(Pergunta antiga, Pergunta nova) throws Exception {
        removerDoIndice(antiga);
        indexarPergunta(nova);
    }

    private void indexarPergunta(Pergunta pergunta) throws Exception {
        ArrayList<String> termos = normalizarTermos(pergunta.getPalavrasChave());
        if (termos.isEmpty()) {
            return;
        }
        float frequencia = 1f / termos.size();
        for (String termo : termos) {
            ElementoLista atual = indicePalavras.read(termo, pergunta.getId());
            if (atual == null) {
                indicePalavras.create(termo, new ElementoLista(pergunta.getId(), frequencia));
            } else {
                indicePalavras.update(termo,
                        new ElementoLista(pergunta.getId(), atual.getFrequencia() + frequencia));
            }
        }
    }

    private void removerDoIndice(Pergunta pergunta) throws Exception {
        for (String termo : new HashSet<>(normalizarTermos(pergunta.getPalavrasChave()))) {
            indicePalavras.delete(termo, pergunta.getId());
        }
    }

    private ArrayList<String> normalizarTermos(String texto) {
        ArrayList<String> termos = new ArrayList<>();
        if (texto == null) {
            return termos;
        }
        String semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
        for (String termo : semAcentos.split("[^\\p{L}\\p{Nd}]+")) {
            if (!termo.isEmpty() && !STOP_WORDS.contains(termo)
                    && !termo.matches("\\d+")) {
                termos.add(termo);
            }
        }
        return termos;
    }

    private void reconstruirIndicePalavras() throws Exception {
        indicePalavras.close();
        java.io.File dicionario = new java.io.File("./dados/Pergunta/indicePalavras.diretorio.db");
        java.io.File blocos = new java.io.File("./dados/Pergunta/indicePalavras.cestos.db");
        if (!dicionario.delete() || !blocos.delete()) {
            throw new Exception("Não foi possível recriar o índice invertido de perguntas.");
        }
        indicePalavras = new ListaInvertida(8, dicionario.getPath(), blocos.getPath());
        for (Pergunta pergunta : readAll()) {
            if (pergunta.getAtiva()) {
                indexarPergunta(pergunta);
                indicePalavras.incrementaEntidades();
            }
        }
    }
}
