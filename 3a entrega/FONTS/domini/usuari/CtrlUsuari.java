package domini.usuari;

import persistencia.CtrlPersistencia;

import java.util.*;

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

    private final Map<String, Usuari> usuaris = new HashMap<>();

    private CtrlUsuari() {
        usuaris.put("admin", new Administrador(1, "admin", "admin"));
    }

    public static CtrlUsuari getInstance() {
        if (instance == null) instance = new CtrlUsuari();
        return instance;
    }

    public Map<String, Usuari> getUsuaris() {
        return usuaris;
    }

    public void registrarUsuari(String username, String password, String tipus) throws UserException {
        if (usuaris.containsKey(username)) throw new UserException("L'usuari ja existeix");

        //Emmagatzemem els id dels usuaris per poder comprovar posteriorment si existeixen
        Set<Integer> userIds = new HashSet<>();
        for (String user : usuaris.keySet()) {
            userIds.add(usuaris.get(user).getId());
        }
        int newId = generateId();
        while (userIds.contains(newId)) newId = generateId();

        usuaris.put(username, tipus.equals(TYPE_CLIENT) ? new Client(newId, username, password) : new Administrador(newId, username, password));
    }

    private int generateId() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    public void iniciarSessio(String username, String password) throws UserException {
        if (usuariIniciat != null) throw new UserException("Ja hi ha una sessió iniciada");
        //Comprovem la validesa de l'usuari / contrasenya
        if (usuaris.containsKey(username)) {
            if (usuaris.get(username).getPassword().equals(password)) {
                usuariIniciat = usuaris.get(username);
                return;
            }
        }
        throw new UserException("Usuari o contrasenya invàlids");
    }

    public void tancarSessio() throws UserException {
        if (usuariIniciat == null) throw new UserException("No hi ha cap sessió iniciada.");
        usuariIniciat = null;
    }

    public void llegirUsuaris(List<List<String>> listUsuaris) {
        if (listUsuaris.isEmpty()) return;
        usuaris.clear();
        for (List<String> user : listUsuaris) {
            if (Objects.equals(user.get(USERTYPE_FIELD), TYPE_ADMIN)) {
                usuaris.put(user.get(USERNAME_FIELD), new Administrador(
                        Integer.parseInt(user.get(ID_FIELD)),
                        user.get(USERNAME_FIELD),
                        user.get(PASSWORD_FIELD)));
            } else {
                usuaris.put(user.get(USERNAME_FIELD), new Client(
                        Integer.parseInt(user.get(ID_FIELD)),
                        user.get(USERNAME_FIELD),
                        user.get(PASSWORD_FIELD)));
            }
        }
    }

    public Usuari getUsuariIniciat() throws UserException {
        if (usuariIniciat == null) throw new UserException("No hi ha cap sessió iniciada.");
        return usuariIniciat;
    }

    public void eliminarUsuari(String id) throws UserException {
        if (usuaris.containsKey(id)) usuaris.remove(id);
        else throw new UserException("L'usuari no existeix");
    }

    public void escriureUsuaris() {
        List<List<String>> usersToWrite = new LinkedList<>();
        for (String userId : usuaris.keySet()) {
            //Convertim l'usuari a una llista d'stings per desar-lo en el format correcte
            Usuari usuari = usuaris.get(userId);
            List<String> user = new LinkedList<>();
            user.add(usuari.getId().toString());
            user.add(usuari.getTipus());
            user.add(usuari.getUsername());
            user.add(usuari.getPassword());
            usersToWrite.add(user);
        }
        CtrlPersistencia ctrlPersistencia = CtrlPersistencia.getInstance();
        ctrlPersistencia.writeUsuari(usersToWrite);
    }
}
