package domini.item.atributs;

public class AtribCategoric extends Atribut {

    String value;

    public AtribCategoric() {
        atrInitialized = false;
    }

    public AtribCategoric(String val) {
        value = val;
        atrInitialized = true;
    }

    public int getTipus() {
        return Atribut.TIPUS_CATEGORIC;
    }

    public String getStringValue() throws AtributInvalidException {
        if (!atrInitialized) {
            return null;
        }
        return value;
    }

    public void setStringValue(String newVal) {
        value = newVal;
        atrInitialized = true;
    }

    public Double getDoubleValue() throws AtributInvalidException {
        throw new AtributInvalidException("Trying to get Int Value from Boolean Attribute");
    }

    public void setDoubleValue(Double newVal) throws AtributInvalidException {
        throw new AtributInvalidException("Trying to set Int Value in Boolean Attribute");
    }

    public Boolean getBoolValue() throws AtributInvalidException {
        throw new AtributInvalidException("Trying to get Bool Value from Boolean Attribute");
    }

    public void setBoolValue(Boolean newVal) throws AtributInvalidException {
        throw new AtributInvalidException("Trying to set Bool Value in Boolean Attribute");
    }

    @Override
    public String toString() {
        return "Atribut Categòric: inicialitzat->" + atrInitialized + "|valor->" + value;
    }
}