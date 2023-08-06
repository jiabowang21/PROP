import presentacio.CtrlPresentacio;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * Classe principal que s'encarrega de la inicialització del programa.
 *
 * @author Hector Godoy Creus
 */
public class SistemaRecomanadorPROP {
    public static void main(String[] args) {
        invokeLater(() -> CtrlPresentacio.getInstance().inicialitzarPresentacio());
    }
}
