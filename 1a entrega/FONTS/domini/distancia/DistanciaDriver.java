package domini.distancia;

import domini.item.CtrlItems;
import domini.item.Item;
import utils.InOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jiabo Wang
 */
public class DistanciaDriver {

    public static void main(String[] args) throws Exception {
        InOut inOut = new InOut();
        List<List<String>> llistaItems = new ArrayList<>();
        HashMap<Integer, Double> f1;
        HashMap<Integer, Double> f2;
        CtrlItems ctrlI = CtrlItems.getInstance();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 2:
                        f1 = new HashMap<>();
                        f2 = new HashMap<>();
                        inOut.writeln("Escriu un enter que indiqui quantes valoracions té el client 1:");
                        inOut.writeln("No es pot fer més d'una valoració pel mateix ID de ítem");
                        int n = inOut.readint();
                        inOut.writeln("A continuació escriu les n valoracions de la següent manera, ID item seguit d'un espai i després la valoració ");
                        inOut.writeln("No es pot fer més d'una valoració pel mateix ID de ítem");
                        for (int ii = 0; ii < n; ++ii) {
                            int ID1 = inOut.readint();
                            double val1 = inOut.readdouble();
                            f1.put(ID1, val1);
                        }

                        inOut.writeln("Escriu un enter que indiqui quantes valoracions té el client 2:");
                        int n2 = inOut.readint();
                        inOut.writeln("A continuació escriu les n valoracions de la següent manera, ID item seguit d'un espai i després la valoració ");
                        inOut.writeln("No es pot fer més d'una valoració pel mateix ID de ítem");
                        for (int ii = 0; ii < n2; ++ii) {
                            int ID1 = inOut.readint();
                            double val1 = inOut.readdouble();
                            f2.put(ID1, val1);
                        }
                        DistClient dc = new DistClient(f1, f2);
                        inOut.writeln("La distància entre el client 1 i el client 2 es de " + dc.getDistancia());
                        break;
                    case 1:
                        llistaItems.clear();
                        inOut.writeln("Escriu el nom dels atributs de l'ítem 1 separats per un espai (ha d'incloure un atribut que sigui id de l'ítem): \n");
                        inOut.readline();
                        List<String> l;
                        l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                        llistaItems.add(l);

                        inOut.writeln("Escriu el valor de cada atribut de l'ítem 1 en el mateix ordre");
                        l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                        llistaItems.add(l);

                        inOut.writeln("Escriu el valor de cada atribut de l'ítem 2 en el mateix ordre");
                        l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                        llistaItems.add(l);

                        ctrlI.carregarConjuntItems(llistaItems);

                        Item i = ctrlI.getItems().get(0);

                        Item i2 = ctrlI.getItems().get(1);
                        DistItem di = new DistItem(i, i2);
                        inOut.writeln("La distància entre els items " + i.getID() + " i " + i2.getID() + " es de " + di.getDistancia());
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
        inOut.writeln("Driver Controlador de Distancia");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Distancia entre dos Items");
        inOut.writeln("2. Distancia entre dos Clients");
        inOut.writeln("3. Sortir");
        inOut.write("[opció] ");
    }
}