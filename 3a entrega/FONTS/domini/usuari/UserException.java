package domini.usuari;

/**
 * Classe que representa una excepció relacionada amb
 * el sistema d'usuaris.
 *
 * @author Hector Godoy Creus
 */
public class UserException extends Exception {
    public UserException(String errorMessage) {
        super(errorMessage);
    }
}
