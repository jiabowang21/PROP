package domini.recomanacio;

/**
 * @author Pau Antonio Soler
 */

public class Recomanacio {
    private final Integer itemID;
    private final Double valoracio_esperada;

    public Recomanacio(Integer itemID, Double val) {
        this.itemID = itemID;
        valoracio_esperada = val;
    }

    public Integer getItemID() {
        return itemID;
    }

    public Double getValoracio() {
        return valoracio_esperada;
    }

    public void imprimirRecomanacio() {
        System.out.println("Item ID " + itemID + ", Valoracio Esperada " + valoracio_esperada);
    }
}