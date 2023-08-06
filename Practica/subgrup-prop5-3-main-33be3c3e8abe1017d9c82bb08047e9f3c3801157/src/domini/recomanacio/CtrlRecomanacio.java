package domini.recomanacio;

import persistencia.CtrlPersistencia;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * @author Pau Antonio Soler
 */

public class CtrlRecomanacio {
    public static final String ESTRATEGIA_COLLABORATIVE = "collaborative";
    public static final String ESTRATEGIA_CONTENT_BASED = "contentBased";
    public static final String ESTRATEGIA_HIBRID = "hibrid";
    private List<List<String>> test;

    //instancia del controlador i llistes dels conjunts d'items obtinguts
    private static CtrlRecomanacio instance;
    private final HashMap<Integer, ConjuntRecomanacions> llistaConjuntRecomanacionsContent = new HashMap<>();
    private final HashMap<Integer, ConjuntRecomanacions> llistaConjuntRecomanacionsCollab = new HashMap<>();
    private final HashMap<Integer, ConjuntRecomanacions> llistaConjuntRecomanacionsHibrid = new HashMap<>();


    private CtrlRecomanacio() {
    }

    // consultora que retorna la instancia del controlador
    public static CtrlRecomanacio getInstance() {
        if (instance == null) instance = new CtrlRecomanacio();
        return instance;
    }

    // consultora que retorna les recomanacions obtingudes amb l'estrategia de content based de l'usuari amb l'identificador userId
    public ArrayList<Recomanacio> getLlistaConjuntRecomanacionsContent(int userId) throws RecomanacionsException {
        if (llistaConjuntRecomanacionsContent.containsKey(userId))
            return llistaConjuntRecomanacionsContent.get(userId).getCjtRecomanacions();
        else throw new RecomanacionsException("L'usuari indicat no té recomanacions Content Based.");
    }

    // consultora que retorna les recomanacions obtingudes amb l'estrategia de collaborative de l'usuari amb l'identificador userId
    public ArrayList<Recomanacio> getLlistaConjuntRecomanacionsCollab(int userId) throws RecomanacionsException {
        if (llistaConjuntRecomanacionsCollab.containsKey(userId))
            return llistaConjuntRecomanacionsCollab.get(userId).getCjtRecomanacions();
        else throw new RecomanacionsException("L'usuari indicat no té recomanacions Collaborative Filtering.");
    }

    // consultora que retorna les recomanacions obtingudes amb l'estrategia hibrida de l'usuari amb l'identificador userId
    public ArrayList<Recomanacio> getLlistaConjuntRecomanacionsHibrid(int userId) throws RecomanacionsException {
        if (llistaConjuntRecomanacionsHibrid.containsKey(userId))
            return llistaConjuntRecomanacionsHibrid.get(userId).getCjtRecomanacions();
        else throw new RecomanacionsException("L'usuari indicat no té recomanacions híbrides.");
    }

    // operacio que crea un nou conjunt de recomanacions i n'obte recomanacions segons l'estrategia i usuari indicats
    public void obtenirRecomanacions(String estrategia, Integer userID) throws Exception {
        Timestamp data = new Timestamp(System.currentTimeMillis());
        ConjuntRecomanacions c = new ConjuntRecomanacions(userID, data, estrategia);
        c.obtenirRecomanacions();
        switch (estrategia) {
            case ESTRATEGIA_COLLABORATIVE:
                if (llistaConjuntRecomanacionsCollab.containsKey(userID)) {
                    llistaConjuntRecomanacionsCollab.replace(userID, c);
                } else llistaConjuntRecomanacionsCollab.put(userID, c);
                break;
            case ESTRATEGIA_CONTENT_BASED:
                if (llistaConjuntRecomanacionsContent.containsKey(userID)) {
                    llistaConjuntRecomanacionsContent.replace(userID, c);
                } else llistaConjuntRecomanacionsContent.put(userID, c);
                break;
            case ESTRATEGIA_HIBRID:
                if (llistaConjuntRecomanacionsHibrid.containsKey(userID)) {
                    llistaConjuntRecomanacionsHibrid.replace(userID, c);
                } else llistaConjuntRecomanacionsHibrid.put(userID, c);
                break;
        }
    }

    // imprimeix les recomanacions que te l'usuari indicat
    public void imprimirRecomanacions(Integer userID) throws RecomanacionsException {
        if (llistaConjuntRecomanacionsContent.containsKey(userID)) {
            llistaConjuntRecomanacionsContent.get(userID).imprimirRecomanacions();
        } else {
            throw new RecomanacionsException("L'usuari indicat no té recomanacions.");
        }
        if (llistaConjuntRecomanacionsCollab.containsKey(userID)) {
            llistaConjuntRecomanacionsCollab.get(userID).imprimirRecomanacions();
        } else {
            throw new RecomanacionsException("L'usuari indicat no té recomanacions.");
        }
        if (llistaConjuntRecomanacionsHibrid.containsKey(userID)) {
            llistaConjuntRecomanacionsHibrid.get(userID).imprimirRecomanacions();
        } else {
            throw new RecomanacionsException("L'usuari indicat no té recomanacions.");
        }
    }

