package domini.sistemarecomanador;

import domini.item.CtrlItems;
import domini.item.Item;
import domini.valoracio.CtrlValoracio;
import domini.valoracio.Valoracio;
import persistencia.CtrlPersistencia;
import utils.InOut;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Jiabo Wang
 */
public class SistemaRecomanadorDriver {
    public static void main(String[] args) throws Exception {

        InOut inOut = new InOut();
        HashMap<Integer, Set<Valoracio>> cjtValoracions;
        ArrayList<Item> cjtItems;
        List<List<String>> llistaItems = new ArrayList<>();

        int id;
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 2:
                        inOut.writeln("Llegir de teclat o de fitxer? [t/F]\n");
                        inOut.readline();
                        if (inOut.read() == 't') {
                            Set<Valoracio> c;
                            cjtValoracions = new HashMap<>();
                            inOut.writeln("Escriu un enter que indiqui quants clients hi haura:\n");
                            int n_clients = inOut.readint();

                            for (int iii = 0; iii < n_clients; ++iii) {
                                c = new HashSet<>();
                                inOut.writeln("Escriu l'id del client ");
                                int id_client = inOut.readint();
                                inOut.writeln("Escriu un enter que indiqui quantes valoracions te el client " + id_client + " :");
                                int n = inOut.readint();
                                inOut.writeln("A continuacio escriu les n valoracions de la seguent manera, ID item seguit d'un espai i despres la valoracio ");
                                inOut.writeln("No es pot fer mes d'una valoracio pel mateix ID de item");
                                for (int ii = 0; ii < n; ++ii) {
                                    int ID1 = inOut.readint();
                                    double val1 = inOut.readdouble();
                                    Valoracio v = new Valoracio(id_client, ID1, val1);
                                    c.add(v);
                                }
                                cjtValoracions.put(id_client, c);
                            }
                            inOut.writeln("Valoracions dels clients carregats");
                            
                            llistaItems.clear();
                            inOut.writeln("Escriu un enter que indiqui quants items hi haura:");
                            int n_items = inOut.readint();
                            inOut.writeln("Escriu el nom dels atributs de l'item separats per un espai (ha d'incloure un atribut que sigui id de l'item):");
                            inOut.readline();
                            List<String> l;
                            l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                            llistaItems.add(l);
                            for (int iii = 0; iii < n_items; ++iii) {
                                inOut.writeln("Escriu el valor de cada atribut de l'item en el mateix ordre");
                                l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                                llistaItems.add(l);
                            }
                            CtrlItems ctrlI = CtrlItems.getInstance();
                            ctrlI.carregarConjuntItems(llistaItems);
                            cjtItems = ctrlI.getItems();
                            inOut.writeln("Items carregats " + cjtItems.size());
                        } else {
                            inOut.writeln("Proporciona el path per carregar els items");

                            String path = inOut.readword();
                            
                            llistaItems.clear();
                            //Carregar items
                            CtrlItems ctrlI = CtrlItems.getInstance();
                            CtrlPersistencia ctrlP = CtrlPersistencia.getInstance();
                            llistaItems = new ArrayList<>();
                            ctrlP.setDirectoriDeTreball(path);
                            llistaItems = ctrlP.readItems();
                            ctrlI.carregarConjuntItems(llistaItems);
                            cjtItems = CtrlItems.getInstance().getItems();

                            inOut.writeln("Items carregats");


                            inOut.writeln("Proporciona el path per carregar les valoracions");

                            String path2 = inOut.readword();
                            ctrlP.setDirectoriDeTreball(path2);
                            //Carregar valoracions
                            CtrlValoracio ctrlV = CtrlValoracio.getInstance();
                            List<List<String>> llistaValoracio;
                            llistaValoracio = ctrlP.readValoracions();
                            ctrlV.carregarValoracions(llistaValoracio);
                            cjtValoracions = ctrlV.getValoracions();

                            inOut.writeln("Valoracions carregats");
                        }

                        inOut.writeln("Proporciona el id del client al qui li vols fer les recomanacions");
                        id = inOut.readint();
                        if (cjtValoracions.containsKey(id)) {
                            SistemaRecomanador sr = new SistemaRecomanador(cjtValoracions, cjtItems, id);
                            Map<Integer, Double> rec2;
                            rec2 = sr.CollaborativeFiltering();
                            inOut.writeln("Recomanacions fetes per l'algorisme Collaborative Filtering al client " + id);
                            for (Entry<Integer, Double> e : rec2.entrySet()) {
                                inOut.writeln("Item " + e.getKey() + " Valoracio " + e.getValue());
                            }
                        } else {
                            inOut.writeln("El client amb el ID " + id + " no ha fet cap valoracio");
                        }
                        break;
                    case 1:
                        inOut.writeln("Llegir de teclat o de fitxer? [t/F]\n");
                        inOut.readline();
                        if (inOut.read() == 't') {
                            Set<Valoracio> c;
                            cjtValoracions = new HashMap<>();
                            inOut.writeln("Escriu un enter que indiqui quants clients hi haura�:\n");
                            int n_clients = inOut.readint();

                            for (int iii = 0; iii < n_clients; ++iii) {
                                c = new HashSet<>();
                                inOut.writeln("Escriu l'id del client ");
                                int id_client = inOut.readint();
                                inOut.writeln("Escriu un enter que indiqui quantes valoracions te el client " + id_client + " :");
                                int n = inOut.readint();
                                inOut.writeln("A continuacio escriu les n valoracions de la segent manera, ID item seguit d'un espai i despres la valoracio ");
                                inOut.writeln("No es pot fer mes d'una valoracio pel mateix ID de item");
                                for (int ii = 0; ii < n; ++ii) {
                                    int ID1 = inOut.readint();
                                    double val1 = inOut.readdouble();
                                    Valoracio v = new Valoracio(id_client, ID1, val1);
                                    c.add(v);
                                }
                                cjtValoracions.put(id_client, c);
                            }
                            inOut.writeln("Valoracions dels clients carregats");
                            llistaItems.clear();
                            inOut.writeln("Escriu un enter que indiqui quants items hi haura:");
                            int n_items = inOut.readint();
                            inOut.writeln("Escriu el nom dels atributs de l'item separats per un espai (ha d'incloure un atribut que sigui id de l'item):");
                            inOut.readline();
                            List<String> l;
                            l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                            llistaItems.add(l);
                            for (int iii = 0; iii < n_items; ++iii) {
                                inOut.writeln("Escriu el valor de cada atribut de l'item en el mateix ordre");
                                l = Arrays.asList(inOut.readline().trim().split("\\s+"));
                                llistaItems.add(l);
                            }
                            CtrlItems ctrlI = CtrlItems.getInstance();
                            ctrlI.carregarConjuntItems(llistaItems);
                            cjtItems = ctrlI.getItems();
                            inOut.writeln("Items carregats " + cjtItems.size());
                        } else {
                            inOut.writeln("Proporciona el path per carregar els items");

                            String path = inOut.readword();
                            llistaItems.clear();
                            //Carregar items
                            CtrlItems ctrlI = CtrlItems.getInstance();
                            CtrlPersistencia ctrlP = CtrlPersistencia.getInstance();
                            llistaItems = new ArrayList<>();
                            ctrlP.setDirectoriDeTreball(path);
                            llistaItems = ctrlP.readItems();
                            ctrlI.carregarConjuntItems(llistaItems);
                            cjtItems = CtrlItems.getInstance().getItems();

                            inOut.writeln("Items carregats");


                            inOut.writeln("Proporciona el path per carregar les valoracions");

                            String path2 = inOut.readword();
                            ctrlP.setDirectoriDeTreball(path2);
                            //Carregar valoracions
                            CtrlValoracio ctrlV = CtrlValoracio.getInstance();
                            List<List<String>> llistaValoracio;
                            llistaValoracio = ctrlP.readValoracions();
                            ctrlV.carregarValoracions(llistaValoracio);
                            cjtValoracions = ctrlV.getValoracions();

                            inOut.writeln("Valoracions carregats");
                        }

                        inOut.writeln("Proporciona el id del client al qui li vols fer les recomanacions");
                        id = inOut.readint();
                        if (cjtValoracions.containsKey(id)) {
                            SistemaRecomanador sr2 = new SistemaRecomanador(cjtValoracions, cjtItems, id);
                            Map<Integer, Double> rec;
                            rec = sr2.ContentBasedFiltering();
                            System.out.println("Recomanacions fetes per l'algorisme ContentBased Filtering al client " + id);
                            for (Entry<Integer, Double> e : rec.entrySet()) {
                                inOut.writeln("Item " + e.getKey() + " Valoracio " + e.getValue());
                            }
                        } else {
                            inOut.writeln("El client amb el ID " + id + " no ha fet cap valoracio");
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
        inOut.writeln("Driver Sistema Recomanador");
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Aplicar ContentBased Filtering");
        inOut.writeln("2. Aplicar Collaborative Filtering");
        inOut.writeln("3. Sortir");
        inOut.write("[Opcio] ");
    }

}

