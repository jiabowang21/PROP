package domini.estrategiarecomanacio;

import domini.distancia.DistClient;
import domini.distancia.DistItem;
import domini.item.Item;
import domini.valoracio.Valoracio;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author Jiabo Wang
 */
public class EstrategiaRecomanacio {

    //Atribut per a obtenir numeros randoms
    private final Random random = new Random();
    //Atribut per a guardar totes les valoracions fetes fins el moment
    private final HashMap<Integer, HashMap<Integer, Double>> data;
    //Atribut per a guardar els items totals
    private final ArrayList<Item> items_totals;
    //Atribut per a guardar l'identificador del client a qui li volem fer la recomanacio
    private final Integer clientID;

    //constructora de la classe EstrategiaRecomanacio
    public EstrategiaRecomanacio(HashMap<Integer, Set<Valoracio>> c, ArrayList<Item> i, Integer x) {
        
        items_totals = i;
        clientID = x;
        data = new HashMap<>();        
        
        c.forEach((key, value) -> {
            HashMap<Integer, Double> va = new HashMap<>();
            value.forEach(v -> va.put(v.getItemID(), v.getValoracio()));
            data.put(key, va);
        });
    }

    //Funcio que ordena els elements d'un map pels valors del elemnts
    private Map<Integer, Double> sortByComparator(Map<Integer, Double> unsortMap, boolean order) {
        List<Entry<Integer, Double>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort((o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Map<Integer, Double> sortedMap = new LinkedHashMap<>();
        list.forEach(entry -> {
            sortedMap.put(entry.getKey(), entry.getValue());
        });
        return sortedMap;
    }

    //Implementa l'algoritme kNN
    private Map<Integer, Double> kNN(ArrayList<Item> C, HashMap<Integer, Double> val) throws Exception {
        
        //conjunt de recomanacions sense ordenar
        Map<Integer, Double> sense_ordenar = new HashMap<>();
        //conjunt de recomanacions ordenat
        Map<Integer, Double> resultat = new HashMap<>();
        //parametre k de la funcio
        int k = 10;
        
        //per cada item agradat calcula la seva distancia amb els altres items 
        for (Item i1 : C) {
            Map<Integer, Double> aux = new HashMap<>();
            for (Item i2 : items_totals) {
                Integer iaux2 = i2.getID();
                if (iaux2 != i1.getID() && !val.containsKey(iaux2)) {
                    DistItem distancia1 = new DistItem(i1, i2);
                    //calcula la seva valoracio esperada multiplicant la distancia per la valoracio del item agradat
                    aux.put(iaux2, Math.round((1 - distancia1.getDistancia()) * val.get(i1.getID())*100.0)/100.0);
                }
            }
            aux = sortByComparator(aux, false);
            
            for (HashMap.Entry<Integer, Double> epp : aux.entrySet()) {
                Integer kkey = epp.getKey();
                Double f = sense_ordenar.get(kkey);
                if (f == null || f < aux.get(kkey)) {
                    sense_ordenar.put(kkey, aux.get(kkey));
                }
            }
        }
        
        //els ordena de manera decreixent de la valoracio esperada
        sense_ordenar = sortByComparator(sense_ordenar, false);
        Iterator<Integer> itr = sense_ordenar.keySet().iterator();
        
        int count = 0;
        //agafa els 10 items amb la valoracio esperada mes alta
        while (count < k && itr.hasNext()) {
            Integer kkey = itr.next();
            resultat.put(kkey, sense_ordenar.get(kkey));
            ++count;
        }
        //els ordena de manera decreixent de la valoracio esperada
        resultat = sortByComparator(resultat, false);
        
        return resultat;
    }

    //Implementa l'algoritme ContentBased Filtering
    public Map<Integer, Double> ContentBasedFiltering() throws Exception {
        
        //valoracions del client
        HashMap<Integer, Double> valoracions_client = new HashMap<>();
        //items que ha valorat el client
        ArrayList<Item> items_valorats = new ArrayList<>();
        
        if (data.containsKey(clientID)) valoracions_client = data.get(clientID);
        else return valoracions_client;
        
        //calcula la valoracio maxima, es a dir, per saber si s'ha puntuat sobre 5 o sobre 10
        double max = 0.0;
        for (HashMap.Entry<Integer, HashMap<Integer, Double>> entry : data.entrySet()) {
            for (HashMap.Entry<Integer, Double> entry2 : entry.getValue().entrySet()) {
                if (entry2.getValue() > max) max = entry2.getValue();
            }
        }
        
        //s'agafen els items que li han agradat al client, es a dir, aquells que tenen una valoracio major o igual que 0.7 * max
        for (HashMap.Entry<Integer, Double> entry : valoracions_client.entrySet()) {
            if (entry.getValue() >= 0.7 * max) {
                items_totals.stream().filter(i -> (i.getID() == entry.getKey())).forEachOrdered(items_valorats::add);
            }
        }
        
        //crida a la funcio kNN 
        return kNN(items_valorats, valoracions_client);
    }
    
    //Generacio dels k centroides random
    private List<HashMap<Integer, Double>> CentroidesRandom(List<Integer> clients, int k) {
        //centroides 
        List<HashMap<Integer, Double>> centroids = new ArrayList<>();
        //valors maxims de les valoracions dels items
        HashMap<Integer, Double> max_values = new HashMap<>();
        //valors minims de les valoracions dels items
        HashMap<Integer, Double> min_values = new HashMap<>();
        //items
        Set<Integer> items = new HashSet<>();
        
        //calcul del valor maxim i minim de cada item
        clients.forEach(client -> {
            data.get(client).forEach((key, value) -> {
                //valor maxim
                max_values.compute(key, (k1, max) -> max == null || value > max ? value : max);

                //valor minim
                min_values.compute(key, (k1, min) -> min == null || value < min ? value : min);

                items.add(key);
            });
        }); 
        
        //creacio dels centroides
        for (int i = 0; i < k; ++i) {
            HashMap<Integer, Double> coordinates = new HashMap<>();
            items.forEach(attribute -> {
                double max = max_values.get(attribute);
                double min = min_values.get(attribute);
                coordinates.put(attribute, random.nextDouble() * (max - min) + min);
            });
            centroids.add(coordinates);
        }
        
        return centroids;
    }

    //Donat un client, hem de buscar el centroide del cluster que estigui a menor distancia a ell
    private HashMap<Integer, Double> CentroideMesProper(Integer client, List<HashMap<Integer, Double>> centroids) {
        double minimumDistance = 99999;
        HashMap<Integer, Double> nearest = null;
        
        //per cada centroide, calculem la seva distancia amb client i ens quedem amb el mes petit
        for (HashMap<Integer, Double> centroid : centroids) {
            DistClient distance = new DistClient(data.get(client), centroid);

            double currentDistance = distance.getDistancia();
            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearest = centroid;
            }
        }
        
        return nearest;
    }

    //Assignar cada client al seu cluster mes proper
    private HashMap<HashMap<Integer, Double>, List<Integer>> assignarCluster(HashMap<HashMap<Integer, Double>, List<Integer>> clusters, Integer client, HashMap<Integer, Double> centroid) {
        
        //primer eliminem el client del seu cluster inicial
        clusters.entrySet().forEach(entry -> {
            entry.getValue().stream().filter(i -> (i.equals(client))).forEachOrdered(i -> {
                entry.getValue().remove(i);
            });
        }); 
        
        //afegim el client al cluster que te el centroide de la entrada
        
        //si el cluster conte el centroide, s'afageix el client a aquell centroide del cluster
        if (clusters.containsKey(centroid)) {
            clusters.get(centroid).add(client);
        }
        
        //en cas contrari, es crea un nou centroide i s'afageix al cluster
        else {
            List<Integer> ne = new ArrayList<>();
            ne.add(client);
            clusters.put(centroid, ne);
        }
        
        return clusters;
        
    }

   //calcul de la mitjana dels valors d'un centroide
    private HashMap<Integer, Double> mitjana(HashMap<Integer, Double> centroid, List<Integer> clients) {
        
        //Si el centroide no te cap client, no s'ha de recolocar. 
        if (clients == null || clients.isEmpty()) {
            return centroid;
        }
        
        //Sino, hem de fer la mitjana de totes les valoracions dels clients que formen part del centroide
        clients.stream().map(client1 -> data.get(client1)).forEachOrdered(valors -> {
            valors.entrySet().forEach(entry -> {
                centroid.put(entry.getKey(), 0.0);
            });
        });
        clients.forEach(client -> {
            data.get(client).forEach(
                    (k, v) -> centroid.compute(k, (k1, currentValue) -> v + (currentValue != null ? currentValue : 0.0)));
        });
        centroid.forEach((k, v) -> centroid.put(k, v / clients.size()));

        return centroid;
    }

    //Recololocacio de tots els centroides, es recalculen els nous centroides fent la mitjana
    private List<HashMap<Integer, Double>> recolocacioCentroides(HashMap<HashMap<Integer, Double>, List<Integer>> clusters) {
        return clusters.entrySet().stream().map(e -> mitjana(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    //algoritme kmeans
    private HashMap<HashMap<Integer, Double>, List<Integer>> kmeans(List<Integer> clients, int k) {
        
        //llista dels centroides
        List<HashMap<Integer, Double>> centroids = CentroidesRandom(clients, k);
        //el conjunt dels clusters totals
        HashMap<HashMap<Integer, Double>, List<Integer>> clusters = new HashMap<>();
        //copia del conjunt de clusters, utilitzat per saber si al cap d'una iteracio 
        //els clusters canvien o no
        HashMap<HashMap<Integer, Double>, List<Integer>> lastState = new HashMap<>();
        //boolea que indica si s'ha d'acabar o no 
        Boolean shouldTerminate = false;

        while (shouldTerminate.equals(false)) {
            //per cada client calcula el centroide mes proper i se li asigna
            for (Integer client : clients) {
                HashMap<Integer, Double> centroid = CentroideMesProper(client, centroids);
                clusters = assignarCluster(clusters, client, centroid);
            }
            //si els clusters son iguals que els de la iteracio anteior, s'acaba el bucle
            if (clusters.equals(lastState)) shouldTerminate = true;
            
            lastState = clusters;
            
            //es recoloquen els centroides
            centroids = recolocacioCentroides(clusters);
            clusters = new HashMap<>();
        }

        return lastState;
    }

    //Troba al grup que pertany el client al qui se li vol fer la recomanacio
    private List<Integer> find_group(HashMap<HashMap<Integer, Double>, List<Integer>> kmean) {
        List<Integer> group = new ArrayList<>();
        
        for (HashMap.Entry<HashMap<Integer, Double>, List<Integer>> entry : kmean.entrySet()) {
            List<Integer> c1 = entry.getValue();
            for (Integer client : c1) {
                if (client.equals(clientID)) {
                    group = c1;
                    break;
                }
            }
        }
        return group;
    }

    //slopeOne algorithm
    private HashMap<Integer, Double> slopeOne(HashMap<Integer, HashMap<Integer, Double>> data3) {
        
        //Conjunt dels valors de la diferencia
        HashMap<Integer, Map<Integer, Double>> diferencia = new HashMap<>();

        //Conjunt dels valors de frequencia
        HashMap<Integer, Map<Integer, Double>> frequencia = new HashMap<>();
        
        //calcul de frequencies i diferencies
        data3.entrySet().forEach(user -> {
            //per cada Item
            user.getValue().entrySet().stream().map(e -> {
                //inicialitzar el valor, si era buit
                if (!diferencia.containsKey(e.getKey())) {
                    diferencia.put(e.getKey(), new HashMap<>());
                    frequencia.put(e.getKey(), new HashMap<>());
                }
                return e;
            }).forEachOrdered(e -> {
                //recorre tots els altres items per calcular les diferencies
                user.getValue().entrySet().stream().filter(e2 -> (!e.getKey().equals(e2.getKey()))).peek(e2 -> {
                    //ens guardem les frequencies dels items
                    double cont = 0.0;
                    if (frequencia.get(e.getKey()).containsKey(e2.getKey())) {
                        cont = frequencia.get(e.getKey()).get(e2.getKey());
                    }
                    frequencia.get(e.getKey()).put(e2.getKey(), cont + 1.0);
                }).forEachOrdered(e2 -> {
                    //la diferencia actual, ess a dir la que hem anat acumulant
                    double diferencia_actual = 0.0;
                    
                    if (diferencia.get(e.getKey()).containsKey(e2.getKey())) {
                        diferencia_actual = diferencia.get(e.getKey()).get(e2.getKey());
                    }

                    //la diferencia observada la diferencia entre les dues valoracions
                    double diferencia_observada = e.getValue() - e2.getValue();

                    diferencia.get(e.getKey()).put(e2.getKey(), diferencia_actual + diferencia_observada);
                }); //no calcularem la diferencia de valoracions sobre el mateix item
            });
        });

        //per cada item, fem la mitjana de les valoracions, es a dir, dividir diferencia entre frequencia
        diferencia.keySet().forEach(j -> diferencia.get(j).keySet().forEach(i -> {

            //diferencia
            double oldValue = diferencia.get(j).get(i);
            //frequencia
            double count = frequencia.get(j).get(i);
            //actualitzar valor
            diferencia.get(j).put(i, oldValue / count);
        }));

        HashMap<Integer, Double> valorP = new HashMap<>();
        HashMap<Integer, Double> freqP = new HashMap<>();

        //Inicialitza
        diferencia.keySet().stream().peek(j -> valorP.put(j, 0.0)).forEachOrdered((var j) -> freqP.put(j, 0.0));

        HashMap<Integer, HashMap<Integer, Double>> out = new HashMap<>();

        data3.entrySet().stream().map(e -> {
            e.getValue().keySet().forEach((var j) -> {
                diferencia.keySet().stream().filter(k2 -> (diferencia.containsKey(k2) && diferencia.get(k2).containsKey(j) && frequencia.containsKey(k2) && frequencia.get(k2).containsKey(j))).map(k2 -> {
                    double valor = (diferencia.get(k2).get(j) + e.getValue().get(j)) * frequencia.get(k2).get(j);
                    valorP.put(k2, valorP.get(k2) + valor);
                    return k2;
                }).forEachOrdered(k2 -> {
                    freqP.put(k2, freqP.get(k2) + frequencia.get(k2).get(j));
                });
            });
            return e;
        }).forEachOrdered(e -> {
            HashMap<Integer, Double> clean = new HashMap<>();
            valorP.keySet().stream().filter(j -> (freqP.get(j) > 0)).forEachOrdered(j -> {
                clean.put(j, valorP.get(j) / freqP.get(j));
            });
            e.getValue().keySet().forEach(j -> {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } 
                else if (!clean.containsKey(j)) {
                    clean.put(j, -1.0);
                }
            });
            out.put(e.getKey(), clean);
        });
        
        return out.get(clientID);
    }

    //Funcio que implementa l'algoritme Collaborative Filtering
    public Map<Integer, Double> CollaborativeFiltering() {
        //conjunt de tots els clients
        List<Integer> clients_totals = new ArrayList<>();
        
        //conjunt de resultat amb les recomanacions
        Map<Integer, Double> resultat = new HashMap<>();
        
        //conjunt dels grups de clients obtinguts aplicant l'algoritme kmean
        HashMap<HashMap<Integer, Double>, List<Integer>> kmean;
        
        //grup que pertany el nostre client
        List<Integer> group;
        
        //valoracions de tots els clients que pertanyen el grup on pertany el nostre client
        HashMap<Integer, HashMap<Integer, Double>> data_grup = new HashMap<>();
        
        //resultat que obtenim quan s'aplica l'algoritme slopeOne
        HashMap<Integer, Double> outputData;
        
        if (!data.containsKey(clientID)) return resultat;
        
        //calcul dels clients totals
        data.forEach((key, value) -> clients_totals.add(key));
        
        //fixem el parametre com a 4 en el sistema hi ha mes de 10 clients, en cas contrari k = 1
        int k;
        if (data.size() > 10) k = 4;
        else k = 1;
        
        //s'aplica l'algoritme kmean
        kmean = kmeans(clients_totals, k);
        
        //busquem el grup on pertany el nostre client
        group = find_group(kmean);

        if (group != null) {
            group.forEach(client -> data_grup.put(client, data.get(client)));
        }

        //s'aplica l'algoritme SlopeOne
        outputData = slopeOne(data_grup);

        //conjunt auxiliar
        Map<Integer, Double> aux = new HashMap<>();

        HashMap<Integer, Double> val = data.get(clientID);
        
        if (outputData != null) {
            for (Entry<Integer, Double> e : outputData.entrySet()) {
                if (e.getKey() != null) {
                    Integer s12 = e.getKey();
                    if (val.get(s12) == null) {
                        aux.put(s12, e.getValue());
                    }
                }
            }
        }

        aux = sortByComparator(aux, false);

        int top = 0;

        for (HashMap.Entry<Integer, Double> ep : aux.entrySet()) {
            if (top < 10) {
                resultat.put(ep.getKey(), Math.round(ep.getValue() * 100.0) / 100.0);
                ++top;
            }
        }

        resultat = sortByComparator(resultat, false);

        return resultat;
    }
    
    //Funcio que implementa l'algoritme Hibrid
    public Map<Integer, Double> Hibrid() throws Exception {
        
        //conjunt de recomanacions al aplicar collaborative filtering
        Map<Integer, Double> collaborative;
        
        //conjunt de recomanacions al aplicar content_based filtering
        Map<Integer, Double> content_based;
        
        //conjunt de recomanacions de fusionar les recomanacions obtingudes per content_based i coolaborative
        Map<Integer, Double> resultat_combinacio = new HashMap<>();

        //conjunt de recomanacions finals
        Map<Integer, Double> resultat = new HashMap<>();
        
        content_based = ContentBasedFiltering();

        collaborative = CollaborativeFiltering();
        
        for (HashMap.Entry<Integer, Double> ep : collaborative.entrySet()) {
            resultat_combinacio.put(ep.getKey(), Math.round(ep.getValue() * 65.0) / 100.0);
        }
        
        for (HashMap.Entry<Integer, Double> ep : content_based.entrySet()) {
            if (!resultat_combinacio.containsKey(ep.getKey()) || (resultat_combinacio.get(ep.getKey()) < ep.getValue())) {
                resultat_combinacio.put(ep.getKey(), ep.getValue());
            }
        }
        
        //ordenar les recomanacions segons la seva valoracio esperada
        resultat_combinacio = sortByComparator(resultat_combinacio, false);

        int top = 0;

        //agafar el top 10 de les valoracions esperades mes altes
        for (HashMap.Entry<Integer, Double> ep : resultat_combinacio.entrySet()) {
            if (top < 10) {
                resultat.put(ep.getKey(), Math.round(ep.getValue() * 100.0) / 100.0);
                ++top;
            }
        }

        resultat = sortByComparator(resultat, false);
        
        return resultat;
    }

    private void println(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}