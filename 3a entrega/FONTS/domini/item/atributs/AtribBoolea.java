package domini.item.atributs;

public class AtribBoolea extends Atribut {
    Boolean value;

    public AtribBoolea() {
        atrInitialized = false;
    }

    public AtribBoolea(Boolean val) {
        value = val;
        atrInitialized = true;
    }

    public int getTipus() {
        return Atribut.TIPUS_BOOLEAN;
    }

    public Boolean getBoolValue() throws AtributInvalidException {
        if (!atrInitialized) {
            return null;
        }
        return value;
    }

    public void setBoolValue(Boolean newVal) {
        value = newVal;
        atrInitialized = true;
    }

    public Double getDoubleValue() throws AtributInvalidException {
        throw new AtributInvalidException("Trying to get Int Value from Boolean Attribute");
    }

    public void setDoubleValue(Double newVal) throws AtributInvalidException {
        throw new AtributInvalidException("Trying to set Int Value in Boolean Attribute");
    }

    public String getStringValue() throws AtributInvalidException {
        throw new AtributInvalidException("Trying to get String Value from Boolean Attribute");
    }

    public void setStringValue(String newVal) throws AtributInvalidException {
        throw new AtributInvalidException("Trying to set String Value in Boolean Attribute");
    }

    @Override
    public String toString() {
        return "Atribut Boolean: inicialitzat->" + atrInitialized + "|valor->" + value;
    }
}