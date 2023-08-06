package domini.item.atributs;

import domini.item.CtrlItems;
import domini.item.Item;
import persistencia.CtrlPersistencia;
import utils.InOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class AtributDriver {
    public static void main(String[] args) throws Exception {
        AtribBoolea b = new AtribBoolea();
        AtribCategoric c = new AtribCategoric();
        AtribNumeric n = new AtribNumeric();
        List<List<String>> llistaItems = new ArrayList<>();

        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 2:
                        inOut.writeln(b.toString());
                        inOut.writeln(c.toString());
                        inOut.writeln(n.toString());
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

                            inOut.writeln("Escriu el valor de cada atribut de l'ítem en el mateix ordre");
                            l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                            llistaItems.add(l);
                        } else {
                            inOut.writeln("Desitja canviar el directori d'on llegir items.csv? (per defecte es ./data) [y/N]");
                            inOut.readline();
                            if (inOut.read() == 'y') {
                                inOut.writeln("Selecciona el directori d'on llegir items.csv, (pot ser directori relatiu o absolut)");
                                inOut.readline();
                                ctrlP.setDirectoriDeTreball(inOut.readline());
                            }
                            llistaItems = ctrlP.readItems();
                        }
                        CtrlItems ctrlI = CtrlItems.getInstance();
                        ctrlI.carregarConjuntItems(llistaItems);
                        if (ctrlI.mida() < 1) throw new Exception("El fitxer no s'ha llegit correctament");
                        Item i = CtrlItems.getInstance().getItems().get(0);
                        try {
                            b = (AtribBoolea) i.GetAtrB().entrySet().iterator().next().getValue();
                            c = i.GetAtrC().entrySet().iterator().next().getValue().get(0);
                            n = (AtribNumeric) i.GetAtrN().entrySet().iterator().next().getValue();
                            //ti = new TipusItem(i.getTipus());
                            inOut.writeln("S'ha llegit correctament el primer item del conjunt\n");
                        } catch (NoSuchElementException e) {
                            inOut.writeln("Error: L'ítem ha de contenir com a mínim un atribut de cada tipus");
                        }

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
        inOut.writeln("Driver Classe Atribut");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Carregar els tres atributs de cada tipus del primer item del conjunt d'ítems");
        inOut.writeln("2. Imprimir Atributs");
        inOut.writeln("3. Sortir");
        inOut.write("[opció] ");
    }
}