package domini.item;

import persistencia.CtrlPersistencia;
import utils.InOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemDriver {
    public static void main(String[] args) throws Exception {
        Item i = new Item(1, new TipusItem());
        InOut inOut = new InOut();
        List<List<String>> llistaItems = new ArrayList<>();
        String nom;
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 4:
                        inOut.writeln(i.getID());
                        break;
                    case 3:
                        inOut.writeln("Introdueix el nom de l'atribut a modificar: ");
                        nom = inOut.readword();
                        inOut.writeln("Introdueix el nou valor: ");
                        String valor = inOut.readword();
                        i.modificarAtribut(nom, valor);
                        inOut.writeln("L'atribut s'ha actualitzat correctament\n");
                        break;
                    case 2:
                        inOut.writeln(i.toString());
                        break;
                    case 1:
                        llistaItems.clear();
                        CtrlPersistencia ctrlP = CtrlPersistencia.getInstance();
                        inOut.writeln("Llegir de teclat o de fitxer? [t/F]");
                        inOut.readline();
                        if (inOut.read() == 't') {
                            inOut.writeln("Escriu el nom dels atributs separats per un espai:\n");
                            inOut.readline();
                            List<String> l;
                            l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                            llistaItems.add(l);

                            inOut.writeln("Escriu el valor de cada atribut de l'item en el mateix ordre");
                            l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                            llistaItems.add(l);
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
                        CtrlItems ctrlI = CtrlItems.getInstance();
                        ctrlI.carregarConjuntItems(llistaItems);
                        if (ctrlI.mida() < 1) throw new Exception("El fitxer no s'ha llegit correctament");
                        i = CtrlItems.getInstance().getItems().get(0);
                        inOut.writeln("S'ha llegit correctament el primer item del conjunt\n");
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
        inOut.writeln("1. Carregar Item");
        inOut.writeln("2. Imprimir Item");
        inOut.writeln("3. Modificar un atribut de l'item");
        inOut.writeln("4. Imprimir ID");
        inOut.writeln("5. Sortir");
        inOut.write("[opció] ");
    }
}