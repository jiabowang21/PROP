package domini.item;

import domini.item.atributs.Atribut;
import domini.item.atributs.AtributInvalidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Oriol Deiros
 */


public class CtrlItems {
    private static CtrlItems instance;
    private final ArrayList<Item> cjtItems;

    private ArrayList<String> nomAtributs;
    private TipusItem plantillaTipus;


    private CtrlItems() {
        plantillaTipus = new TipusItem();
        cjtItems = new ArrayList<>();
    }

    public static CtrlItems getInstance() {
        if (instance == null) instance = new CtrlItems();
        return instance;
    }

    public final ArrayList<Item> getItems() {
        return cjtItems;
    }

    private int getAtributeType(String firstCheck, String secondCheck) throws Exception {
        if (firstCheck == null) {
            throw new Exception("Error checking type of items");
        }
        if (secondCheck == null) secondCheck = firstCheck;
        if ((firstCheck.equals("True") || firstCheck.equals("False")) && (secondCheck.equals("True") || secondCheck.equals("False"))) {
            return Atribut.TIPUS_BOOLEAN;
        } else {
            try {
                Double.parseDouble(firstCheck);
                Double.parseDouble(secondCheck);
                return Atribut.TIPUS_NUMERIC;
            } catch (NumberFormatException e) {
                return Atribut.TIPUS_CATEGORIC;
            }
        }
    }

    private void definirAtributsItem(List<List<String>> conjunt) throws Exception {
        plantillaTipus = new TipusItem();
        int size = conjunt.get(0).size();
        for (int i = 0; i < size; i++) {
            if (conjunt.get(0).get(i).equals("id")) continue;
            int randVal;
            try {
                randVal = new Random().nextInt(size - 2) + 1;
            } catch (IllegalArgumentException e) {
                randVal = 1;
            }
            int atrType = Atribut.TIPUS_CATEGORIC;
            if (conjunt.size() == 2) atrType = getAtributeType(conjunt.get(1).get(i), null);
            else if (size != 2)
                atrType = getAtributeType(conjunt.get(randVal % (size + 1)).get(i), conjunt.get((randVal * 2) % (size + 1)).get(i));
            plantillaTipus.afegirAtribut(conjunt.get(0).get(i), atrType);
        }

    }

    public void afegirItem(List<String> item) throws Exception {
        TipusItem tipus = new TipusItem(plantillaTipus);
        if (item.size() != nomAtributs.size()) {
            throw new Exception("Error de Parseig, el vector d'atributs de l'item no correspon amb el nombre d'atributs");
        }
        int id = -1;
        for (int i = 0; i < item.size(); i++) {
            if (nomAtributs.get(i).equals("id")) {
                try {
                    id = Integer.parseInt(item.get(i));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                continue;
            }
            tipus.modificarAtribut(nomAtributs.get(i), tipus.getTipus(nomAtributs.get(i)), item.get(i));
        }
        if (id == -1) {
            throw new AtributInvalidException("Item ID no trobat en el conjunt");
        }
        Item nouItem = new Item(id, tipus);
        cjtItems.add(nouItem);
    }

    private Item getItem(int id) throws Exception {
        for (Item i : cjtItems) {
            if (i.getID() == id) return i;
        }
        throw new Exception("ID no trobat");
    }

    public void modificarItem(int id, String nomAtribut, String nouValor) throws Exception {
        if (!existeixItem(id)) throw new Exception("L'item no està en el conjunt");
        Item i = getItem(id);
        i.modificarAtribut(nomAtribut, nouValor);
    }

    public void eliminarItem(int id) throws Exception {
        for (int i = 0; i < cjtItems.size(); i++) {
            if (cjtItems.get(i).getID() == id) {
                cjtItems.remove(i);
                return;
            }
        }
        throw new Exception("ID no trobat");
    }

    public final ArrayList<String> getNomAtributs() {
        return nomAtributs;
    }

    public boolean existeixItem(int id) {
        for (Item cjtItem : cjtItems) {
            if (cjtItem.getID() == id) {
                return true;
            }
        }
        return false;
    }

    public int mida() {
        return cjtItems.size();
    }

    public void carregarConjuntItems(List<List<String>> conjunt) throws Exception {
        cjtItems.clear();
        nomAtributs = new ArrayList<>(conjunt.get(0));
        definirAtributsItem(conjunt);
        for (int i = 1; i < conjunt.size(); i++) {
            try {
                afegirItem(conjunt.get(i));
            } catch (Exception e) {
                System.out.println("No s'ha afegit un element per contenir el seguent error: ");
                System.out.println(e.getMessage());
            }
        }
    }

}