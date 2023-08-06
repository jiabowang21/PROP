package domini.distancia;

import java.util.HashMap;

/**
 * @author Jiabo Wang
 */

public class DistClient extends Distancia {

    private final HashMap<Integer, Double> f1;
    private final HashMap<Integer, Double> f2;

    public DistClient(HashMap<Integer, Double> fz1, HashMap<Integer, Double> fz2) {
        f1 = fz1;
        f2 = fz2;
    }

    @Override
    public Double getDistancia() {
        double sum = 0;
        for (Integer key : f1.keySet()) {
            Double v1 = f1.get(key);
            Double v2 = f2.get(key);

            if (v1 != null && v2 != null) {
                sum += Math.pow(v1 - v2, 2);
            } 
            else if (v1 == null) {
                sum += Math.pow(v2, 2);
            } else {
                sum += Math.pow(v1, 2);
            }
        }
        for (Integer key : f2.keySet()) {
            Double v1 = f2.get(key);
            Double v2 = f1.get(key);
            if (v2 == null) {
                sum += Math.pow(v1, 2);
            }
        }
        
        double distancia = Math.sqrt(sum);
        return Math.round(distancia * 100.0) / 100.0;
    }
}
