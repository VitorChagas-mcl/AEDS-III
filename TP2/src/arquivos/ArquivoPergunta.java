package arquivos;

import java.util.ArrayList;

import aed3.ArvoreBMais;
import entidades.Pergunta;
import aed3.ParIdUsuarioPergunta;

public class ArquivoPergunta extends aed3.Arquivo<Pergunta> {

    private ArvoreBMais<ParIdUsuarioPergunta> IndiceUsuarioPergunta;

    public ArquivoPergunta() throws Exception {
        super("Pergunta", Pergunta.class.getConstructor());
        IndiceUsuarioPergunta = new ArvoreBMais<ParIdUsuarioPergunta>(ParIdUsuarioPergunta.class.getConstructor(),
                5, "./dados/Pergunta/indicePergunta.diretorio.db");
    }

    @Override
    public int create(Pergunta pergunta) throws Exception {
        int id = super.create(pergunta);
        IndiceUsuarioPergunta.create(new ParIdUsuarioPergunta(pergunta.getIdUsuario(), id));
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

    @Override
    public boolean update(Pergunta PerguntaNova) throws Exception {
        Pergunta PerguntaAntigo = super.read(PerguntaNova.getId());
        if (PerguntaAntigo == null)
            return false;
        if (PerguntaAntigo.getAtiva() == false)
            return false;

        if (super.update(PerguntaNova)) {
            if (PerguntaAntigo.getIdUsuario() != PerguntaNova.getIdUsuario()) {
                this.IndiceUsuarioPergunta
                        .delete(new ParIdUsuarioPergunta(PerguntaAntigo.getIdUsuario(), PerguntaAntigo.getId()));
                this.IndiceUsuarioPergunta
                        .create(new ParIdUsuarioPergunta(PerguntaNova.getIdUsuario(), PerguntaNova.getId()));
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
        if (super.delete(idPergunta)) {
            this.IndiceUsuarioPergunta.delete(new ParIdUsuarioPergunta(Pergunta.getIdUsuario(), idPergunta));
            Pergunta.setAtiva(false);
            return true;
        }

        return false;
    }
}
