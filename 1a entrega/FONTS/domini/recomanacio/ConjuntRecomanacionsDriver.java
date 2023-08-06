package domini.recomanacio;

import persistencia.CtrlPersistencia;
import utils.InOut;

import java.util.Date;
import java.util.List;

/**
 * @author Pau Antonio Soler
 */

public class ConjuntRecomanacionsDriver {
    public static void main(String[] args) throws Exception {
        ConjuntRecomanacions cr = new ConjuntRecomanacions(null, null, null);
        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 6:
                        String estrat = cr.getEstrategia();
                        if (estrat != null) inOut.writeln("Estrategia: " + estrat);
                        else inOut.writeln("No s'ha recomanat cap Item");
                        break;
                    case 5:
                        Date date = cr.getData();
                        if (date != null) inOut.writeln("Data: " + date);
                        else inOut.writeln("No s'ha recomanat cap Item");
                        break;
                    case 4:
                        Integer usuari = cr.getUserID();
                        if (usuari != null) inOut.writeln("Usuari: " + usuari);
                        else inOut.writeln("No s'ha recomanat cap Item");
                        break;
                    case 3:
                        if (cr.getUserID() != null) cr.imprimirRecomanacions();
                        else inOut.writeln("No s'han obtingut Recomanacions");
                        break;
                    case 2:
                        CtrlPersistencia ctrlP = CtrlPersistencia.getInstance();
                        List<List<String>> test = ctrlP.readRecomanacionsTestUnknown();
                        cr.carregaTest(test);

                        if (cr.getUserID() != null) inOut.writeln("Qualitat Recomanacions: " + cr.avaluarQualitat());
                        else inOut.writeln("No s'han obtingut Recomanacions");
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
                        Date data = new Date();
                        cr = new ConjuntRecomanacions(userID, data, estrategia);
                        cr.obtenirRecomanacions();
                        inOut.writeln("Les recomanacions s'han obtingut correctament\n");
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
        inOut.writeln("Driver Conjunt Recomanacions");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Obtenir Recomanacions");
        inOut.writeln("2. Avaluar Qualitat Recomanacions");
        inOut.writeln("3. Imprimir Recomanacions");
        inOut.writeln("4. Imprimir Usuari");
        inOut.writeln("5. Imprimir Data");
        inOut.writeln("6. Imprimir Estrategia");
        inOut.writeln("7. Sortir");
        inOut.write("[opció] ");
    }

}