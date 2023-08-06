package domini.valoracio;

import persistencia.CtrlPersistencia;

import java.util.*;

/**
 * @author Pau Antonio Soler
 */

public class CtrlValoracio {
    // instancia del controlador
    private static CtrlValoracio instance;
    // conjunt de valoracions del sistema segons el identificador de l'usuari
    private final HashMap<Integer, Set<Valoracio>> valoracions = new HashMap<>();
    private double valoracioMaxima = -1.0;

    private CtrlValoracio() {
    }

    public double getValoracioMaxima() {
        return valoracioMaxima;
    }

    public void setValoracioMaxima(double valoracioMaxima) {
        this.valoracioMaxima = valoracioMaxima;
    }

    // retorna la instancia del controlador
    public static CtrlValoracio getInstance() {
        if (instance == null) instance = new CtrlValoracio();
        return instance;
    }

    // operacio que afegeix una nova valoracio amb els atributs indicats
    public void valorarItem(Integer userID, Integer itemID, Double valoracio) {
        Valoracio v = new Valoracio(userID, itemID, valoracio);
        Set<Valoracio> valoracionsUsuari = new HashSet<>();
        if (valoracions.containsKey(userID)) {
            valoracionsUsuari = valoracions.get(userID);
            for (Valoracio val : valoracionsUsuari) {
                if (val.getItemID().equals(itemID)) {
                    valoracionsUsuari.remove(val);
                    break;
                }
            }
            valoracionsUsuari.add(v);
            valoracions.replace(userID, valoracionsUsuari);
        } else {
            valoracionsUsuari.add(v);
            valoracions.put(userID, valoracionsUsuari);
        }
    }

    // consultora que retorna un set amb totes les valoracions que ha realitzat un usuari
    public Set<Valoracio> getValoracionsUsuari(Integer userID) throws ValoracioException {
        Set<Valoracio> valoracions_usuari;

        valoracions_usuari = valoracions.get(userID);

        if (valoracions_usuari == null) {
            throw new ValoracioException("L'usuari no ha valorat cap ítem.");
        }
        return valoracions_usuari;
    }

    // consultora que retorna tot el conjunt de valoracions
    public HashMap<Integer, Set<Valoracio>> getValoracions() {
        return valoracions;
    }

    // operacio que permet guardar les valoracions en un fitxer
    public void escriureValoracions() {
        List<List<String>> valoracionsToWrite = new LinkedList<>();
        ArrayList<String> headers = new ArrayList<>();
        headers.add("itemId");
        headers.add("userId");
        headers.add("rating");
        valoracionsToWrite.add(headers);
        for (int vals : valoracions.keySet()) {
            for (Valoracio v : valoracions.get(vals)) {
                List<String> valoracio = new LinkedList<>();
                valoracio.add(String.valueOf(v.getItemID()));
                valoracio.add(String.valueOf(v.getUserID()));
                valoracio.add(String.valueOf(v.getValoracio()));
                valoracionsToWrite.add(valoracio);
            }
        }

        CtrlPersistencia ctrlPersistencia = CtrlPersistencia.getInstance();
        ctrlPersistencia.writeValoracio(valoracionsToWrite);
    }

    // operacio que carrega les valoracions passades per parametre al sistema
    public void carregarValoracions(List<List<String>> historicValoracions) {
        if (!historicValoracions.isEmpty()) {
            List<String> header = historicValoracions.remove(0);
            HashMap<String, Integer> ordre = new HashMap<>();
            for (int i = 0; i < 3; ++i) {
                ordre.put(header.get(i), i);
            }
            for (List<String> val : historicValoracions) {
                String[] valoracio = new String[3];
                int index = 0;
                for (String s : val) {
                    valoracio[index] = s;
                    ++index;
                }
                Integer itemID = Integer.parseInt(valoracio[ordre.get("itemId")]);
                Integer userID = Integer.parseInt(valoracio[ordre.get("userId")]);
                Double valor = Double.parseDouble(valoracio[ordre.get("rating")]);

                Valoracio v = new Valoracio(userID, itemID, valor);

                if (valoracions.containsKey(userID)) {
                    valoracions.get(userID).add(v);
                } else {
                    Set<Valoracio> vvv = new HashSet<>();
                    vvv.add(v);
                    valoracions.put(userID, vvv);
                }
            }
        }
    }

    // imprimeix els atributs de les valoracions de l'usuari indicat
    public void imprimirValoracionsUsuari(Integer userID) {
        Set<Valoracio> val = valoracions.get(userID);
        for (Valoracio v : val) {
            v.imprimirValoracio();
        }
    }

    // operacio per a esborrar una valoracio concreta
    public void esborrarValoracio(Integer userID, Integer itemID) throws ValoracioException {
        boolean esborrat = false;
        if (valoracions.containsKey(userID)) {
            Set<Valoracio> noves_valoracions = valoracions.get(userID);
            for (Valoracio v : noves_valoracions) {
                if (v.getItemID().equals(itemID)) {
                    noves_valoracions.remove(v);
                    esborrat = true;
                    break;
                }
            }
            if (esborrat) {
                valoracions.replace(userID, noves_valoracions);
            }
        }
        if (!esborrat){
            throw new ValoracioException("L'ítem no ha estat valorat prèviament per l'usuari.");
        }
    }
}