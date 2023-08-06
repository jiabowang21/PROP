package domini.item;

import domini.item.atributs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oriol Deiros
 */

public class TipusItem {
    private final HashMap<String, Atribut> atributs_numerics;
    private final HashMap<String, ArrayList<AtribCategoric>> atributs_categorics;
    private final HashMap<String, Atribut> atributs_booleans;

    public TipusItem() {
        atributs_numerics = new HashMap<>();
        atributs_booleans = new HashMap<>();
        atributs_categorics = new HashMap<>();
    }

    public TipusItem(TipusItem ti) {
        atributs_numerics = new HashMap<>(ti.atributs_numerics);
        atributs_booleans = new HashMap<>(ti.atributs_booleans);
        atributs_categorics = new HashMap<>(ti.atributs_categorics);
    }

    final HashMap<String, Atribut> getAtrN() {
        return atributs_numerics;
    }

    final HashMap<String, ArrayList<AtribCategoric>> getAtrC() {
        return atributs_categorics;
    }

    final HashMap<String, Atribut> getAtrB() {
        return atributs_booleans;
    }

    void afegirAtribut(String nom, int tipus) throws AtributInvalidException {
        switch (tipus) {
            case Atribut.TIPUS_BOOLEAN:
                atributs_booleans.put(nom, new AtribBoolea());
                break;
            case Atribut.TIPUS_NUMERIC:
                atributs_numerics.put(nom, new AtribNumeric());
                break;
            case Atribut.TIPUS_CATEGORIC:
                atributs_categorics.put(nom, new ArrayList<>());
                //atributs.put(nom, new AtribCategoric(""));

                break;
            default:
                throw new AtributInvalidException("Tipus d'atribut no valid");
        }
    }

    void modificarAtribut(String nom, int atrType, String val) throws AtributInvalidException {
        //Si el valor està buit, s'afegeix l'atribut com a no inicialitzat
        switch (atrType) {
            case Atribut.TIPUS_BOOLEAN:
                if(val.equals("")) atributs_booleans.put(nom, new AtribBoolea());
                else atributs_booleans.put(nom, new AtribBoolea(Boolean.parseBoolean(val)));
                break;
            case Atribut.TIPUS_NUMERIC:
                try {
                    atributs_numerics.put(nom, new AtribNumeric(Double.parseDouble(val)));
                } catch (NumberFormatException e) {
                    atributs_numerics.put(nom, new AtribNumeric());
                }
                break;
            case Atribut.TIPUS_CATEGORIC:
                ArrayList<AtribCategoric> x = new ArrayList<>();
                //Pel cas dels atributs categòrics, asumim que ; marca la separació dels diferents valors del mateix atribut
                for (String str : val.split(";")) {
                    if(str.equals(""))x.add(new AtribCategoric());
                    else x.add(new AtribCategoric(str));
                }
                atributs_categorics.put(nom, x);

                break;
            default:
                throw new AtributInvalidException("Tipus d'atribut no valid");
        }
    }

    int getTipus(String nomAtribut) throws AtributInvalidException {
        if (atributs_booleans.containsKey(nomAtribut)) {
            return Atribut.TIPUS_BOOLEAN;
        } else if (atributs_numerics.containsKey(nomAtribut)) {
            return Atribut.TIPUS_NUMERIC;
        } else if (atributs_categorics.containsKey(nomAtribut)) {
            return Atribut.TIPUS_CATEGORIC;
        }
        throw new AtributInvalidException("L'atribut amb aquest nom no existeix");
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("||Atributs booleans: ");
        for (Map.Entry<String, Atribut> entry : atributs_booleans.entrySet()) {
            out.append("|").append(entry.getKey()).append("->");
            if (entry.getValue().isInitialized()) {
                try {
                    out.append(entry.getValue().getBoolValue());
                } catch (AtributInvalidException e) {
                    e.printStackTrace();
                }
            } else out.append(" ");
        }
        out.append("||Atributs numèrics: ");
        for (Map.Entry<String, Atribut> entry : atributs_numerics.entrySet()) {
            out.append("|").append(entry.getKey()).append("->");
            if (entry.getValue().isInitialized()) {
                try {
                    out.append(entry.getValue().getDoubleValue());
                } catch (AtributInvalidException e) {
                    e.printStackTrace();
                }
            } else out.append(" ");
        }
        out.append("||Atributs categòrics: ");
        for (Map.Entry<String, ArrayList<AtribCategoric>> entry : atributs_categorics.entrySet()) {
            out.append("|").append(entry.getKey()).append("->");
            for (AtribCategoric a : entry.getValue()) {
                if (a.isInitialized()) {
                    try {
                        out.append(a.getStringValue());
                    } catch (AtributInvalidException e) {
                        e.printStackTrace();
                    }
                } else out.append(" ");
            }
        }
        return out + "||";
    }
}