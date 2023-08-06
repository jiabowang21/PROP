package utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class CsvUtilsDriver {
    public static void main(String[] args) {
        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 3:
                        return;
                    case 2:
                        toCsvDriver(inOut);
                        break;
                    case 1:
                        parseDriver(inOut);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeHelp(InOut inOut) throws Exception {
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Parse csv");
        inOut.writeln("2. Convertir a csv");
        inOut.writeln("3. Sortir");
        inOut.write("[opcio] ");
    }

    private static void parseDriver(InOut inOut) throws Exception {
        inOut.write("Llegir de teclat o de fitxer? [t/F] ");
        inOut.readblanks();
        char input = inOut.read();
        Reader reader;
        if (input == 't') {
            StringBuilder builder = new StringBuilder();
            inOut.writeln("Introdueix les linies del fitxer a parsejar, finalitzant amb 'quit'");
            inOut.readline();
            String lineInput = inOut.readline();
            while (!lineInput.equals("quit")) {
                builder.append(lineInput);
                builder.append('\n');
                lineInput = inOut.readline();
            }
            reader = new StringReader(builder.toString());
        } else {
            inOut.write("Escriu el nom del fitxer de csv: ");
            String line = inOut.readword();
            reader = new FileReader("./data/" + line);
        }

        List<List<String>> result;
        try {
            result = CsvUtils.parse(reader, new LinkedList<>());
        } catch (FileNotFoundException e) {
            inOut.writeln(e.getMessage());
            return;
        }
        int i = 0;
        for (List<String> row : result) {
            inOut.write(i + "," + row.size() + ". | ");
            for (String entry : row) {
                inOut.write(entry);
                inOut.write(" | ");
            }
            inOut.writeln();
            i++;
        }
    }

    private static void toCsvDriver(InOut inOut) throws Exception {
        inOut.writeln("Introdueix els atributs separats per linia, acabats amb la paraula quit");
        List<String> params = new LinkedList<>();
        String input = inOut.readline();
        while (!input.equals("quit")) {
            params.add(input);
            input = inOut.readline();
        }
        params.remove(0);
        inOut.writeln("CSV:");
        inOut.writeln(CsvUtils.toCsv(params));
    }
}
