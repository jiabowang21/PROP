package domini.item;

import domini.item.atributs.AtribCategoric;
import domini.item.atributs.Atribut;
import domini.item.atributs.AtributInvalidException;
import persistencia.CtrlPersistencia;

import java.util.*;

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

    public final ArrayList<String> getAtributeList() throws Exception {
        if(nomAtributs == null) throw new Exception("Conjunt No Inicialitzat");
        return nomAtributs;
    }

    //Retorna una diccionari amb el nom de l'atributc com a clau i el seu tipus com a atribut
    public final HashMap<String, String> getAtributesByType() throws Exception {
        HashMap<String, String> res = new HashMap<>();
        String tipus = null;
        if(nomAtributs == null) throw new Exception("Conjunt No Inicialitzat");
        for (String nom : nomAtributs){
            if(nom.equals("id")) continue;
            switch(plantillaTipus.getTipus(nom)){
                case Atribut.TIPUS_BOOLEAN:
                    tipus = "BOOL";
                    break;
                case Atribut.TIPUS_NUMERIC:
                    tipus = "NUM";
                    break;
                case Atribut.TIPUS_CATEGORIC:
                    tipus = "CAT";
            }
            res.put(nom, tipus);
        }
        return res;
    }

    //Mètode que permet detectar de quin tipus d'atribut es tracta, introduint dos valors de la mateixa columna dels items
    private int getAtributeType(String firstCheck, String secondCheck) throws Exception {
        if (firstCheck == null) {
            throw new Exception("Error checking type of items");
        }
        //Si el segon atribut és null treballem nomès amb el primer
        if (secondCheck == null) secondCheck = firstCheck;
        if ((firstCheck.equals("True") || firstCheck.equals("False")) && (secondCheck.equals("True") || secondCheck.equals("False"))) {
            return Atribut.TIPUS_BOOLEAN;
        } else {
            //Parsejem el double, si retorna error, interpretem que es un atribut categòric
            try {
                Double.parseDouble(firstCheck);
                Double.parseDouble(secondCheck);
                return Atribut.TIPUS_NUMERIC;
            } catch (NumberFormatException e) {
                return Atribut.TIPUS_CATEGORIC;
            }
        }
    }

    //El mètode inicialitza el conjunt d'items, assignant els atributs a plantillaItem
    private void definirAtributsItem(List<List<String>> conjunt) throws Exception {
        plantillaTipus = new TipusItem();
        int size = conjunt.get(0).size();
        for (int i = 0; i < size; i++) {
            if (conjunt.get(0).get(i).equals("id")) continue;
            int randVal;
            //Agafem valors aleatoris del conjunt per obtenir el tipus d'item
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
            //Iterem per tots els atributs de l'item, l'ID té un tractament especial
            tipus.modificarAtribut(nomAtributs.get(i), tipus.getTipus(nomAtributs.get(i)), item.get(i));
        }
        if (id == -1) {
            //LLança excepció si no s'ha trobat l'atribut ID
            throw new AtributInvalidException("Item ID no trobat en el conjunt");
        }
        Item nouItem = new Item(id, tipus);
        cjtItems.add(nouItem);
    }

    public Item getItem(int id) throws Exception {
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
        //Reinicialitza el conjunt, esborrant totes les entrades i afegint els atributs corresponents un altre cop des de zero
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

    public List<String> serialitzarItemPerId(int id) throws Exception {
        return serialitzarItem(this.getItem(id));
    }

    private List<String> serialitzarItem(Item item) throws AtributInvalidException {
        //Itera per tots els atributs de l'item i els retorna en el mateix ordre que es van introduir, és a dir, l'ordre que indica la variable nomAtributs
        List<String> res = new ArrayList<>(nomAtributs.size());
        for(int i = 0; i < nomAtributs.size(); i++) res.add(i, null);
        res.set(nomAtributs.indexOf("id"), String.valueOf(item.getID()));
        String val;
        for(Map.Entry<String, Atribut> b : item.getTipus().getAtrB().entrySet()){
            if(!b.getValue().isInitialized()) val = "";
            else if(b.getValue().getBoolValue()) val = "True";
            else val = "False";
            res.set(nomAtributs.indexOf(b.getKey()), val);
        }
        for(Map.Entry<String, Atribut> n : item.getTipus().getAtrN().entrySet()){
            if(!n.getValue().isInitialized()) res.set(nomAtributs.indexOf(n.getKey()), "");
            else res.set(nomAtributs.indexOf(n.getKey()), n.getValue().getDoubleValue().toString());
        }
        for(Map.Entry<String, ArrayList<AtribCategoric>> c : item.getTipus().getAtrC().entrySet()){
            StringBuilder valBuilder = new StringBuilder();
            if (!c.getValue().get(0).isInitialized()) new StringBuilder();
            else valBuilder = new StringBuilder(c.getValue().get(0).getStringValue());
            for(int j = 1; j < c.getValue().size(); j++) valBuilder.append(';').append(c.getValue().get(j).getStringValue());
            val = valBuilder.toString();
            res.set(nomAtributs.indexOf(c.getKey()), val);
        }
        return res;
    }

    public List<List<String>> serialitzarConjuntItems() throws Exception {
        List<List<String>> cjt = new ArrayList<>();
        cjt.add(this.getAtributeList());

        for (Item item : cjtItems) {
            cjt.add(serialitzarItem(item));
        }
        return cjt;
    }

    public void escriureItems() {
        try {
            CtrlPersistencia.getInstance().writeItem(serialitzarConjuntItems());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}