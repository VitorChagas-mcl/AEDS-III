package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import aed3.InterfaceRegistro;

public class Resposta implements InterfaceRegistro {
    private int idResposta;
    private int idPergunta;
    private int idUsuario;
    private long criacao;
    private String resposta;
    private short nota;
    private short avaliacoes;
    private boolean ativa;

    public Resposta() {
        this.ativa = true;
        this.nota = 0;
        this.avaliacoes = 0;
    }

    public Resposta(int idResposta, int idPergunta, int idUsuario, long criacao,
                    String resposta, short nota, short avaliacoes, boolean ativa) {
        this.idResposta = idResposta;
        this.idPergunta = idPergunta;
        this.idUsuario = idUsuario;
        this.criacao = criacao;
        this.resposta = resposta;
        this.nota = nota;
        this.avaliacoes = avaliacoes;
        this.ativa = ativa;
    }

    @Override
    public int getId() {
        return idResposta;
    }

    @Override
    public void setId(int idResposta) {
        this.idResposta = idResposta;
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public long getCriacao() {
        return criacao;
    }

    public void setCriacao(long criacao) {
        this.criacao = criacao;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public short getNota() {
        return nota;
    }

    public void setNota(short nota) {
        this.nota = nota;
    }

    public short getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(short avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idResposta);
        dos.writeInt(idPergunta);
        dos.writeInt(idUsuario);
        dos.writeLong(criacao);
        dos.writeUTF(resposta);
        dos.writeShort(nota);
        dos.writeShort(avaliacoes);
        dos.writeBoolean(ativa);

        return baos.toByteArray();
    }

    @Override
    public void deserialize(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        idResposta = dis.readInt();
        idPergunta = dis.readInt();
        idUsuario = dis.readInt();
        criacao = dis.readLong();
        resposta = dis.readUTF();
        nota = dis.readShort();
        avaliacoes = dis.readShort();
        ativa = dis.readBoolean();
    }

    @Override
    public String toString() {
        return "\nID Resposta: " + idResposta +
            "\nID Pergunta: " + idPergunta +
            "\nID Usuário: " + idUsuario +
            "\nCriação: " + criacao +
            "\nResposta: " + resposta +
            "\nNota: " + nota +
            "\nAvaliações: " + avaliacoes +
            "\nAtiva: " + ativa;
    }
}