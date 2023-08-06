package domini.recomanacio;

/**
 * @author Pau Antonio Soler
 */

public class Recomanacio {
    // identificador de l'item recomanat
    private final Integer itemID;

    // valor amb que s'espera que l'usuari valori l'item
    private final Double valoracio_esperada;

    // constructora de la recomanacio
    public Recomanacio(Integer itemID, Double val) {
        this.itemID = itemID;
        valoracio_esperada = val;
    }

    // retorna l'identificador de l'item recomanat
    public Integer getItemID() {
        return itemID;
    }

    // retorna el valor esperat
    public Double getValoracio() {
        return valoracio_esperada;
    }

    // imprimeix els atributs de la recomanacio
    public void imprimirRecomanacio() {
        System.out.println("Item ID " + itemID + ", Valoracio Esperada " + valoracio_esperada);
    }
}