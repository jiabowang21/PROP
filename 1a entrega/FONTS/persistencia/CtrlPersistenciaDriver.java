package persistencia;

import utils.InOut;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CtrlPersistenciaDriver {
    public static void main(String[] args) {
        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 12:
                        return;
                    case 11:
                        setDirectoriDeTreballDriver(inOut);
                        break;
                    case 10:
                        writeValoracioDriver(inOut);
                        break;
                    case 9:
                        writeRecomanacioDriver(inOut);
                        break;
                    case 8:
                        writeItemDriver(inOut);
                        break;
                    case 7:
                        writeUsuariDriver(inOut);
                        break;
                    case 6:
                        readRecomanacionsTestUnknownDriver(inOut);
                        break;
                    case 5:
                        readRecomanacionsTestKnownDriver(inOut);
                        break;
                    case 4:
                        readValoracionsDriver(inOut);
                        break;
                    case 3:
                        readRecomanacionsDriver(inOut);
                        break;
                    case 2:
                        readItemsDriver(inOut);
                        break;
                    case 1:
                        readUsuarisDriver(inOut);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeHelp(InOut inOut) throws Exception {
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Llegir usuaris");
        inOut.writeln("2. Llegir items");
        inOut.writeln("3. Llegir recomanacions");
        inOut.writeln("4. Llegir valoracions");
        inOut.writeln("5. Llegir test rating known");
        inOut.writeln("6. Llegir test rating unknown");
        inOut.writeln("7. Escriure usuari");
        inOut.writeln("8. Escriure item");
        inOut.writeln("9. Escriure recomanacio");
        inOut.writeln("10. Escriure valoracio");
        inOut.writeln("11. Cambiar directori de treball");
        inOut.writeln("12. Sortir");
        inOut.write("[opcio] ");
    }

    private static void writeMatrix(List<List<String>> matrix, InOut inOut) throws Exception {
        int i = 0;
        for (List<String> row : matrix) {
            inOut.write(i + "," + row.size() + ". | ");
            for (String entry : row) {
                inOut.write(entry);
                inOut.write(" | ");
            }
            inOut.writeln();
            i++;
        }
    }

    private static void readUsuarisDriver(InOut inOut) throws Exception {
        writeMatrix(CtrlPersistencia.getInstance().readUsuaris(), inOut);
    }

    private static void readItemsDriver(InOut inOut) throws Exception {
        writeMatrix(CtrlPersistencia.getInstance().readItems(), inOut);
    }

    private static void readRecomanacionsDriver(InOut inOut) throws Exception {
        writeMatrix(CtrlPersistencia.getInstance().readRecomanacions(), inOut);
    }

    private static void readValoracionsDriver(InOut inOut) throws Exception {
        writeMatrix(CtrlPersistencia.getInstance().readValoracions(), inOut);
    }

    private static void readRecomanacionsTestKnownDriver(InOut inOut) throws Exception {
        writeMatrix(CtrlPersistencia.getInstance().readRecomanacionsTestKnown(), inOut);
    }

    private static void readRecomanacionsTestUnknownDriver(InOut inOut) throws Exception {
        writeMatrix(CtrlPersistencia.getInstance().readRecomanacionsTestUnknown(), inOut);
    }

    private static List<String> readParams(InOut inOut) throws Exception {
        inOut.writeln("Introdueix els parametres separats per linia, acabats amb la paraula quit");
        List<String> params = new LinkedList<>();
        String input = inOut.readline();
        while (!input.equals("quit")) {
            params.add(input);
            input = inOut.readline();
        }
        params.remove(0);
        return params;
    }

    private static void writeUsuariDriver(InOut inOut) throws Exception {
        CtrlPersistencia.getInstance().writeUsuari(readParams(inOut));
    }

    private static void writeItemDriver(InOut inOut) throws Exception {
        CtrlPersistencia.getInstance().writeItem(readParams(inOut));
    }

    private static void writeRecomanacioDriver(InOut inOut) throws Exception {
        CtrlPersistencia.getInstance().writeRecomanacio(readParams(inOut));
    }

    private static void writeValoracioDriver(InOut inOut) throws Exception {
        CtrlPersistencia.getInstance().writeValoracio(readParams(inOut));
    }

    private static void setDirectoriDeTreballDriver(InOut inOut) throws Exception {
        inOut.writeln("Escriu el nou directori de treball, absolut o relatiu a " + new File("").getAbsolutePath() + File.separator);
        inOut.readline();
        String newDir = inOut.readline();
        try {
            CtrlPersistencia.getInstance().setDirectoriDeTreball(newDir);
        } catch (IOException e) {
            inOut.writeln(e.getMessage());
            return;
        }
        inOut.writeln("Directori canviat correctament");
    }
}
