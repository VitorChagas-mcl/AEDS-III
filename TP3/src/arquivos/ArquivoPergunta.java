package arquivos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import aed3.ArvoreBMais;
import aed3.ElementoLista;
import aed3.ListaInvertida;
import entidades.Pergunta;
import entidades.Resposta;
import entidades.Usuario;
import aed3.ParIdUsuarioPergunta;
import aed3.ParIdPerguntaData;

public class ArquivoPergunta extends aed3.Arquivo<Pergunta> {

    private ArvoreBMais<ParIdUsuarioPergunta> IndiceUsuarioPergunta;
    private ArvoreBMais<ParIdPerguntaData> IndicePerguntaData;
    private ListaInvertida indicePalavras;

    private static final Set<String> STOP_WORDS = new HashSet<String>();
    static {
        String palavras = "a ao aos as ate com como da das de del do dos e eh em entre era "
                + "essa esse este eu foi ha isso isto ja la lhe lhes lo mas me mesmo meu meus "
                + "minha minhas muito na nao nas nem no nos num numa o os ou para pelo pelos "
                + "pela pelas por qual quando que quem se sem seu seus sua suas tambem te teu "
                + "teus tua tuas um uma uns umas voce vos ante apos desde durante exceto menos "
                + "salvo sob sobre tras dois tres quatro cinco seis sete oito nove dez cem mil";
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
        if (indicePalavras.numeroEntidades() != perguntasAtivas || !indicePalavrasConsistente()) {
            reconstruirIndicePalavras();
        }
    }

    @Override
    public int create(Pergunta pergunta) throws Exception {
        if (new ArquivoUsuario().read(pergunta.getIdUsuario()) == null) {
            throw new Exception("O usuário autor da pergunta não existe.");
        }

        pergunta.setPalavrasChave(String.join(";", normalizarTermos(pergunta.getPalavrasChave())));
        int id = super.create(pergunta);
        IndiceUsuarioPergunta.create(new ParIdUsuarioPergunta(pergunta.getIdUsuario(), id));
        IndicePerguntaData.create(new ParIdPerguntaData(id, pergunta.getCriacao()));
        if (indexarPergunta(pergunta)) {
            indicePalavras.incrementaEntidades();
        }
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

        PerguntaNova.setPalavrasChave(String.join(";", normalizarTermos(PerguntaNova.getPalavrasChave())));
        if (super.update(PerguntaNova)) {
            if (PerguntaAntigo.getIdUsuario() != PerguntaNova.getIdUsuario()) {
                this.IndiceUsuarioPergunta
                        .delete(new ParIdUsuarioPergunta(PerguntaAntigo.getIdUsuario(), PerguntaAntigo.getId()));
                this.IndiceUsuarioPergunta
                        .create(new ParIdUsuarioPergunta(PerguntaNova.getIdUsuario(), PerguntaNova.getId()));
            }
            if (PerguntaAntigo.getAtiva() && PerguntaNova.getAtiva()) {
                boolean antigaIndexada = !normalizarTermos(PerguntaAntigo.getPalavrasChave()).isEmpty();
                atualizarIndicePalavras(PerguntaAntigo, PerguntaNova);
                boolean novaIndexada = !normalizarTermos(PerguntaNova.getPalavrasChave()).isEmpty();
                if (!antigaIndexada && novaIndexada) {
                    indicePalavras.incrementaEntidades();
                } else if (antigaIndexada && !novaIndexada) {
                    indicePalavras.decrementaEntidades();
                }
            } else if (PerguntaAntigo.getAtiva() && !PerguntaNova.getAtiva()) {
                removerDoIndice(PerguntaAntigo);
                if (!normalizarTermos(PerguntaAntigo.getPalavrasChave()).isEmpty()) {
                    indicePalavras.decrementaEntidades();
                }
            } else if (!PerguntaAntigo.getAtiva() && PerguntaNova.getAtiva()) {
                if (indexarPergunta(PerguntaNova)) {
                    indicePalavras.incrementaEntidades();
                }
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
            if (!normalizarTermos(Pergunta.getPalavrasChave()).isEmpty()) {
                indicePalavras.decrementaEntidades();
            }
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
            double idf = Math.log10((double) entidades / ocorrencias.length) + 1.0;
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

    private boolean indexarPergunta(Pergunta pergunta) throws Exception {
        ArrayList<String> termos = normalizarTermos(pergunta.getPalavrasChave());
        if (termos.isEmpty()) {
            return false;
        }
        Map<String, Integer> frequencias = contarFrequencias(termos);
        int totalTermos = termos.size();
        for (Map.Entry<String, Integer> entrada : frequencias.entrySet()) {
            float frequencia = (float) entrada.getValue() / totalTermos;
            indicePalavras.create(entrada.getKey(), new ElementoLista(pergunta.getId(), frequencia));
        }
        return true;
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
        for (String palavra : texto.split(";")) {
            String termo = Usuario.tirarAcentos(palavra)
                    .trim()
                    .replaceAll("\\s+", " ")
                    .toLowerCase(new Locale("pt", "BR"));
            if (!termo.isEmpty() && !STOP_WORDS.contains(termo)) {
                termos.add(termo);
            }
        }
        return termos;
    }

    private Map<String, Integer> contarFrequencias(ArrayList<String> termos) {
        Map<String, Integer> frequencias = new LinkedHashMap<>();
        for (String termo : termos) {
            frequencias.put(termo, frequencias.getOrDefault(termo, 0));
        }
        return frequencias;
    }

    private boolean indicePalavrasConsistente() throws Exception {
        for (Pergunta pergunta : readAll()) {
            if (!pergunta.getAtiva()) {
                continue;
            }
            ArrayList<String> termos = normalizarTermos(pergunta.getPalavrasChave());
            Map<String, Integer> frequencias = contarFrequencias(termos);
            for (Map.Entry<String, Integer> entrada : frequencias.entrySet()) {
                ElementoLista elemento = indicePalavras.read(entrada.getKey(), pergunta.getId());
                if (elemento == null) {
                    return false;
                }
                float esperado = (float) entrada.getValue() / termos.size();
                if (Math.abs(elemento.getFrequencia() - esperado) > 0.0001f) {
                    return false;
                }
            }
        }
        return true;
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
                if (indexarPergunta(pergunta)) {
                    indicePalavras.incrementaEntidades();
                }
            }
        }
    }
}