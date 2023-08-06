package domini.usuari;

import utils.InOut;

public class CtrlUsuariDriver {
    public static void main(String[] args) {
        InOut inOut = new InOut();
        for (; ; ) {
            try {
                writeHelp(inOut);
                switch (inOut.readint()) {
                    case 5:
                        return;
                    case 4:
                        getUsuariIniciatDriver(inOut);
                        break;
                    case 3:
                        tancarSessioDriver(inOut);
                        break;
                    case 2:
                        iniciarSessioDriver(inOut);
                        break;
                    case 1:
                        registrarUsuariDriver(inOut);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeHelp(InOut inOut) throws Exception {
        inOut.writeln("Que vols fer?");
        inOut.writeln("1. Registrar usuari");
        inOut.writeln("2. Iniciar sessio");
        inOut.writeln("3. Tancar sessio");
        inOut.writeln("4. Obtenir usuari iniciat");
        inOut.writeln("5. Sortir");
        inOut.write("[opcio] ");
    }

    private static void registrarUsuariDriver(InOut inOut) throws Exception {
        inOut.write("Introdueix nom d'usuari: ");
        String username = inOut.readword();
        inOut.write("Introdueix contrasenya: ");
        String pass = inOut.readword();
        inOut.write("És admin? [s/n] ");
        String admin = inOut.readword();
        try {
            CtrlUsuari.getInstance().registrarUsuari(username, pass, admin.equalsIgnoreCase("s") ? CtrlUsuari.TYPE_ADMIN : CtrlUsuari.TYPE_CLIENT);
        } catch (UserException e) {
            inOut.writeln(e.getMessage());
        }
    }

    private static void iniciarSessioDriver(InOut inOut) throws Exception {
        inOut.write("Usuari: ");
        String username = inOut.readword();
        inOut.write("Contrasenya: ");
        String pass = inOut.readword();
        try {
            CtrlUsuari.getInstance().iniciarSessio(username, pass);
        } catch (UserException e) {
            inOut.writeln(e.getMessage());
            return;
        }
        inOut.writeln("Sessio iniciada");
    }

    private static void tancarSessioDriver(InOut inOut) throws Exception {
        try {
            CtrlUsuari.getInstance().tancarSessio();
        } catch (UserException e) {
            inOut.writeln(e.getMessage());
            return;
        }
        inOut.writeln("Sessio tancada");
    }

    private static void getUsuariIniciatDriver(InOut inOut) throws Exception {
        Usuari usuari;
        try {
            usuari = CtrlUsuari.getInstance().getUsuariIniciat();
        } catch (UserException e) {
            inOut.writeln(e.getMessage());
            return;
        }
        inOut.writeln("Usuari iniciat: id=" + usuari.getId() + ", username=" + usuari.getUsername());
    }
}
