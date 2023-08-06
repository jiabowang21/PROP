package domini.item;

import domini.item.atributs.AtribCategoric;
import domini.item.atributs.Atribut;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Oriol Deiros
 */

public class Item {
    private final int itemID;
    private final TipusItem tipus;

    public Item(int ID, TipusItem tipusItem) {
        itemID = ID;
        tipus = tipusItem;
    }

    public final int getID() {
        return itemID;
    }

    public final HashMap<String, Atribut> GetAtrB() {
        return tipus.getAtrB();
    }

    public final HashMap<String, ArrayList<AtribCategoric>> GetAtrC() {
        return tipus.getAtrC();
    }

    public final HashMap<String, Atribut> GetAtrN() {
        return tipus.getAtrN();
    }


    public void modificarAtribut(String nomAtribut, String valorNou) throws Exception {
        int tipusAtr = tipus.getTipus(nomAtribut);
        tipus.modificarAtribut(nomAtribut, tipusAtr, valorNou);
    }

    final TipusItem getTipus() {
        return tipus;
    }

    @Override
    public String toString() {
        String out = "||ItemID " + itemID;
        out = out + tipus.toString();
        return out + "|";
    }
}
