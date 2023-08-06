package domini.usuari;

/**
 * Classe on s'emmagatzemen les estructures de dades corresponents
 * als usuaris.
 *
 * @author Hector Godoy Creus
 */
public abstract class Usuari {
    private final Integer id;
    private final String username;

    Usuari(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public abstract String getTipus();
}
