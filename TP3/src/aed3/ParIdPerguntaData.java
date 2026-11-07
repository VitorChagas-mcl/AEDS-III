package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIdPerguntaData implements InterfaceArvoreBMais<ParIdPerguntaData>{

    private int idPergunta;
    private long data;
    private short TAMANHO = 12; // 4 bytes + 8 bytes

    public ParIdPerguntaData() {
        this(-1, -1);
    }

    public ParIdPerguntaData(int idPergunta, long data) {
        this.idPergunta = idPergunta;
        this.data = data;
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public long getData() {
        return data;
    }

    @Override
    public ParIdPerguntaData clone() {
        return new ParIdPerguntaData(idPergunta, data);
    }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public int compareTo(ParIdPerguntaData obj) {
        int comparacaoData = Long.compare(obj.data, this.data);
        if (comparacaoData != 0)
            return comparacaoData;
        return Integer.compare(obj.idPergunta, this.idPergunta);
    }

    @Override
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idPergunta);
        dos.writeLong(data);
        return baos.toByteArray();
    }

    @Override
    public void deserialize(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        idPergunta = dis.readInt();
        data = dis.readLong();
    }

    @Override 
    public String toString() {
        return "(" + this.idPergunta + ";" + this.data + ")";
    }
}
