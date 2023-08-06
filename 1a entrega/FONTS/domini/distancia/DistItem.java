package domini.distancia;

import domini.item.Item;
import domini.item.atributs.AtribCategoric;
import domini.item.atributs.Atribut;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Jiabo Wang
 */

public class DistItem extends Distancia {
    private final Item item1;
    private final Item item2;
    private Double distancia;
    private Double atributs;

    public DistItem(Item i1, Item i2) {
        item1 = i1;
        item2 = i2;
        atributs = 0.0;
        distancia = 0.0;
    }

    //0 -> vol dir que són completament iguals
    //1 -> vol dir que són completament diferents

    /**
     * funció que calcula la distància dels atributs categòrics
     */
    private Double d_categorics() throws Exception {
        HashMap<String, ArrayList<AtribCategoric>> aC1 = item1.GetAtrC();
        HashMap<String, ArrayList<AtribCategoric>> aC2 = item2.GetAtrC();
        ArrayList<Double> ponderacio = new ArrayList<>();
        //Tipo i1
        for (HashMap.Entry<String, ArrayList<AtribCategoric>> entry : aC1.entrySet()) {
            ArrayList<AtribCategoric> s1 = entry.getValue();
            String s12 = entry.getKey();
            ArrayList<AtribCategoric> s2 = aC2.get(s12);
            if (s2 != null && s1 != null) {
                //Para cada tipo i1, los atributos de i1
                for (AtribCategoric atribCategoric : s1) {
                    boolean trobat = false;
                    for (AtribCategoric categoric : s2) {
                        if (atribCategoric.getStringValue().equals(categoric.getStringValue())) trobat = true;
                    }
                    if (trobat) ponderacio.add(1.0);
                    else ponderacio.add(0.0);
                }
            } else if (s2 == null && s1 != null) {
                for (AtribCategoric ignored : s1) {
                    ponderacio.add(0.0);
                }
            }
        }

        for (HashMap.Entry<String, ArrayList<AtribCategoric>> entry : aC2.entrySet()) {
            String s22 = entry.getKey();
            if (!aC1.containsKey(s22) && s22 != null) {
                for (AtribCategoric ignored : entry.getValue()) {
                    ponderacio.add(0.0);
                }
            }
        }
        Double suma = 0.0;
        for (Double aDouble : ponderacio) {
            suma += aDouble;
        }
        atributs += ponderacio.size();
        return suma;
    }

    /**
     * funció que calcula la distància dels atributs numerics
     */
    private Double d_numerics() throws Exception {
        HashMap<String, Atribut> aC1 = item1.GetAtrN();
        HashMap<String, Atribut> aC2 = item2.GetAtrN();
        ArrayList<Double> ponderacio = new ArrayList<>();
        //Tipo i1
        for (HashMap.Entry<String, Atribut> entry : aC1.entrySet()) {
            Double s1 = entry.getValue().getDoubleValue();
            String s12 = entry.getKey();
            Double s2 = aC2.get(s12).getDoubleValue();
            if (s2 != null && s1 != null) {
                //Para cada tipo i1, los atributos de i1
                if (s1.equals(s2)) ponderacio.add(1.0);
                else ponderacio.add(0.0);
            } else if (s2 == null && s1 != null) {
                ponderacio.add(0.0);
            }
        }
        return get_d(aC1, aC2, ponderacio);
    }

    private Double get_d(HashMap<String, Atribut> aC1, HashMap<String, Atribut> aC2, ArrayList<Double> ponderacio) {
        for (HashMap.Entry<String, Atribut> entry : aC2.entrySet()) {
            String s22 = entry.getKey();
            if (!aC1.containsKey(s22) && s22 != null) {
                ponderacio.add(0.0);
            }
        }
        Double suma = 0.0;
        for (Double aDouble : ponderacio) {
            suma += aDouble;
        }
        atributs += ponderacio.size();
        return suma;
    }

    /**
     * funció que calcula la distància dels atributs booleans
     */
    private Double d_booleans() throws Exception {
        HashMap<String, Atribut> aC1 = item1.GetAtrB();
        HashMap<String, Atribut> aC2 = item2.GetAtrB();
        ArrayList<Double> ponderacio = new ArrayList<>();
        //Tipo i1
        for (HashMap.Entry<String, Atribut> entry : aC1.entrySet()) {
            Boolean s1 = entry.getValue().getBoolValue();
            String s12 = entry.getKey();
            Boolean s2 = aC2.get(s12).getBoolValue();
            if (s2 != null && s1 != null) {
                //Para cada tipo i1, los atributos de i1
                if (s1.equals(s2)) ponderacio.add(1.0);
                else ponderacio.add(0.0);
            } else if (s2 == null && s1 != null) {
                ponderacio.add(0.0);
            }
        }

        return get_d(aC1, aC2, ponderacio);
    }

    @Override
    public Double getDistancia() throws Exception {
        atributs = 0.0;
        distancia = 1.0 - ((d_categorics() + d_numerics() + d_booleans()) / atributs);
        return Math.round(distancia * 100.0) / 100.0;
    }
}
