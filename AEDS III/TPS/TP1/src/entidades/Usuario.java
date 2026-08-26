package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String email;
    private int hashSenha;
    private int hashRespostaSecreta;
    private String perguntaSecreta;
    

    public Usuario(){
        this(-1, "", "", 0, "", 0);
    }

    public Usuario(int idUsuario, String nome, String email, int hashSenha, String perguntaSecreta, int hashRespostaSecreta){
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.hashSenha = hashSenha;
        this.perguntaSecreta = perguntaSecreta;
        this.hashRespostaSecreta = hashRespostaSecreta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHashSenha() {
        return hashSenha;
    }

    public void setHashSenha(String hashSenha) {
        this.hashSenha = hashSenha.hashCode();
    }

    public String getPerguntaSecreta() {
        return perguntaSecreta;
    }

    public void setPerguntaSecreta(String perguntaSecreta) {
        this.perguntaSecreta = perguntaSecreta;
    }

    public int getHashRespostaSecreta() {
        return hashRespostaSecreta;
    }

    public void setHashRespostaSecreta(String hashRespostaSecreta) {
        String resposataNormalizada = tirarAcentos(hashRespostaSecreta);
        this.hashRespostaSecreta = resposataNormalizada.toLowerCase().trim().hashCode();
    }

    public static String tirarAcentos(String texto) { 
        if (texto == null) { 
            return null; 
        } 
        String normalizado = Normalizer.normalize( texto, Normalizer.Form.NFD ); 
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalizado).replaceAll(""); 
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idUsuario);
        dos.writeUTF(nome);
        dos.writeUTF(email);
        dos.writeInt(hashSenha);
        dos.writeUTF(perguntaSecreta);
        dos.writeInt(hashRespostaSecreta);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        idUsuario = dis.readInt();
        nome = dis.readUTF();
        email = dis.readUTF();
        hashSenha = dis.readInt();
        perguntaSecreta = dis.readUTF();
        hashRespostaSecreta = dis.readInt();
    }

    @Override
    public String toString() {
        return "\nID: " + idUsuario +
               "\nNome: " + nome +
               "\nEmail: " + email +
               "\nPergunta: " + perguntaSecreta;
    }
}
