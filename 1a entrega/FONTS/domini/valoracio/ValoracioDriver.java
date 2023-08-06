package domini.valoracio;

import utils.InOut;

/**
 * @author Pau Antonio Soler
 */

public class ValoracioDriver {
    public static void main(String[] args) throws Exception {
        Valoracio v = new Valoracio(null, null, null);
        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 5:
                        Double rating = v.getValoracio();
                        if (rating != null) inOut.writeln("Valoracio: " + rating);
                        else inOut.writeln("No s'ha valorat cap Item");
                        break;
                    case 4:
                        Integer item = v.getItemID();
                        if (item != null) inOut.writeln("ItemID: " + item);
                        else inOut.writeln("No s'ha valorat cap Item");
                        break;
                    case 3:
                        Integer user = v.getUserID();
                        if (user != null) inOut.writeln("UserID: " + user);
                        else inOut.writeln("No s'ha valorat cap Item");
                        break;
                    case 2:
                        if (v.getItemID() != null) v.imprimirValoracio();
                        else inOut.writeln("No s'ha valorat cap Item");
                        break;
                    case 1:
                        inOut.writeln("A continuació escriu l'identificador de l'item:");
                        Integer itemID = Integer.parseInt(inOut.readword());

                        inOut.writeln("A continuació indica l'identificador de l'usuari:");
                        Integer userID = Integer.parseInt(inOut.readword());

                        inOut.writeln("A continuació indica la valoracio de l'item:");
                        Double valoracio = Double.parseDouble(inOut.readword());

                        v = new Valoracio(userID, itemID, valoracio);
                        inOut.writeln("L'item s'ha valorat correctament\n");
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
        inOut.writeln("Driver Valoracions");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Valoracio un Item");
        inOut.writeln("2. Imprimir Dades de la Valoracio");
        inOut.writeln("3. Imprimir Usari");
        inOut.writeln("4. Imprimir Item");
        inOut.writeln("5. Imprimir Valoracio");
        inOut.writeln("6. Sortir");
        inOut.write("[opció] ");
    }

}