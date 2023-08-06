package domini.usuari;

import persistencia.CtrlPersistencia;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Subcontroladora del domini encarregada de la gestió
 * dels usuaris.
 *
 * @author Hector Godoy Creus
 */
public class CtrlUsuari {
    public static final String TYPE_CLIENT = "type_client";
    public static final String TYPE_ADMIN = "type_admin";

    private static final int ID_FIELD = 0;
    private static final int USERTYPE_FIELD = 1;
    private static final int USERNAME_FIELD = 2;
    private static final int PASSWORD_FIELD = 3;

    private static CtrlUsuari instance;
    private Usuari usuariIniciat = null;

    private CtrlUsuari() {
    }

    public static CtrlUsuari getInstance() {
        if (instance == null) instance = new CtrlUsuari();
        return instance;
    }

    public void registrarUsuari(String username, String password, String tipus) throws UserException {
        Set<Integer> userIds = new HashSet<>();
        for (List<String> usuari : llegirUsuaris()) {
            if (usuari.get(USERNAME_FIELD).equals(username)) throw new UserException("L'usuari ja existeix");
            userIds.add(Integer.parseInt(usuari.get(ID_FIELD)));
        }
        int newId = generateId();
        while (userIds.contains(newId)) newId = generateId();
        escriureUsuari(tipus.equals(TYPE_CLIENT) ? new Client(newId, username) : new Administrador(newId, username),
                password);
    }

    private int generateId() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    public void iniciarSessio(String username, String password) throws UserException {
        if (usuariIniciat != null) throw new UserException("Ja hi ha una sessió iniciada");
        for (List<String> usuari : llegirUsuaris()) {
            if (usuari.get(USERNAME_FIELD).equalsIgnoreCase(username)) {
                if (usuari.get(PASSWORD_FIELD).equals(password)) {
                    if (usuari.get(USERTYPE_FIELD).equals(TYPE_CLIENT))
                        usuariIniciat = new Client(Integer.parseInt(usuari.get(ID_FIELD)), usuari.get(USERNAME_FIELD));
                    else
                        usuariIniciat = new Administrador(Integer.parseInt(usuari.get(ID_FIELD)), usuari.get(USERNAME_FIELD));
                    return;
                } else throw new UserException("Usuari o contrasenya invàlids");
            }
        }
        throw new UserException("Usuari o contrasenya invàlids");
    }

    public void tancarSessio() throws UserException {
        if (usuariIniciat == null) throw new UserException("No hi ha cap sessió iniciada.");
        usuariIniciat = null;
    }

    private List<List<String>> llegirUsuaris() {
        return CtrlPersistencia.getInstance().readUsuaris();
    }

    public Usuari getUsuariIniciat() throws UserException {
        if (usuariIniciat == null) throw new UserException("No hi ha cap sessió iniciada.");
        return usuariIniciat;
    }

    private void escriureUsuari(Usuari usuari, String contrasenya) {
        List<String> user = new LinkedList<>();
        user.add(usuari.getId().toString());
        user.add(usuari.getTipus());
        user.add(usuari.getUsername());
        user.add(contrasenya);

        CtrlPersistencia ctrlPersistencia = CtrlPersistencia.getInstance();
        ctrlPersistencia.writeUsuari(user);
    }
}
