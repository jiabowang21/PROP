package domini.recomanacio;

import domini.estrategiarecomanacio.EstrategiaRecomanacio;
import domini.item.CtrlItems;
import domini.item.Item;
import domini.valoracio.CtrlValoracio;
import domini.valoracio.Valoracio;
import persistencia.CtrlPersistencia;

import java.util.*;

/**
 * @author Pau Antonio Soler
 */

public class ConjuntRecomanacions {
    // identificador de l'usuari a qui es recomanen els items
    private final Integer userID;
    // data en que es realitza la recomanacio
    private final Date data;
    // estrategia utilitzada per a obtenir les recomanacions
    private final String estrategia;

    // llista de recomanacions obtingudes
    private final ArrayList<Recomanacio> CjtRecomanacions = new ArrayList<>();
    // llista de recomanacions de test, utilitzades per a avaluar la qualitat de les recomanacions obtingudes
    private final Map<Integer, ArrayList<Double>> test = new HashMap<>();

    // constructora del conjunt de recomanacions
    public ConjuntRecomanacions(Integer userID, Date data, String estrategia) {
        this.userID = userID;
        this.data = data;
        this.estrategia = estrategia;
    }

    // consultora que retorna el llistat d'items recomanats
    protected ArrayList<Recomanacio> getCjtRecomanacions() {
        return CjtRecomanacions;
    }

    // operacio que permet obtenir les recomanacions, a traves de la estrategia definida a la creadora
    public void obtenirRecomanacions() throws Exception {
        CtrlValoracio ctrlVal = CtrlValoracio.getInstance();
        HashMap<Integer, Set<Valoracio>> cjtVal = ctrlVal.getValoracions();

        CtrlItems ctrlIt = CtrlItems.getInstance();
        if (ctrlIt.mida() == 0) {
            CtrlPersistencia ctrlP = CtrlPersistencia.getInstance();
            List<List<String>> items = ctrlP.readItems();
            ctrlIt.carregarConjuntItems(items);
        }
        ArrayList<Item> cjtItems = ctrlIt.getItems();

        EstrategiaRecomanacio er = new EstrategiaRecomanacio(cjtVal, cjtItems, userID);
        Map<Integer, Double> rec = new HashMap<>();

        switch (estrategia) {
            case CtrlRecomanacio.ESTRATEGIA_COLLABORATIVE:
                rec = er.CollaborativeFiltering();
                break;
            case CtrlRecomanacio.ESTRATEGIA_CONTENT_BASED:
                rec = er.ContentBasedFiltering();
                break;
            case CtrlRecomanacio.ESTRATEGIA_HIBRID:
                rec = er.Hibrid();
                break;
            default:
        }

        for (Map.Entry<Integer, Double> entrada : rec.entrySet()) {
            Recomanacio r = new Recomanacio(entrada.getKey(), entrada.getValue());
            CjtRecomanacions.add(r);
        }

    }

    // consultora que avalua la qualitat de les recomanacions obtingudes previament, retornant el Discounted Cumulative Gain
    public Double avaluarQualitat() throws RecomanacionsException {
        if (test.isEmpty()) {
            throw new RecomanacionsException("No s'ha carregat el fitxer de test.");
        }

        double DCG = 0.0;
        double ultimaValoracio = 0.0;
        ArrayList<Set<Integer>> resultats = new ArrayList<>();

        for (Recomanacio r : CjtRecomanacions) {
            double val = r.getValoracio();
            if (resultats.isEmpty() || val > ultimaValoracio) {
                Set<Integer> s = new HashSet<>();
                s.add(r.getItemID());
                ultimaValoracio = r.getValoracio();
                resultats.add(s);
            } else {
                int mida = resultats.size() - 1;
                Set<Integer> s = resultats.get(mida);
                s.add(r.getItemID());
                resultats.set(mida, s);
            }
        }

        for (int i = 0; i < resultats.size(); ++i) {
            Set<Integer> elements = resultats.get(i);

            for (Integer item : elements) {
                double valMitja = 0.0;
                double numerador = 0.0;
                if (test.containsKey(item)) {
                    ArrayList<Double> valoracioItem = test.get(item);
                    for (Double v : valoracioItem) valMitja += v;
                    valMitja /= valoracioItem.size();
                    numerador = Math.pow(2, valMitja) - 1;
                }
                double denominador = Math.log10(i + 2) / Math.log10(2);
                DCG += numerador / denominador;
            }
        }

        return DCG;
    }

    // operacio que carrega les recomanacions de test passades per parametre a la variable global
    public void carregaTest(List<List<String>> stringTest) throws RecomanacionsException {
        if (!stringTest.isEmpty()) {
            List<String> header = stringTest.remove(0);
            HashMap<String, Integer> ordre = new HashMap<>();
            for (int i = 0; i < 3; ++i) {
                ordre.put(header.get(i), i);
            }
            for (List<String> valTest : stringTest) {
                String[] valoracio = new String[3];
                int index = 0;
                for (String s : valTest) {
                    valoracio[index] = s;
                    ++index;
                }
                Integer itemID = Integer.parseInt(valoracio[ordre.get("itemId")]);
                Double valor = Double.parseDouble(valoracio[ordre.get("rating")]);

                if (test.containsKey(itemID)) {
                    test.get(itemID).add(valor);
                } else {
                    ArrayList<Double> v = new ArrayList<>();
                    v.add(valor);
                    test.put(itemID, v);
                }
            }
        } else {
            throw new RecomanacionsException("No s'ha pogut carregar correctament el fitxer de test.");
        }
    }

    // consultora que retorna la data d'obtencio de les recomanacions
    public Date getData() {
        return data;
    }

    // consultora que retorna l'identificador de l'usuari a qui es recomana
    public Integer getUserID() {
        return userID;
    }

    // consultora que retorna la estrategia utilitzada per a obtenir les recomanacions
    public String getEstrategia() {
        return estrategia;
    }

    // imprimeix els atributs del conjunt i de totes les recomanacions
    public void imprimirRecomanacions() {
        System.out.println("UserID " + userID + ", Data " + data + ", Estrategia " + estrategia);
        for (Recomanacio r : CjtRecomanacions) r.imprimirRecomanacio();
    }

    // operacio que carrega recomanacions previament obtingudes
    public void carregaRecomanacions(List<String> recomanacions) {
        for (int i = 0; i < recomanacions.size(); i+=2) {
            Integer itemID = Integer.parseInt(recomanacions.get(i));
            Double valoracio = Double.parseDouble(recomanacions.get(i+1));
            Recomanacio r = new Recomanacio(itemID, valoracio);
            CjtRecomanacions.add(r);
        }
    }
}