    // operacio que avalua la qualitat de les recomanacions obtingudes previament per l'usuari i en calcula la mitjana
    public Double avaluarQualitat(Integer userID) throws RecomanacionsException {
        if (!llistaConjuntRecomanacionsContent.containsKey(userID) &&
                !llistaConjuntRecomanacionsCollab.containsKey(userID) &&
                !llistaConjuntRecomanacionsHibrid.containsKey(userID)) {
            throw new RecomanacionsException("L'usuari indicat no té recomanacions.");
        }
        Double sumaAvaluacions = 0.0;
        Double conjunts = 0.0;

        if (llistaConjuntRecomanacionsContent.containsKey(userID)) {
            ConjuntRecomanacions recomanacioUsuari = llistaConjuntRecomanacionsContent.get(userID);
            recomanacioUsuari.carregaTest(test);
            sumaAvaluacions += recomanacioUsuari.avaluarQualitat();
            conjunts += 1;
        }

        if (llistaConjuntRecomanacionsCollab.containsKey(userID)) {
            ConjuntRecomanacions recomanacioUsuari = llistaConjuntRecomanacionsCollab.get(userID);
            recomanacioUsuari.carregaTest(test);
            sumaAvaluacions += recomanacioUsuari.avaluarQualitat();
            conjunts += 1;
        }

        if (llistaConjuntRecomanacionsHibrid.containsKey(userID)) {
            ConjuntRecomanacions recomanacioUsuari = llistaConjuntRecomanacionsHibrid.get(userID);
            recomanacioUsuari.carregaTest(test);
            sumaAvaluacions += recomanacioUsuari.avaluarQualitat();
            conjunts += 1;
        }

        return sumaAvaluacions/conjunts;
    }

    // operacio que invoca a les operacions necessaries per a escriure les recomanacions al fitxer de dades
    public void escriureRecomanacions() {
        List<List<String>> recomanacionsToWrite = new LinkedList<>();
        serialitzarRecomanacions(recomanacionsToWrite, llistaConjuntRecomanacionsContent);
        serialitzarRecomanacions(recomanacionsToWrite, llistaConjuntRecomanacionsCollab);
        serialitzarRecomanacions(recomanacionsToWrite, llistaConjuntRecomanacionsHibrid);
        CtrlPersistencia ctrlPersistencia = CtrlPersistencia.getInstance();
        ctrlPersistencia.writeRecomanacio(recomanacionsToWrite);
    }

    // operacio que serialitza les recomanacions per a poder ser guardades en un fitxer
    private void serialitzarRecomanacions(List<List<String>> recomanacionsToWrite, HashMap<Integer, ConjuntRecomanacions> llistaConjuntRecomanacionsContent) {
        for(ConjuntRecomanacions cr : llistaConjuntRecomanacionsContent.values()) {
            List<String> recomanacions = new LinkedList<>();
            recomanacions.add(cr.getUserID().toString());
            recomanacions.add(String.valueOf(cr.getData().getTime()));
            recomanacions.add(cr.getEstrategia());

            ArrayList<Recomanacio> cjtRecomanacions = cr.getCjtRecomanacions();
            for (Recomanacio r : cjtRecomanacions) {
                recomanacions.add(r.getItemID().toString());
                recomanacions.add(r.getValoracio().toString());
            }
            recomanacionsToWrite.add(recomanacions);
        }
    }

    // operacio que guarda l'historic de recomanacions passat per parametres a les llistes de recomanacions
    public void carregaRecomanacions(List<List<String>> historicRecomanacions) throws ParseException {
        if (!historicRecomanacions.isEmpty()) {
            llistaConjuntRecomanacionsContent.clear();
            llistaConjuntRecomanacionsCollab.clear();
            llistaConjuntRecomanacionsHibrid.clear();
            for (List<String> rec : historicRecomanacions) {
                Integer userID = Integer.parseInt(rec.get(0));
                Date date = new Date(Long.getLong(rec.get(1)));
                String estrategia = rec.get(2);

                ConjuntRecomanacions cjtRecomanacions = new ConjuntRecomanacions(userID, date, estrategia);
                cjtRecomanacions.carregaRecomanacions(rec.subList(3, rec.size()));

                switch (estrategia) {
                    case ESTRATEGIA_COLLABORATIVE:
                        llistaConjuntRecomanacionsCollab.put(userID, cjtRecomanacions);
                        break;
                    case ESTRATEGIA_CONTENT_BASED:
                        llistaConjuntRecomanacionsContent.put(userID, cjtRecomanacions);
                        break;
                    case ESTRATEGIA_HIBRID:
                        llistaConjuntRecomanacionsHibrid.put(userID, cjtRecomanacions);
                        break;
                }
            }
        }
    }

    // operacio que carrega el fitxer de test necessari per a avaluar les recomanacions
    public void carregaTest(List<List<String>> testRecomanacions) {
        test = testRecomanacions;
    }
}