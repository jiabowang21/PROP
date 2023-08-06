package domini.usuari;

public class Client extends Usuari {
    Client(int id, String username) {
        super(id, username);
    }

    @Override
    public String getTipus() {
        return CtrlUsuari.TYPE_CLIENT;
    }
}
