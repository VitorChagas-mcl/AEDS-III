package entidades;

import java.io.DataOutputStream;

import aed3.InterfaceRegistro;

public class Pergunta implements InterfaceRegistro{
    private int idPergunta;
    private int idUsuario;
    private long criacao; //atributo para salvar a data e hora da criação da pergunta em milissegundos
    private long alteracao; //mesma coisa de cima, porem para alteração da mesma
    private short nota; //nota da pergunta(soma das notas dadas por outros usuarios) pode ser negativa 
    private String pergunta;
    private String palavrasChave; //lista de termos separados por ";", usados para busca
    private boolean ativa; //perguntas arquivadas tem que ter esse atributo como false(considerando que elas não podem ser excluidas, apenas arquivadas)

    public Pergunta(){
        this(-1,-1, 0, 0, 0, "", "", false);
    }

    public Pergunta(int idPergunta, int idUsuario, long criacao, long alteracao, short nota, String pergunta, String palavrasChave, boolean ativa){
        this.idPergunta = idPergunta;
        this.idUsuario = idUsuario;
        this.criacao = criacao;
        this.alteracao = alteracao;
        this.nota = nota;
        this.pergunta = pergunta;
        this.palavrasChave = palavrasChave;
        this.ativa = ativa;
    }

    @Override
    public int getId() {
        return idPergunta;
    }
    @Override
    public void setId(int idPergunta) {
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

    public long getAlteracao() {
        return alteracao;
    }

    public void setAlteracao(long alteracao) {
        this.alteracao = alteracao;
    }

    public short getNota() {
        return nota;
    }

    public void setNota(short nota) {
        this.nota = nota;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getPalavrasChave() {
        return palavrasChave;
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    public boolean getAtiva(){
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idPergunta);
        dos.writeInt(idUsuario);
        dos.writeLong(criacao);
        dos.writeLong(alteracao);
        dos.writeShort(nota);
        dos.writeUTF(pergunta);
        dos.writeUTF(palavrasChave);
        dos.writeBoolean(ativa);
        return baos.toByteArray();
    }

    @Override
    public void deserialize(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        idPergunta = dis.readInt();
        idUsuario = dis.readInt();
        criacao = dis.readLong();
        alteracao = dis.readLong();
        nota = dis.readShort();
        pergunta = dis.readUTF();
        palavrasChave = dis.readUTF();
        ativa = dis.readBoolean();
    }

    @Override
    public String toString() { //Junto às perguntas, precisaremos imprimir um num relacionado, já que elas serão exibidas sequencialmente
        return " " + ativa + 
               "\n" + criacao +
               "\n" + pergunta +
               "\nPalavras chave: " + palavrasChave;
    }
}