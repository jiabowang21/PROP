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

    private String file_usuaris = "data" + File.separator + FILE_USUARIS;
    private String file_items = "data" + File.separator + FILE_ITEMS;
    private String file_valoracions = "data" + File.separator + FILE_VALORACIONS;
    private String file_recomanacions = "data" + File.separator + FILE_RECOMANACIONS;
    private String file_ratings_test_known = "data" + File.separator + FILE_RATINGS_TEST_KNOWN;
    private String file_ratings_test_unknown = "data" + File.separator + FILE_RATINGS_TEST_UNKNOWN;

    //Instància única del controlador
    private static CtrlPersistencia instance;

    private CtrlPersistencia() {
    }

    //Creem una sola instància, que retornarem sempre
    public static CtrlPersistencia getInstance() {
        if (instance == null) instance = new CtrlPersistencia();
        return instance;
    }

    public void setFile_usuaris(String file_usuaris) throws IOException {
        File file = new File(file_usuaris);
        if (!file.exists()) throw new IOException("El fitxer no existeix");
        if (!file.isFile()) throw new IOException("La ruta indicada no correspon a un fitxer");
        this.file_usuaris = file_usuaris;
    }

    public void setFile_items(String file_items) throws IOException {
        File file = new File(file_items);
        if (!file.exists()) throw new IOException("El fitxer no existeix");
        if (!file.isFile()) throw new IOException("La ruta indicada no correspon a un fitxer");
        this.file_items = file_items;
    }

    public void setFile_valoracions(String file_valoracions) throws IOException {
        File file = new File(file_valoracions);
        if (!file.exists()) throw new IOException("El fitxer no existeix");
        if (!file.isFile()) throw new IOException("La ruta indicada no correspon a un fitxer");
        this.file_valoracions = file_valoracions;
    }

    public void setFile_recomanacions(String file_recomanacions) throws IOException {
        File file = new File(file_recomanacions);
        if (!file.exists()) throw new IOException("El fitxer no existeix");
        if (!file.isFile()) throw new IOException("La ruta indicada no correspon a un fitxer");
        this.file_recomanacions = file_recomanacions;
    }

    public void setFile_ratings_test_known(String file_ratings_test_known) throws IOException {
        File file = new File(file_ratings_test_known);
        if (!file.exists()) throw new IOException("El fitxer no existeix");
        if (!file.isFile()) throw new IOException("La ruta indicada no correspon a un fitxer");
        this.file_ratings_test_known = file_ratings_test_known;
    }

    public void setFile_ratings_test_unknown(String file_ratings_test_unknown) throws IOException {
        File file = new File(file_ratings_test_unknown);
        if (!file.exists()) throw new IOException("El fitxer no existeix");
        if (!file.isFile()) throw new IOException("La ruta indicada no correspon a un fitxer");
        this.file_ratings_test_unknown = file_ratings_test_unknown;
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

        file_usuaris = newDir + FILE_USUARIS;
        file_items = newDir + FILE_ITEMS;
        file_valoracions = newDir + FILE_VALORACIONS;
        file_recomanacions = newDir + FILE_RECOMANACIONS;
        file_ratings_test_known = newDir + FILE_RATINGS_TEST_KNOWN;
        file_ratings_test_unknown = newDir + FILE_RATINGS_TEST_UNKNOWN;
    }

    //-----------------------------------------------
    // Funcions relacionades amb la lectura de dades
    //-----------------------------------------------

    private List<List<String>> readData(String name) {
        try {
            //Llegeix una string en format csv i la transforma a llista d'strings
            return CsvUtils.parse(new FileIO(name).getReader(), new ArrayList<>());
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<List<String>> readUsuaris() {
        return readData(file_usuaris);
    }

    public List<List<String>> readItems() {
        return readData(file_items);
    }

    public List<List<String>> readRecomanacions() {
        return readData(file_recomanacions);
    }

    public List<List<String>> readValoracions() {
        return readData(file_valoracions);
    }

    public List<List<String>> readRecomanacionsTestKnown() {
        return readData(file_ratings_test_known);
    }

    public List<List<String>> readRecomanacionsTestUnknown() {
        return readData(file_ratings_test_unknown);
    }


    //-----------------------------------------------
    // Funcions relacionades amb l'escriptura de dades
    //-----------------------------------------------

    private void writeDataLine(String name, List<List<String>> data) {
        try {
            FileIO fileIO = new FileIO(name);
            for (int i = 0; i < data.size(); i++) {
                fileIO.writeLine(CsvUtils.toCsv(data.get(i)), i != 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeUsuari(List<List<String>> data) {
        writeDataLine(file_usuaris, data);
    }

    public void writeItem(List<List<String>> data) {
        writeDataLine(file_items, data);
    }

    public void writeRecomanacio(List<List<String>> data) {
        writeDataLine(file_recomanacions, data);
    }

    public void writeValoracio(List<List<String>> data) {
        writeDataLine(file_valoracions, data);
    }
}
