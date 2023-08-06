package domini.recomanacio;

import domini.item.CtrlItems;
import domini.item.Item;
import domini.sistemarecomanador.SistemaRecomanador;
import domini.valoracio.CtrlValoracio;
import domini.valoracio.Valoracio;
import persistencia.CtrlPersistencia;

import java.util.*;

/**
 * @author Pau Antonio Soler
 */

public class ConjuntRecomanacions {
    private final Integer userID;
    private final Date data;
    private final String estrategia;

    private final ArrayList<Recomanacio> CjtRecomanacions = new ArrayList<>();
    private final Map<Integer, ArrayList<Double>> test = new HashMap<>();

    public ConjuntRecomanacions(Integer userID, Date data, String estrategia) {
        this.userID = userID;
        this.data = data;
        this.estrategia = estrategia;
    }

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

        SistemaRecomanador sr = new SistemaRecomanador(cjtVal, cjtItems, userID);
        Map<Integer, Double> rec = new HashMap<>();

        switch (estrategia) {
            case "collaborative":
                rec = sr.CollaborativeFiltering();
                break;
            case "contentBased":
                rec = sr.ContentBasedFiltering();
                break;
            default:
        }

        for (Map.Entry<Integer, Double> entrada : rec.entrySet()) {
            Recomanacio r = new Recomanacio(entrada.getKey(), entrada.getValue());
            CjtRecomanacions.add(r);
        }

    }

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

    public Date getData() {
        return data;
    }

    public Integer getUserID() {
        return userID;
    }

    public String getEstrategia() {
        return estrategia;
    }

    public void imprimirRecomanacions() {
        System.out.println("UserID " + userID + ", Data " + data + ", Estrategia " + estrategia);
        for (Recomanacio r : CjtRecomanacions) r.imprimirRecomanacio();
    }
}