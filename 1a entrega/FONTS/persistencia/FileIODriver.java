package persistencia;

import utils.InOut;

public class FileIODriver {
    public static void main(String[] args) {
        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 3:
                        return;
                    case 2:
                        readFileDriver(inOut);
                        break;
                    case 1:
                        writeFileDriver(inOut);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static void writeHelp(InOut inOut) throws Exception {
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Escriure fitxer");
        inOut.writeln("2. Llegir fitxer");
        inOut.writeln("3. Sortir");
        inOut.write("[opcio] ");
    }

    private static void writeFileDriver(InOut inOut) throws Exception {
        inOut.write("Introdueix el nom del fitxer: ");
        String filename = inOut.readword();

        FileIO fileIO = new FileIO(filename);

        inOut.writeln("Introdueix el contingut del fitxer, acabant amb 'quit':");
        inOut.readblanks();
        String input = inOut.readline();
        while (!input.equals("quit")) {
            fileIO.writeLine(input);
            input = inOut.readline();
        }
    }

    private static void readFileDriver(InOut inOut) throws Exception {
        inOut.write("Introdueix el nom del fitxer: ");
        String filename = inOut.readword();

        FileIO fileIO = new FileIO(filename);

        for (String line : fileIO.readFile()) {
            inOut.writeln(line);
        }
    }
}
