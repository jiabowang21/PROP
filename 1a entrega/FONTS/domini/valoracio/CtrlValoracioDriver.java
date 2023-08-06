package domini.valoracio;

import persistencia.CtrlPersistencia;
import utils.InOut;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Pau Antonio Soler
 */

public class CtrlValoracioDriver {
    public static void main(String[] args) throws Exception {
        CtrlValoracio ctrlV = CtrlValoracio.getInstance();
        CtrlPersistencia ctrlP = CtrlPersistencia.getInstance();

        List<List<String>> llistaValoracions;
        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 3:
                        inOut.writeln("A continuació indica l'identificador de l'usuari:");
                        Integer user = Integer.parseInt(inOut.readword());
                        ctrlV.imprimirValoracionsUsuari(user);
                        inOut.writeln("\n");
                        break;
                    case 2:
                        if (ctrlP.readValoracions().isEmpty()) {
                            List<String> capcalera = new LinkedList<>();
                            capcalera.add("itemId");
                            capcalera.add("userId");
                            capcalera.add("rating");
                            ctrlP.writeValoracio(capcalera);
                        }
                        inOut.writeln("A continuació escriu l'identificador de l'item:");
                        Integer itemID = Integer.parseInt(inOut.readword());

                        //Integer userID = ctrlU.getUsuariIniciat().getId();
                        inOut.writeln("A continuació indica l'identificador de l'usuari:");
                        Integer userID = Integer.parseInt(inOut.readword());

                        inOut.writeln("A continuació indica la valoracio de l'item:");
                        Double valoracio = Double.parseDouble(inOut.readword());

                        ctrlV.valorarItem(userID, itemID, valoracio);
                        inOut.writeln("L'item s'ha valorat correctament\n");

                        break;
                    case 1:
                        llistaValoracions = ctrlP.readValoracions();
                        ctrlV.carregarValoracions(llistaValoracions);
                        inOut.writeln("S'han carregat correctament l'històric de valoracions\n");
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
        inOut.writeln("Driver Controlador de Valoracions");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Carregar històric de valoracions");
        inOut.writeln("2. Valorar un item");
        inOut.writeln("3. Imprimir valoracions d'un usuari");
        inOut.writeln("4. Sortir");
        inOut.write("[opció] ");
    }

}