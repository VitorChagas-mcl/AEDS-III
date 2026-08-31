package arquivos;
import entidades.Usuario;

public class ArquivoUsuario extends aed3.Arquivo<Usuario> {
    
    public ArquivoUsuario() throws Exception  {
        super("Usuario", Usuario.class.getConstructor());
    }
}
