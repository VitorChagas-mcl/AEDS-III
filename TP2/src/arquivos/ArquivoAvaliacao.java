package arquivos;

import entidades.Avaliacao;

public class ArquivoAvaliacao extends aed3.Arquivo<Avaliacao> {

    public ArquivoAvaliacao() throws Exception {
        super("Avaliacao", Avaliacao.class.getConstructor());
    }

    public Avaliacao readByUsuarioResposta(int idUsuario, int idResposta)
            throws Exception {
        for (Avaliacao avaliacao : readAll()) {
            if (avaliacao.getIdUsuario() == idUsuario
                    && avaliacao.getIdResposta() == idResposta) {
                return avaliacao;
            }
        }
        return null;
    }

    @Override
    public int create(Avaliacao avaliacao) throws Exception {
        if (readByUsuarioResposta(
                avaliacao.getIdUsuario(),
                avaliacao.getIdResposta()) != null) {
            throw new Exception("Este usuário já avaliou esta resposta.");
        }

        return super.create(avaliacao);
    }
}