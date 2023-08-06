package domini.item.atributs;

public abstract class Atribut {
    public static final int TIPUS_BOOLEAN = 0;
    public static final int TIPUS_NUMERIC = 1;
    public static final int TIPUS_CATEGORIC = 2;

    Boolean atrInitialized;

    public Boolean isInitialized() {
        return atrInitialized;
    }

    public abstract int getTipus();

    public abstract Double getDoubleValue() throws AtributInvalidException;

    public abstract void setDoubleValue(Double newVal) throws AtributInvalidException;

    public abstract Boolean getBoolValue() throws AtributInvalidException;

    public abstract void setBoolValue(Boolean newVal) throws AtributInvalidException;

    public abstract String getStringValue() throws AtributInvalidException;

    public abstract void setStringValue(String newVal) throws AtributInvalidException;
}