package domini.usuari;

public class Administrador extends Usuari {
    Administrador(int id, String username) {
        super(id, username);
    }

    @Override
    public String getTipus() {
        return CtrlUsuari.TYPE_ADMIN;
    }
}
