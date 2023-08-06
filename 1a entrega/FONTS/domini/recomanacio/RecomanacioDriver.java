package domini.recomanacio;

import utils.InOut;

/**
 * @author Pau Antonio Soler
 */

public class RecomanacioDriver {
    public static void main(String[] args) throws Exception {
        Recomanacio r = new Recomanacio(null, null);
        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 4:
                        Double rating = r.getValoracio();
                        if (rating != null) inOut.writeln("Valoracio Esperada: " + rating);
                        else inOut.writeln("No s'ha recomanat cap Item");
                        break;
                    case 3:
                        Integer item = r.getItemID();
                        if (item != null) inOut.writeln("ItemID: " + item);
                        else inOut.writeln("No s'ha recomanat cap Item");
                        break;
                    case 2:
                        if (r.getItemID() != null) r.imprimirRecomanacio();
                        else inOut.writeln("No s'ha recomanat cap Item");
                        break;
                    case 1:
                        inOut.writeln("A continuació escriu l'identificador de l'item:");
                        Integer itemID = Integer.parseInt(inOut.readword());

                        inOut.writeln("A continuació indica la valoracio esperada de l'item:");
                        Double valoracio = Double.parseDouble(inOut.readword());

                        r = new Recomanacio(itemID, valoracio);
                        inOut.writeln("L'item s'ha recomanat correctament\n");
                        break;
                    default:
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeHelp(InOut inOut) throws Exception {
        inOut.writeln("Driver Recomanacio");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Recomanar un Item");
        inOut.writeln("2. Imprimir Dades de la Recomanacio");
        inOut.writeln("3. Imprimir Item");
        inOut.writeln("4. Imprimir Valoracio Esperada");
        inOut.writeln("5. Sortir");
        inOut.write("[opció] ");
    }

}