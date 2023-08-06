package domini.item;

import domini.item.atributs.Atribut;
import domini.item.atributs.AtributInvalidException;
import persistencia.CtrlPersistencia;
import utils.InOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TipusItemDriver {
    public static void main(String[] args) throws Exception {
        TipusItem ti = new TipusItem();
        InOut inOut = new InOut();
        List<List<String>> llistaItems = new ArrayList<>();
        String nom;
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 5:
                        inOut.writeln("Introdueix el nom de l'atribut a modificar: ");
                        nom = inOut.readword();
                        inOut.writeln("Introdueix el nou valor: ");
                        String val = inOut.readword();
                        ti.modificarAtribut(nom, ti.getTipus(nom), val);
                        inOut.writeln("L'atribut s'ha modificat correctament\n");
                        break;
                    case 4:
                        inOut.writeln("Introdueix el nom de l'atribut a afegir: ");
                        nom = inOut.readword();
                        inOut.writeln("Introdueix el número del tipus d'atribut: (0-Booleà, 1-Numeric, 2-Categòric)");
                        int t = inOut.readint();
                        ti.afegirAtribut(nom, t);
                        inOut.writeln("L'atribut s'ha inserit correctament\n");
                        break;
                    case 3:
                        inOut.writeln("Introdueix el nom de l'atribut a consultar: ");
                        nom = inOut.readword();
                        try {
                            switch (ti.getTipus(nom)) {
                                case Atribut.TIPUS_BOOLEAN:
                                    inOut.writeln("L'atribut és booleà\n");
                                    break;
                                case Atribut.TIPUS_CATEGORIC:
                                    inOut.writeln("L'atribut és categòric\n");
                                    break;
                                case Atribut.TIPUS_NUMERIC:
                                    inOut.writeln("L'atribut és numèric\n");
                            }
                        } catch (AtributInvalidException e) {
                            inOut.writeln("L'atribut amb aquest nom no existeix\n");
                        }

                        break;
                    case 2:
                        inOut.writeln(ti.toString());
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
                        Item i = CtrlItems.getInstance().getItems().get(0);
                        ti = new TipusItem(i.getTipus());
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
        inOut.writeln("Driver de TipusItem");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Carregar un TipusItem");
        inOut.writeln("2. Imprimir Atributs");
        inOut.writeln("3. Obtenir tipus d'un atribut");
        inOut.writeln("4. Afegeix un atribut");
        inOut.writeln("5. Modifica un atribut");
        inOut.writeln("6. Sortir");
        inOut.write("[opció] ");
    }
}