package domini.valoracio;

/**
 * @author Pau Antonio Soler
 */

public class Valoracio {
    private final Integer itemID;
    private final Integer userID;
    private final Double valoracio;

    public Valoracio(Integer userID, Integer itemID, Double rating) {
        this.userID = userID;
        this.itemID = itemID;
        valoracio = rating;
    }

    public Integer getItemID() {
        return itemID;
    }

    public Integer getUserID() {
        return userID;
    }

    public Double getValoracio() {
        return valoracio;
    }

    public void imprimirValoracio() {
        System.out.println("User ID " + userID + ", Item ID " + itemID + ", Valoracio " + valoracio);
    }
}