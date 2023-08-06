package persistencia;

import utils.InOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Stub de la classe CtrlPersistencia, per provar les classes d'usuari i
 * les seves controladores.
 *
 * @author Hector Godoy Creus
 */
public class CtrlPersistenciaStub {
    private static CtrlPersistenciaStub instance;

    private CtrlPersistenciaStub() {
    }

    public static CtrlPersistenciaStub getInstance() {
        if (instance == null) instance = new CtrlPersistenciaStub();
        return instance;
    }

    public void writeUsuari(List<String> usuari) {
        try {
            new InOut().writeln("[STUB, no s'ha registrat] Escrit usuari amb id " + usuari.get(0) + ", tipus " + usuari.get(1)
                    + ", username " + usuari.get(2) + ", contrasenya " + usuari.get(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> readUsuaris() {
        List<List<String>> usuaris = new ArrayList<>();
        usuaris.add(new ArrayList<>() {{
            add("21");
            add("type_client");
            add("dei");
            add("viscaElBarça");
        }});
        return usuaris;
    }
}
