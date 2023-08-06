package domini.usuari;

/**
 * Classe que hereda d'Usuari i representa un administrador del sistema
 *
 * @author Hector Godoy Creus
 */
public class Client extends Usuari {
    Client(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getTipus() {
        return CtrlUsuari.TYPE_CLIENT;
    }
}
