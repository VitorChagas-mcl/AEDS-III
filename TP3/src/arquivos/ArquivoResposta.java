package arquivos;

import java.util.ArrayList;

import aed3.ArvoreBMais;
import aed3.ParIdId;
import entidades.Avaliacao;
import entidades.Pergunta;
import entidades.Resposta;

public class ArquivoResposta extends aed3.Arquivo<Resposta> {

    private ArvoreBMais<ParIdId> indicePerguntaResposta;
    private ArvoreBMais<ParIdId> indiceUsuarioResposta;

    public ArquivoResposta() throws Exception {
        super("Resposta", Resposta.class.getConstructor());
        indicePerguntaResposta = new ArvoreBMais<ParIdId>(ParIdId.class.getConstructor(), 5,
                "./dados/Resposta/indicePerguntaResposta.diretorio.db");
        indiceUsuarioResposta = new ArvoreBMais<ParIdId>(ParIdId.class.getConstructor(), 5,
                "./dados/Resposta/indiceUsuarioResposta.diretorio.db");
        
    }

    @Override 
    public int create(Resposta resposta) throws Exception {
        Pergunta pergunta = new ArquivoPergunta().read(resposta.getIdPergunta());
        if (pergunta == null || !pergunta.getAtiva()) {
            throw new Exception("A pergunta da resposta não existe ou está arquivada.");
        }
        if (new ArquivoUsuario().read(resposta.getIdUsuario()) == null) {
            throw new Exception("O usuário autor da resposta não existe.");
        }

        int id = super.create(resposta);
        indicePerguntaResposta.create(new ParIdId(resposta.getIdPergunta(), id));
        indiceUsuarioResposta.create(new ParIdId(resposta.getIdUsuario(), id));
        return id;
    }


    public ArrayList<Resposta>readByPerguntaRespostas(int idPergunta) throws Exception {
        ArrayList<Resposta> respostas = new ArrayList<>();
        ArrayList<ParIdId> respostasIndice = indicePerguntaResposta.read(null);
        for (ParIdId par : respostasIndice) {
            if (par.getId1() == idPergunta) {
                Resposta resposta = super.read(par.getId2());
                if (resposta != null) {
                    respostas.add(resposta);
                }
            }
        }
        return respostas;
    }

    public ArrayList<Resposta>readByUsuarioRespostas(int idUsuario) throws Exception {
        ArrayList<Resposta> respostas = new ArrayList<>();
        ArrayList<ParIdId> respostasIndice = indiceUsuarioResposta.read(null);
        for (ParIdId par : respostasIndice) {
            if (par.getId1() == idUsuario) {
                Resposta resposta = super.read(par.getId2());
                if (resposta != null) {
                    respostas.add(resposta);
                }
            }
        }
        return respostas;
    }

    public boolean ajustarAvaliacao(int idResposta, int variacaoNota, int variacaoAvaliacoes)
            throws Exception {
        Resposta resposta = super.read(idResposta);
        if (resposta == null) {
            return false;
        }

        int novaNota = resposta.getNota() + variacaoNota;
        int novaQuantidade = resposta.getAvaliacoes() + variacaoAvaliacoes;
        if (novaNota < Short.MIN_VALUE || novaNota > Short.MAX_VALUE
                || novaQuantidade < 0 || novaQuantidade > Short.MAX_VALUE) {
            throw new IllegalStateException("A nota ou a quantidade de avaliações está fora do limite permitido.");
        }

        resposta.setNota((short) novaNota);
        resposta.setAvaliacoes((short) novaQuantidade);
        return super.update(resposta);
    }
    
    @Override
    public boolean update(Resposta respostaNova) throws Exception {
       Resposta respostaAntiga = super.read(respostaNova.getId());
        if (respostaAntiga == null)
            return false;
        if (respostaAntiga.isAtiva() == false)
            return false;

        if (respostaAntiga.getIdPergunta() != respostaNova.getIdPergunta()
                || respostaAntiga.getIdUsuario() != respostaNova.getIdUsuario()) {
            throw new Exception("Não é permitido alterar a pergunta ou o autor de uma resposta.");
        }

        if (super.update(respostaNova)) {
            return true;
        }

        return false;
    }
    
    @Override 
    public boolean delete(int idResposta) throws Exception {
        if(super.read(idResposta) == null){
            System.out.println("Resposta não encontrada.");
            return false;
        }
        Resposta resposta = super.read(idResposta);
        ArquivoAvaliacao arquivoAvaliacao = new ArquivoAvaliacao();
        ArrayList<Avaliacao> avaliacoes = arquivoAvaliacao.readByResposta(idResposta);
        for (Avaliacao avaliacao : avaliacoes) {
            if (!arquivoAvaliacao.delete(avaliacao.getId())) {
                throw new Exception("Não foi possível excluir uma avaliação relacionada à resposta.");
            }
        }

        if (super.delete(idResposta)) {
            indicePerguntaResposta.delete(new ParIdId(resposta.getIdPergunta(), idResposta));
            indiceUsuarioResposta.delete(new ParIdId(resposta.getIdUsuario(), idResposta));
            return true;
        }
        return false;
    }
}