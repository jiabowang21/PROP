package domini.sistemarecomanador;

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
public class SistemaRecomanador {

    //random value
    //
    private static final Random random = new Random();
    private final HashMap<Integer, HashMap<Integer, Double>> data;
    private final ArrayList<Item> items_totals;
    private final Integer clientID;

    //constructora
    public SistemaRecomanador(HashMap<Integer, Set<Valoracio>> c, ArrayList<Item> i, Integer x) {
        items_totals = i;
        clientID = x;
        data = new HashMap<>();
        for (Entry<Integer, Set<Valoracio>> e : c.entrySet()) {
            HashMap<Integer, Double> va = new HashMap<>();
            for (Valoracio v : e.getValue()) {
                va.put(v.getItemID(), v.getValoracio());
            }
            data.put(e.getKey(), va);
        }
    }

    //Funcio que ordena un map pels seus valors
    private static Map<Integer, Double> sortByComparator(Map<Integer, Double> unsortMap, final boolean order) {

        List<Entry<Integer, Double>> list = new LinkedList<>(unsortMap.entrySet());

        list.sort((o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<Integer, Double> sortedMap = new LinkedHashMap<>();
        for (Entry<Integer, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    //Implementa l'algoritme kNN
    private Map<Integer, Double> kNN(ArrayList<Item> C, HashMap<Integer, Double> val) throws Exception {
        Map<Integer, Double> k1 = new HashMap<>();
        int k = 10;
        for (Item i1 : C) {
            Map<Integer, Double> k2 = new HashMap<>();
            for (Item i2 : items_totals) {
                Integer iaux2 = i2.getID();
                if (iaux2 != i1.getID() && !val.containsKey(iaux2)) {
                    DistItem distancia1 = new DistItem(i1, i2);
                    k2.put(iaux2, distancia1.getDistancia());
                }
            }
            k2 = sortByComparator(k2, true);
            int count = 0;
            for (HashMap.Entry<Integer, Double> epp : k2.entrySet()) {
                if (count < k) {
                    Integer kkey = epp.getKey();
                    Double f = k1.get(kkey);
                    if (f == null || f < k2.get(kkey)) {
                        k1.put(kkey, k2.get(kkey));
                        ++count;
                    }
                }
            }
        }
        Map<Integer, Double> res = new HashMap<>();
        k1 = sortByComparator(k1, true);
        int count = 0;
        double max = 0.0;
        for (HashMap.Entry<Integer, HashMap<Integer, Double>> entry : data.entrySet()) {
            for (HashMap.Entry<Integer, Double> entry2 : entry.getValue().entrySet()) {
                if (entry2.getValue() > max) max = entry2.getValue();
            }
        }
        Iterator<Integer> itr = k1.keySet().iterator();
        while (count < k && itr.hasNext()) {
            Integer kkey = itr.next();
            res.put(kkey, (1 - k1.get(kkey)) * max);
            ++count;
        }
        res = sortByComparator(res, false);
        return res;
    }

    //Implementa l'algoritme ContentBased Filtering
    public Map<Integer, Double> ContentBasedFiltering() throws Exception {
        HashMap<Integer, Double> v1 = new HashMap<>();
        if (data.containsKey(clientID)) v1 = data.get(clientID);
        else return v1;
        ArrayList<Item> C = new ArrayList<>();
        for (HashMap.Entry<Integer, Double> entry : v1.entrySet()) {
            if (entry.getValue() >= 3.5) {
                for (Item i : items_totals) {
                    if (i.getID() == entry.getKey()) {
                        C.add(i);
                    }
                }
            }
        }
        return kNN(C, v1);
    }

    //Generacio de centroides random
    private List<HashMap<Integer, Double>> CentroidesRandom(List<Integer> clients, int k) {
        List<HashMap<Integer, Double>> centroids = new ArrayList<>();
        HashMap<Integer, Double> max_values = new HashMap<>();
        HashMap<Integer, Double> min_values = new HashMap<>();
        Set<Integer> attributes = new HashSet<>();
        for (Integer client : clients) {
            data.get(client).forEach((key, value) -> {
                //valor maxim
                max_values.compute(key, (k1, max) -> max == null || value > max ? value : max);

                //valor minim
                min_values.compute(key, (k1, min) -> min == null || value < min ? value : min);

                attributes.add(key);
            });
        }
        for (int i = 0; i < k; ++i) {
            HashMap<Integer, Double> coordinates = new HashMap<>();
            for (Integer attribute : attributes) {
                double max = max_values.get(attribute);
                double min = min_values.get(attribute);
                coordinates.put(attribute, random.nextDouble() * (max - min) + min);
            }
            centroids.add(coordinates);
        }
        return centroids;
    }

    //Donat un client, hem de buscar el centroide del cluster mes proper a ell
    private HashMap<Integer, Double> CentroideMesProper(Integer client, List<HashMap<Integer, Double>> centroids) {
        double minimumDistance = Double.MAX_VALUE;
        HashMap<Integer, Double> nearest = null;
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

    //Assignar a cada client al cluster mes proper
    private void assignarCluster(HashMap<HashMap<Integer, Double>, List<Integer>> clusters, Integer client, HashMap<Integer, Double> centroid) {
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(client);
            return list;
        });
    }

    //Si despres d'una iteracio, un centroide no te cap client, no s'ha de recolocar. 
    //Sino, hem de recolocar el centroide de cada cluster, que es fer la mitjana de totes les valoracions dels clients que formen part
    private HashMap<Integer, Double> mitjana(HashMap<Integer, Double> centroid, List<Integer> clients) {
        if (clients == null || clients.isEmpty()) {
            return centroid;
        }

        for (Integer client1 : clients) {
            HashMap<Integer, Double> valors = data.get(client1);
            for (HashMap.Entry<Integer, Double> entry : valors.entrySet()) {
                centroid.put(entry.getKey(), 0.0);
            }
        }
        for (Integer client : clients) {
            data.get(client).forEach(
                    (k, v) -> centroid.compute(k, (k1, currentValue) -> v + (currentValue != null ? currentValue : 0.0)));
        }
        centroid.forEach((k, v) -> centroid.put(k, v / clients.size()));

        return centroid;
    }

    //Recololocacio de tots els centroides
    private List<HashMap<Integer, Double>> recolocacioCentroides(HashMap<HashMap<Integer, Double>, List<Integer>> clusters) {
        return clusters.entrySet().stream().map(e -> mitjana(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    //algoritme kmeans
    private HashMap<HashMap<Integer, Double>, List<Integer>> kmeans(List<Integer> clients, int k) {
        List<HashMap<Integer, Double>> centroids = CentroidesRandom(clients, k);
        HashMap<HashMap<Integer, Double>, List<Integer>> clusters = new HashMap<>();
        HashMap<HashMap<Integer, Double>, List<Integer>> lastState = new HashMap<>();
        Boolean shouldTerminate = false;

        while (shouldTerminate.equals(false)) {

            for (Integer client : clients) {
                HashMap<Integer, Double> centroid = CentroideMesProper(client, centroids);
                assignarCluster(clusters, client, centroid);
            }

            if (clusters.equals(lastState)) shouldTerminate = true;
            lastState = clusters;

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
    private HashMap<Integer, Double> slopeOne(HashMap<Integer, HashMap<Integer, Double>> data) {

        HashMap<Integer, Map<Integer, Double>> diferencia = new HashMap<>();

        HashMap<Integer, Map<Integer, Double>> frequencia = new HashMap<>();
        //calcul de frequencies i diferencies
        for (HashMap.Entry<Integer, HashMap<Integer, Double>> user : data.entrySet()) {
            //per cada Item 
            for (HashMap.Entry<Integer, Double> e : user.getValue().entrySet()) {

                //inicialitzar el valor, si era buit
                if (!diferencia.containsKey(e.getKey())) {
                    diferencia.put(e.getKey(), new HashMap<>());
                    frequencia.put(e.getKey(), new HashMap<>());
                }

                //recorre tots els altres items per calcular les diferencies
                for (HashMap.Entry<Integer, Double> e2 : user.getValue().entrySet()) {
                    //no calcularem la diferencia de valoracions sobre el mateix item
                    if (!e.getKey().equals(e2.getKey())) {

                        //ens guardem les frequencies dels items
                        double cont = 0.0;

                        if (frequencia.get(e.getKey()).containsKey(e2.getKey())) {
                            cont = frequencia.get(e.getKey()).get(e2.getKey());
                        }

                        frequencia.get(e.getKey()).put(e2.getKey(), cont + 1.0);

                        //la diferencia actual, ess a dir la que hem anat acumulant
                        double diferencia_actual = 0.0;

                        if (diferencia.get(e.getKey()).containsKey(e2.getKey())) {
                            diferencia_actual = diferencia.get(e.getKey()).get(e2.getKey());
                        }

                        //la diferencia observada la diferencia entre les dues valoracions
                        double diferencia_observada = e.getValue() - e2.getValue();

                        diferencia.get(e.getKey()).put(e2.getKey(), diferencia_actual + diferencia_observada);
                    }
                }
            }
        }

        //per cada item, fem la mitjana de les valoracions, es a dir, dividir diferencia entre frequencia
        for (Integer j : diferencia.keySet()) {
            for (Integer i : diferencia.get(j).keySet()) {
                //diferencia
                double oldValue = diferencia.get(j).get(i);
                //frequencia
                double count = frequencia.get(j).get(i);
                //actualitzar valor
                diferencia.get(j).put(i, oldValue / count);
            }
        }

        HashMap<Integer, Double> valorP = new HashMap<>();
        HashMap<Integer, Double> freqP = new HashMap<>();

        //Inicialitza
        for (Integer j : diferencia.keySet()) {
            valorP.put(j, 0.0);
            freqP.put(j, 0.0);
        }

        for (int j : data.get(clientID).keySet()) {
            for (int k2 : diferencia.keySet()) {
                if (k2 != j && diferencia.containsKey(k2) && diferencia.get(k2).containsKey(j) && frequencia.containsKey(k2) && frequencia.get(k2).containsKey(j)) {
                        double valor = (diferencia.get(k2).get(j) + data.get(clientID).get(j)) * frequencia.get(k2).get(j);
                        valorP.put(k2, valorP.get(k2) + valor);
                        freqP.put(k2, freqP.get(k2) + frequencia.get(k2).get(j));
                } 
            }
        }
        HashMap<Integer, Double> clean = new HashMap<>();
        for (Integer j : valorP.keySet()) {
            if (freqP.get(j) > 0) {
                clean.put(j, valorP.get(j) / freqP.get(j));
            }
        }
        return clean;
    }

    //Funcio que implementa l'algoritme Collaborative Filtering
    public Map<Integer, Double> CollaborativeFiltering() {
        List<Integer> clients_totals = new ArrayList<>();
        Map<Integer, Double> resultat = new HashMap<>();
        if (!data.containsKey(clientID)) return resultat;
        
        for (Entry<Integer, HashMap<Integer, Double>> ef : data.entrySet()) {
            clients_totals.add(ef.getKey());
        }
        HashMap<HashMap<Integer, Double>, List<Integer>> kmean;
        int k;
        if (data.size() > 10) k = 5;
        else k = 1;

        kmean = kmeans(clients_totals, k);

        List<Integer> group;

        group = find_group(kmean);

        HashMap<Integer, HashMap<Integer, Double>> data3 = new HashMap<>();

        if (group != null) {
            for (Integer client : group) {
                data3.put(client, data.get(client));
            }
        }

        HashMap<Integer, Double> outputData;

        outputData = slopeOne(data3);


        Map<Integer, Double> k1 = new HashMap<>();


        HashMap<Integer, Double> val = data.get(clientID);

        for (Entry<Integer, Double> e : outputData.entrySet()) {
            Integer s12 = e.getKey();
            if (val.get(s12) == null) {
                k1.put(s12, e.getValue());
            }
        }

        k1 = sortByComparator(k1, false);

        int top = 0;

        for (HashMap.Entry<Integer, Double> ep : k1.entrySet()) {
            if (top < 10) {
                resultat.put(ep.getKey(), ep.getValue());
                ++top;
            }
        }

        resultat = sortByComparator(resultat, false);

        return resultat;
    }
}

