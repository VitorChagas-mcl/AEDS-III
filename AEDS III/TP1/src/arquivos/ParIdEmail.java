package arquivos;

import java.io.*;

import aed3.InterfaceHashExtensivel;

public class ParIdEmail implements InterfaceHashExtensivel {
    private int id;
    private String email;
    private short TAMANHO = (short)(100 + 4);
    
    public ParIdEmail() {
        this.id = -1;
        this.email = "";
    }

    public ParIdEmail(int id, String email) {
        if(!(email.contains("@") && email.contains("."))){
            throw new IllegalArgumentException("Email inválido.");
        }
        this.id = id;
        this.email = email;
    }
    public int getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public int hashCode(){
        return hash(this.email);
    }
    
    @Override
    public short size() {
        return this.TAMANHO;
    }
    
    @Override
    public String toString() {
        return "("+this.id + ";" + this.email+")";
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.email);
        return baos.toByteArray();
    }

    public void deserialize(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.email = dis.readUTF();
    }

    public static int hash(String email) {
        if (email == null || email.isEmpty()) return 0;
        long h = 0;
        for (char c : email.toLowerCase().trim().toCharArray())
            h = h * 31 + c;
        return (int) Math.abs(h % (int)(1e9 + 7));
    }
}