package domini.recomanacio;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Pau Antonio Soler
 */

public class CtrlRecomanacio {
    private static CtrlRecomanacio instance;
    private final HashMap<Integer, ConjuntRecomanacions> llistaConjuntRecomanacions = new HashMap<>();

    private CtrlRecomanacio() {
    }

    public static CtrlRecomanacio getInstance() {
        if (instance == null) instance = new CtrlRecomanacio();
        return instance;
    }

    public void obtenirRecomanacions(String estrategia, Integer userID) throws Exception {
        Date data = new Date();
        ConjuntRecomanacions c = new ConjuntRecomanacions(userID, data, estrategia);
        c.obtenirRecomanacions();
        if (llistaConjuntRecomanacions.containsKey(userID)) {
            llistaConjuntRecomanacions.replace(userID, c);
        } else llistaConjuntRecomanacions.put(userID, c);
    }

    public void imprimirRecomanacions(Integer userID) throws RecomanacionsException {
        if (llistaConjuntRecomanacions.containsKey(userID)) {
            llistaConjuntRecomanacions.get(userID).imprimirRecomanacions();
        } else {
            throw new RecomanacionsException("L'usuari indicat no té recomanacions.");
        }
    }

    public Double avaluarQualitat(Integer userID, List<List<String>> test) throws RecomanacionsException {
        if (!llistaConjuntRecomanacions.containsKey(userID)) {
            throw new RecomanacionsException("L'usuari indicat no té recomanacions.");
        }
        ConjuntRecomanacions recomanacioUsuari = llistaConjuntRecomanacions.get(userID);
        recomanacioUsuari.carregaTest(test);
        return recomanacioUsuari.avaluarQualitat();
    }
}