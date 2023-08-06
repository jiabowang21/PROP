package domini.valoracio;

import persistencia.CtrlPersistencia;

import java.util.*;

/**
 * @author Pau Antonio Soler
 */

public class CtrlValoracio {
    private static CtrlValoracio instance;
    private final HashMap<Integer, Set<Valoracio>> valoracions = new HashMap<>();

    private CtrlValoracio() {
    }

    public static CtrlValoracio getInstance() {
        if (instance == null) instance = new CtrlValoracio();
        return instance;
    }

    public void valorarItem(Integer userID, Integer itemID, Double valoracio) {
        Valoracio v = new Valoracio(userID, itemID, valoracio);
        Set<Valoracio> noves_valoracions = new HashSet<>();
        if (valoracions.containsKey(userID)) {
            noves_valoracions = valoracions.get(userID);
            noves_valoracions.add(v);
            valoracions.replace(userID, noves_valoracions);
        } else {
            noves_valoracions.add(v);
            valoracions.put(userID, noves_valoracions);
        }

        guardarValoracio(v);
    }

    public Set<Valoracio> getValoracionsUsuari(Integer userID) throws ValoracioException {
        Set<Valoracio> valoracions_usuari;

        valoracions_usuari = valoracions.get(userID);

        if (valoracions_usuari == null) {
            throw new ValoracioException("L'usuari no ha valorat cap ítem.");
        }
        return valoracions_usuari;
    }

    public HashMap<Integer, Set<Valoracio>> getValoracions() {
        if (valoracions.isEmpty()) {
            CtrlPersistencia ctrlP = CtrlPersistencia.getInstance();
            List<List<String>> llistaValoracions = ctrlP.readValoracions();
            carregarValoracions(llistaValoracions);
        }
        return valoracions;
    }

    private void guardarValoracio(Valoracio v) {
        List<String> valoracio = new LinkedList<>();
        valoracio.add(v.getItemID().toString());
        valoracio.add(v.getUserID().toString());
        valoracio.add(v.getValoracio().toString());

        CtrlPersistencia ctrlPersistencia = CtrlPersistencia.getInstance();
        ctrlPersistencia.writeValoracio(valoracio);
    }

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

    public void imprimirValoracionsUsuari(Integer userID) {
        Set<Valoracio> val = valoracions.get(userID);
        for (Valoracio v : val) {
            v.imprimirValoracio();
        }
    }
}