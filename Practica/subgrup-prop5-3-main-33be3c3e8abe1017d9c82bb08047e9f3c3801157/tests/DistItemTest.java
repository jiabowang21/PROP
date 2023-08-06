import domini.distancia.DistItem;
import domini.item.CtrlItems;
import domini.item.Item;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Jiabo Wang
 */

public class DistItemTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(DistItemTest.class);
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
    public void test1() throws Exception {
        Double dist = generalTest("genere empresa id ", "ficcio marvel 1 ", "romance marvel 2 ");
        assertEquals(0.5, dist, 0);
    }

    @Test
    public void test2() throws Exception {
        Double dist = generalTest("genere empresa id ", "ficcio marvela 1 ", "romance marvel 2 ");
        assertEquals(1.0, dist, 0);
    }

    @Test
    public void test3() throws Exception {
        Double dist = generalTest("genere empresa id ", "ficcio marvela 1 ", "ficcio marvela 2 ");
        assertEquals(0.0, dist, 0);
    }

    @Test
    public void test4() throws Exception {
        Double dist = generalTest("genere empresa id ", "ficcio;romance marvela 1 ", "ficcio marvela 2 ");
        assertEquals(0.33, dist, 0);
    }

    public Double generalTest(String in1, String in2, String in3) throws Exception {
        List<List<String>> llistaItems = new ArrayList<>();
        List<String> l;
        ArrayList<Item> cjtItems;
        l = Arrays.asList(in1.trim().split("\\s+"));
        llistaItems.add(l);
        //Atributs item 1
        l = Arrays.asList(in2.trim().split("\\s+"));
        llistaItems.add(l);
        //Atributs item 2
        l = Arrays.asList(in3.trim().split("\\s+"));
        llistaItems.add(l);
        CtrlItems ctrlI = CtrlItems.getInstance();
        ctrlI.carregarConjuntItems(llistaItems);
        cjtItems = CtrlItems.getInstance().getItems();
        DistItem i = new DistItem(cjtItems.get(0), cjtItems.get(1));
        return i.getDistancia();
    }

}

