package persistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe amb diverses utilitats per llegir i escriure fitxers a disc.
 *
 * @author Hector Godoy Creus
 */
public class FileIO {

    private final String filename;

    //Utilitzem File.separator per compatibilitat Windows/Unix
    FileIO(String filename) {
        this.filename = filename;
    }


    //-----------------------------------------------
    // Funcions relacionades amb la lectura d'un fitxer
    //-----------------------------------------------

    List<String> readFile() throws IOException {
        List<String> file = new ArrayList<>();

        FileReader fr = new FileReader(filename);
        Scanner scanner = new Scanner(fr);

        while (scanner.hasNextLine())
            file.add(scanner.nextLine());

        fr.close();

        return file;
    }

    Reader getReader() throws FileNotFoundException {
        return new FileReader(filename);
    }


    //-----------------------------------------------
    // Funcions relacionades amb l'escriptura d'un fitxer
    //-----------------------------------------------

    void writeLine(String content) throws IOException {
        File file = new File(filename);
        String filePath = new File(file.getAbsolutePath()).getParent();
        File fileDir = new File(filePath);
        //noinspection ResultOfMethodCallIgnored
        fileDir.mkdirs();
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
        out.write(content);
        out.write("\n");
        out.close();
    }

}
