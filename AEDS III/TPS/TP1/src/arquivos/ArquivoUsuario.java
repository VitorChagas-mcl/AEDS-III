package arquivos;
import aed3.HashExtensivel;
import entidades.Usuario;

public class ArquivoUsuario extends aed3.Arquivo<Usuario> {

    HashExtensivel<ParIdEmail> hashEmail;

    public ArquivoUsuario() throws Exception  {
        super("Usuario", Usuario.class.getConstructor());
        this.hashEmail = new HashExtensivel<>(ParIdEmail.class.getConstructor(), 4, "./dados/Usuario/indiceID.diretorio.db", "./dados/Usuario/indiceID.cestos.db");
    }

    public int create(Usuario usuario) throws Exception {
        if(this.readByEmail(usuario.getEmail()) != null)
            throw new Exception("Email já cadastrado.");
        int id = super.create(usuario);
        hashEmail.create(new ParIdEmail(id, usuario.getEmail()));
        return id;
    }

    public Usuario readByEmail(String email) throws Exception{
        ParIdEmail par = hashEmail.read(ParIdEmail.hash(email));
        if(par == null) return null;
        return super.read(par.getId()); 
    }
}
