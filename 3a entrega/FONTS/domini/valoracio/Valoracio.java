package domini.valoracio;

/**
 * @author Pau Antonio Soler
 */

public class Valoracio {
    // identificador de l'item valorat
    private final Integer itemID;
    // identificador de l'usuari que ha valorat
    private final Integer userID;
    // valor amb que l'usuari ha valorat l'item
    private Double valoracio;

    // creadora
    public Valoracio(Integer userID, Integer itemID, Double rating) {
        this.userID = userID;
        this.itemID = itemID;
        valoracio = rating;
    }

    // consultora del identificador de l'item
    public Integer getItemID() {
        return itemID;
    }

    // consultora de l'identificador de l'usuari
    public Integer getUserID() {
        return userID;
    }

    // consultora del valor de la valoracio
    public Double getValoracio() {
        return valoracio;
    }

    // imprimeix els atributs de la valoracio
    public void imprimirValoracio() {
        System.out.println("User ID " + userID + ", Item ID " + itemID + ", Valoracio " + valoracio);
    }

    // operacio que modifica el valor de la valoracio amb el nou valor passat per parametre
    public void setValoracio(Double valoracio) {
        this.valoracio = valoracio;
    }
}