package arquivos;

import java.util.ArrayList;

import aed3.ArvoreBMais;
import aed3.ParIdId;
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
    
    @Override
    public boolean update(Resposta respostaNova) throws Exception {
       Resposta respostaAntiga = super.read(respostaNova.getId());
        if (respostaAntiga == null)
            return false;
        if (respostaAntiga.isAtiva() == false)
            return false;

        if (super.update(respostaNova)) {
            if (respostaAntiga.getIdPergunta() != respostaNova.getIdPergunta()) {
                this.indicePerguntaResposta
                        .delete(new ParIdId(respostaAntiga.getIdPergunta(), respostaAntiga.getId()));
                this.indicePerguntaResposta
                        .create(new ParIdId(respostaNova.getIdPergunta(), respostaNova.getId()));
            }
            if (respostaAntiga.getIdUsuario() != respostaNova.getIdUsuario()) {
                this.indiceUsuarioResposta
                        .delete(new ParIdId(respostaAntiga.getIdUsuario(), respostaAntiga.getId()));
                this.indiceUsuarioResposta
                        .create(new ParIdId(respostaNova.getIdUsuario(), respostaNova.getId()));
            }
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
        if(super.delete(idResposta)){
            indicePerguntaResposta.delete(new ParIdId(resposta.getIdPergunta(), resposta.getId()));
            indiceUsuarioResposta.delete(new ParIdId(resposta.getIdUsuario(), resposta.getId()));
            return true;
        }
        return false;
    }
}