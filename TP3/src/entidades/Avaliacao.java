package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aed3.InterfaceRegistro;

public class Avaliacao implements InterfaceRegistro {
    private int idAvaliacao;
    private int idUsuario;
    private int idResposta;
    private boolean avaliacao;

    public Avaliacao() {
        this(-1, -1, -1, false);
    }

    public Avaliacao(int idUsuario, int idResposta, boolean avaliacao) {
        this(-1, idUsuario, idResposta, avaliacao);
    }

    public Avaliacao(int idAvaliacao, int idUsuario, int idResposta, boolean avaliacao) {
        this.idAvaliacao = idAvaliacao;
        this.idUsuario = idUsuario;
        this.idResposta = idResposta;
        this.avaliacao = avaliacao;
    }

    @Override
    public int getId() {
        return idAvaliacao;
    }

    @Override
    public void setId(int id) {
        idAvaliacao = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
    }

    public boolean isAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(boolean avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idAvaliacao);
        dos.writeInt(idUsuario);
        dos.writeInt(idResposta);
        dos.writeBoolean(avaliacao);

        return baos.toByteArray();
    }

    @Override
    public void deserialize(byte[] dados) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(dados);
        DataInputStream dis = new DataInputStream(bais);

        idAvaliacao = dis.readInt();
        idUsuario = dis.readInt();
        idResposta = dis.readInt();
        avaliacao = dis.readBoolean();
    }
}