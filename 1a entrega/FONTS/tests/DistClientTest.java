import domini.distancia.DistClient;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Jiabo Wang
 */

public class DistClientTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(DistClientTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("Todos los test han ido bien");
        } else {
            System.out.println("Algun test ha ido mal");
        }
    }

    @Test
    public void test1() {
        HashMap<Integer, Double> f = new HashMap<>();
        HashMap<Integer, Double> f2 = new HashMap<>();
        f.put(1, 2.0);
        f.put(2, 5.0);
        f.put(3, 1.0);
        f2.put(1, 2.0);
        f2.put(2, 5.0);
        f2.put(3, 1.5);
        DistClient d = new DistClient(f, f2);
        assertEquals(0.5, d.getDistancia(), 0);
    }

    @Test
    public void test2() {
        HashMap<Integer, Double> f = new HashMap<>();
        HashMap<Integer, Double> f2 = new HashMap<>();
        f.put(114, 5.0);
        f.put(258, 3.0);
        f.put(324, 2.5);
        f2.put(114, 1.0);
        f2.put(258, 0.0);
        DistClient d = new DistClient(f, f2);
        assertEquals(5.59, d.getDistancia(), 0);
    }

    @Test
    public void test3() {
        HashMap<Integer, Double> f = new HashMap<>();
        HashMap<Integer, Double> f2 = new HashMap<>();
        f.put(999, 4.5);
        f.put(124353, 2.0);
        f.put(1243, 1.0);
        f2.put(123, 2.5);
        f2.put(245, 3.0);
        f2.put(354, 1.0);
        DistClient d = new DistClient(f, f2);
        assertEquals(6.44, d.getDistancia(), 0);
    }

    @Test
    public void test4() {
        HashMap<Integer, Double> f = new HashMap<>();
        HashMap<Integer, Double> f2 = new HashMap<>();
        f.put(1090, 5.0);
        f.put(120, 0.0);
        f.put(340, 0.0);
        f2.put(1090, 0.0);
        f2.put(120, 5.0);
        f2.put(340, 5.0);
        DistClient d = new DistClient(f, f2);
        assertEquals(8.66, d.getDistancia(), 0);
    }

    @Test
    public void test5() {
        HashMap<Integer, Double> f = new HashMap<>();
        HashMap<Integer, Double> f2 = new HashMap<>();
        f.put(1, 2.0);
        f.put(2, 4.0);
        f.put(3, 0.5);
        f2.put(1, 2.5);
        f2.put(2, 3.5);
        f2.put(3, 1.0);
        DistClient d = new DistClient(f, f2);
        assertEquals(0.87, d.getDistancia(), 0);
    }

}
