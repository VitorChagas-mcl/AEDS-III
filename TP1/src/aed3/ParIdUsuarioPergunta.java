package aed3;

import java.io.*;

// Par (idUsuario, idPergunta) para o índice B+ de inscrições por usuário.

public class ParIdUsuarioPergunta implements InterfaceArvoreBMais<ParIdUsuarioPergunta>, Comparable<ParIdUsuarioPergunta> {

    private int idUsuario;
    private int idPergunta;
    private short TAMANHO = 8; // 4 bytes + 4 bytes

    public ParIdUsuarioPergunta() {
        this(-1, -1);
    }

    public ParIdUsuarioPergunta(int idUsuario, int idPergunta) {
        this.idUsuario   = idUsuario;
        this.idPergunta = idPergunta;
    }

    public int getIdUsuario()   { return idUsuario; }
    public int getIdPergunta() { return idPergunta; }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idUsuario);
        dos.writeInt(idPergunta);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        idUsuario   = dis.readInt();
        idPergunta = dis.readInt();
    }

    // Ordena por idUsuario; desempata por idPergunta
    @Override
    public int compareTo(ParUsuarioIdPerguntaId obj) {
        if (this.idUsuario != obj.idUsuario)
            return Integer.compare(this.idUsuario, obj.idUsuario);
        return Integer.compare(this.idPergunta, obj.idPergunta);
    }

    @Override
    public ParUsuarioIdPerguntaId clone() {
        return new ParUsuarioIdPerguntaId(this.idUsuario, this.idPergunta);
    }

    @Override
    public String toString() {
        return "Usuario: " + idUsuario + " | Inscrição: " + idPergunta;
    }
}