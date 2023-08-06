package domini.item;

import persistencia.CtrlPersistencia;
import utils.InOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CtrlItemsDriver {
    public static void main(String[] args) throws Exception {

        CtrlItems ctrlI = CtrlItems.getInstance();
        CtrlPersistencia ctrlP = CtrlPersistencia.getInstance();
        List<List<String>> llistaItems = new ArrayList<>();
        InOut inOut = new InOut();
        int id;
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 8:
                        inOut.writeln("El conjunt d'Items té una mida de " + ctrlI.mida() + " items\n");
                        break;
                    case 7:
                        inOut.writeln("Introdueix identificador de l'item: ");
                        id = inOut.readint();
                        if (ctrlI.existeixItem(id)) inOut.writeln("L'item existix dins del conjunt\n");
                        else inOut.writeln("L'item no existeix en el conjunt\n");
                        break;
                    case 6:
                        inOut.writeln("Introdueix identificador de l'item: ");
                        id = inOut.readint();
                        try {
                            ctrlI.eliminarItem(id);
                            inOut.writeln("L'item s'ha esborrat correctament\n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        inOut.writeln("Introdueix identificador de l'item: ");
                        id = inOut.readint();
                        inOut.writeln("Introdueix en nom de l'atribut de l'item: ");
                        String atr = inOut.readword();
                        inOut.writeln("Introdueix el nou valor de l'atribut: ");
                        String newVal = inOut.readword();
                        ctrlI.modificarItem(id, atr, newVal);
                        inOut.writeln("Modificació realitzada correctament\n");
                        break;
                    case 4:
                        inOut.writeln("A continuació has d'escriure els valors dels atributs en el seguent ordre, separats per un espai: ");
                        for (String s : ctrlI.getNomAtributs()) inOut.write(' ' + s);
                        inOut.write("\n");
                        ArrayList<String> newItem = new ArrayList<>();
                        for (int i = 0; i < ctrlI.getNomAtributs().size(); i++) {
                            newItem.add(inOut.readword());
                        }
                        ctrlI.afegirItem(newItem);
                        inOut.writeln("L'item s'ha afegit al conjunt correctament\n");
                        break;
                    case 3:
                        inOut.writeln("Introdueix la posició de l'item a llegir, segons l'ordre llegit valor entre 1 i " + ctrlI.mida());
                        int itemPos = inOut.readint();
                        inOut.writeln(CtrlItems.getInstance().getItems().get(itemPos - 1).toString());
                        break;
                    case 2:
                        for (Item i : CtrlItems.getInstance().getItems()) {
                            inOut.writeln("------------------");
                            inOut.writeln(i.toString());
                        }
                        break;
                    case 1:
                        llistaItems.clear();
                        inOut.writeln("Llegir de teclat o de fitxer? [t/F]");
                        inOut.readline();
                        if (inOut.read() == 't') {
                            inOut.writeln("Escriu el nom dels atributs separats per un espai:\n");
                            inOut.readline();
                            List<String> l;
                            l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                            llistaItems.add(l);

                            inOut.writeln("Escriu el valor de cada atribut de l'item en el mateix ordre, per parar, escriu STOP");
                            l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                            while (!l.get(0).equals("STOP")) {
                                llistaItems.add(l);
                                inOut.writeln("Escriu el valor de cada atribut de l'item en el mateix ordre, per parar, escriu STOP");
                                l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                            }
                        } else {
                            inOut.writeln("Desitja canviar el directori d'on llegir items.csv? (per defecte es ./data) [y/N]");
                            inOut.readline();
                            if (inOut.read() == 'y') {
                                inOut.writeln("Sel·leciona el directori d'on llegir items.csv, (pot ser directori relatiu o absolut)");
                                inOut.readline();
                                ctrlP.setDirectoriDeTreball(inOut.readline());
                            }
                            llistaItems = ctrlP.readItems();
                        }

                        ctrlI.carregarConjuntItems(llistaItems);
                        inOut.writeln("S'han llegit correctament " + ctrlI.mida() + " items\n");
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
        inOut.writeln("Driver Controlador d'Items");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Carregar conjunt d'items");
        inOut.writeln("2. Escriure TOTS els items carregats");
        inOut.writeln("3. Escriure un item");
        inOut.writeln("4. Afegir un item");
        inOut.writeln("5. Modifica un item");
        inOut.writeln("6. Esborra un item");
        inOut.writeln("7. Comprova si existeix un item");
        inOut.writeln("8. Imprimeix la mida del conjunt d'Items");
        inOut.writeln("9. Sortir");
        inOut.write("[opció] ");
    }
}
