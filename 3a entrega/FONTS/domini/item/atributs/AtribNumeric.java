package domini.item.atributs;

public class AtribNumeric extends Atribut {
    Double value;

    public AtribNumeric() {
        atrInitialized = false;
    }

    public AtribNumeric(Double val) {
        value = val;
        atrInitialized = true;
    }

    public int getTipus() {
        return Atribut.TIPUS_NUMERIC;
    }

    public Double getDoubleValue() {
        if (!atrInitialized) {
            return null;
        }
        return value;
    }

    public void setDoubleValue(Double newVal) {
        value = newVal;
        atrInitialized = true;
    }

    public Boolean getBoolValue() throws AtributInvalidException {
        throw new AtributInvalidException("Trying to get Bool Value from Boolean Attribute");
    }

    public void setBoolValue(Boolean newVal) throws AtributInvalidException {
        throw new AtributInvalidException("Trying to set Bool Value in Boolean Attribute");
    }

    public String getStringValue() throws AtributInvalidException {
        throw new AtributInvalidException("Trying to get String Value from Boolean Attribute");
    }

    public void setStringValue(String newVal) throws AtributInvalidException {
        throw new AtributInvalidException("Trying to set String Value in Boolean Attribute");
    }

    @Override
    public String toString() {
        return "Atribut Numeric: inicialitzat->" + atrInitialized + "|valor->" + value;
    }
}