package domini.recomanacio;

import persistencia.CtrlPersistencia;
import utils.InOut;

import java.util.List;

/**
 * @author Pau Antonio Soler
 */

public class CtrlRecomanacioDriver {
    public static void main(String[] args) throws Exception {
        CtrlPersistencia ctrlP = CtrlPersistencia.getInstance();
        CtrlRecomanacio ctrlR = CtrlRecomanacio.getInstance();

        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 3:
                        inOut.writeln("A continuació escriu l'identificador de l'usuari:");
                        Integer user = Integer.parseInt(inOut.readword());

                        List<List<String>> test = ctrlP.readRecomanacionsTestUnknown();
                        Double qualitat = ctrlR.avaluarQualitat(user, test);
                        inOut.writeln("La valoració obtinguda per a les recomanacions ha estat: " + qualitat);
                        break;
                    case 2:
                        inOut.writeln("A continuació escriu l'identificador de l'usuari:");
                        Integer usuari = Integer.parseInt(inOut.readword());

                        ctrlR.imprimirRecomanacions(usuari);
                        break;
                    case 1:
                        inOut.writeln("A continuació escriu l'identificador de l'usuari:");
                        Integer userID = Integer.parseInt(inOut.readword());

                        inOut.writeln("A continuació indica el sistema recomanador que vols utilitzar:");
                        inOut.writeln("1. Collaborative Filtering");
                        inOut.writeln("2. Content Based Filtering");
                        String estrategia = inOut.readword();
                        switch (estrategia) {
                            case "1":
                                estrategia = "collaborative";
                                break;
                            case "2":
                                estrategia = "contentBased";
                                break;
                            default:
                                estrategia = "";
                        }

                        if (estrategia.equals("")) {
                            inOut.writeln("Sistema recomanador incorrecte\n");
                            break;
                        }
                        ctrlR.obtenirRecomanacions(estrategia, userID);
                        inOut.writeln("S'han obtingut correctament les recomanacions\n");
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
        inOut.writeln("Driver Controlador de Recomanacions");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Obtenir Recomanacions");
        inOut.writeln("2. Veure les Recomanacions");
        inOut.writeln("3. Avaluar Qualitat de les Recomanacions");
        inOut.writeln("4. Sortir");
        inOut.write("[opció] ");
    }

}