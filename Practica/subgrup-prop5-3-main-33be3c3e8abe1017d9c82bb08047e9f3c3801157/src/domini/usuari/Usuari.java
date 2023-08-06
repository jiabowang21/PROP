package domini.usuari;

/**
 * Classe on s'emmagatzemen les estructures de dades corresponents
 * als usuaris.
 *
 * @author Hector Godoy Creus
 */
public abstract class Usuari {
    private final Integer id;
    private String username, password;

    Usuari(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract String getTipus();
}
