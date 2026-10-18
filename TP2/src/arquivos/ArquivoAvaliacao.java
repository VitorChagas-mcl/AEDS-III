package arquivos;

import java.util.ArrayList;

import entidades.Avaliacao;
import entidades.Resposta;
import entidades.Usuario;

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

    public ArrayList<Avaliacao> readByUsuarioAvaliacoes(int idUsuario)
            throws Exception {
        ArrayList<Avaliacao> avaliacoesDoUsuario = new ArrayList<>();
        for (Avaliacao avaliacao : readAll()) {
            if (avaliacao.getIdUsuario() == idUsuario) {
                avaliacoesDoUsuario.add(avaliacao);
            }
        }
        return avaliacoesDoUsuario;
    }

    public ArrayList<Avaliacao> readByResposta(int idResposta) throws Exception {
        ArrayList<Avaliacao> avaliacoesDaResposta = new ArrayList<>();
        for (Avaliacao avaliacao : readAll()) {
            if (avaliacao.getIdResposta() == idResposta) {
                avaliacoesDaResposta.add(avaliacao);
            }
        }
        return avaliacoesDaResposta;
    }

    @Override
    public int create(Avaliacao avaliacao) throws Exception {
        Usuario usuario = new ArquivoUsuario().read(avaliacao.getIdUsuario());
        if (usuario == null) {
            throw new Exception("O usuário da avaliação não existe.");
        }

        Resposta resposta = new ArquivoResposta().read(avaliacao.getIdResposta());
        if (resposta == null || !resposta.isAtiva()) {
            throw new Exception("A resposta avaliada não existe ou está arquivada.");
        }
        if (resposta.getIdUsuario() == avaliacao.getIdUsuario()) {
            throw new Exception("O autor não pode avaliar a própria resposta.");
        }

        if (readByUsuarioResposta(
                avaliacao.getIdUsuario(),
                avaliacao.getIdResposta()) != null) {
            throw new Exception("Este usuário já avaliou esta resposta.");
        }

        int variacaoNota = avaliacao.isAvaliacao() ? 1 : -1;
        ArquivoResposta arquivoResposta = new ArquivoResposta();
        if (!arquivoResposta.ajustarAvaliacao(resposta.getId(), variacaoNota, 1)) {
            throw new Exception("Não foi possível atualizar a nota da resposta.");
        }

        try {
            return super.create(avaliacao);
        } catch (Exception erro) {
            try {
                if (!arquivoResposta.ajustarAvaliacao(resposta.getId(), -variacaoNota, -1)) {
                    throw new Exception("Não foi possível restaurar a nota da resposta.");
                }
            } catch (Exception erroRestauracao) {
                throw new Exception(
                        "Falha ao registrar a avaliação e restaurar a resposta.",
                        erroRestauracao);
            }
            throw erro;
        }
    }

    @Override
    public boolean update(Avaliacao avaliacaoNova) throws Exception {
        Avaliacao avaliacaoAtual = super.read(avaliacaoNova.getId());
        if (avaliacaoAtual == null) {
            return false;
        }

        if (avaliacaoAtual.getIdUsuario() != avaliacaoNova.getIdUsuario()
                || avaliacaoAtual.getIdResposta() != avaliacaoNova.getIdResposta()
                || avaliacaoAtual.isAvaliacao() != avaliacaoNova.isAvaliacao()) {
            throw new Exception("Avaliações não podem ser alteradas; cancele o voto e registre outro.");
        }

        return true;
    }

    @Override
    public boolean delete(int idAvaliacao) throws Exception {
        Avaliacao avaliacao = super.read(idAvaliacao);
        if (avaliacao == null) {
            return false;
        }

        ArquivoResposta arquivoResposta = new ArquivoResposta();
        Resposta resposta = arquivoResposta.read(avaliacao.getIdResposta());
        if (resposta == null) {
            return super.delete(idAvaliacao);
        }

        if (resposta.getAvaliacoes() <= 0) {
            throw new IllegalStateException("A quantidade de avaliações da resposta é inválida.");
        }
        int variacaoNota = avaliacao.isAvaliacao() ? -1 : 1;
        if (!arquivoResposta.ajustarAvaliacao(resposta.getId(), variacaoNota, -1)) {
            throw new Exception("Não foi possível ajustar a nota da resposta.");
        }

        try {
            if (!super.delete(idAvaliacao)) {
                throw new Exception("Não foi possível remover a avaliação.");
            }
            return true;
        } catch (Exception erro) {
            try {
                if (!arquivoResposta.ajustarAvaliacao(resposta.getId(), -variacaoNota, 1)) {
                    throw new Exception("Não foi possível restaurar a avaliação da resposta.");
                }
            } catch (Exception erroRestauracao) {
                throw new Exception(
                        "Falha ao remover a avaliação e restaurar a nota e a quantidade.",
                        erroRestauracao);
            }
            throw erro;
        }
    }
}