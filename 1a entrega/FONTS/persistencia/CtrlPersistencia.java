package persistencia;

import utils.CsvUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe controladora de la capa de persistència. Ofereix els mètodes per
 * llegir i escriure els fitxers de dades.
 *
 * @author Hector Godoy Creus
 */
public class CtrlPersistencia {
    //Noms dels fitxers a la persistència
    private final static String FILE_USUARIS = "users.csv";
    private final static String FILE_ITEMS = "items.csv";
    private final static String FILE_VALORACIONS = "ratings.db.csv";
    private final static String FILE_RECOMANACIONS = "recommendations.csv";
    private final static String FILE_RATINGS_TEST_KNOWN = "ratings.test.known.csv";
    private final static String FILE_RATINGS_TEST_UNKNOWN = "ratings.test.unknown.csv";

    private String dir = "data" + File.separator;

    //Instància única del controlador
    private static CtrlPersistencia instance;

    private CtrlPersistencia() {
    }

    //Creem una sola instància, que retornarem sempre
    public static CtrlPersistencia getInstance() {
        if (instance == null) instance = new CtrlPersistencia();
        return instance;
    }

    public void setDirectoriDeTreball(String directory) throws IOException {
        String newDir;
        if (directory.charAt(directory.length() - 1) == File.separatorChar)
            newDir = directory;
        else
            newDir = directory + File.separatorChar;

        File file = new File(newDir);
        if (!file.exists()) throw new IOException("El directori no existeix");
        if (file.isFile()) throw new IOException("La ruta indicada correspon a un fitxer");

        dir = newDir;
    }

    //-----------------------------------------------
    // Funcions relacionades amb la lectura de dades
    //-----------------------------------------------

    private List<List<String>> readData(String name) {
        try {
            return CsvUtils.parse(new FileIO(dir + name).getReader(), new ArrayList<>());
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<List<String>> readUsuaris() {
        return readData(FILE_USUARIS);
    }

    public List<List<String>> readItems() {
        return readData(FILE_ITEMS);
    }

    public List<List<String>> readRecomanacions() {
        return readData(FILE_RECOMANACIONS);
    }

    public List<List<String>> readValoracions() {
        return readData(FILE_VALORACIONS);
    }

    public List<List<String>> readRecomanacionsTestKnown() {
        return readData(FILE_RATINGS_TEST_KNOWN);
    }

    public List<List<String>> readRecomanacionsTestUnknown() {
        return readData(FILE_RATINGS_TEST_UNKNOWN);
    }


    //-----------------------------------------------
    // Funcions relacionades amb l'escriptura de dades
    //-----------------------------------------------

    private void writeDataLine(String name, List<String> data) {
        try {
            new FileIO(dir + name).writeLine(CsvUtils.toCsv(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeUsuari(List<String> data) {
        writeDataLine(FILE_USUARIS, data);
    }

    public void writeItem(List<String> data) {
        writeDataLine(FILE_ITEMS, data);
    }

    public void writeRecomanacio(List<String> data) {
        writeDataLine(FILE_RECOMANACIONS, data);
    }

    public void writeValoracio(List<String> data) {
        writeDataLine(FILE_VALORACIONS, data);
    }
}
