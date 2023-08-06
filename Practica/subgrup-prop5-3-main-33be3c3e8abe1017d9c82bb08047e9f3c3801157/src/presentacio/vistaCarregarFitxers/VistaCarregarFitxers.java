package presentacio.vistaCarregarFitxers;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import presentacio.CtrlPresentacio;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Classe que genera la vista per carregar fitxers de dades al sistema
 *
 * @author Hector Godoy Creus
 */
public class VistaCarregarFitxers {
    private JButton carregarUsuarisButton;
    private JPanel panel;
    private JButton carregarItemsButton;
    private JButton carregarValoracionsButton;
    private JButton carregarDesDeCarpetaButton;
    private JButton carregarRecomanacionsButton;

    public JPanel getPanel() {
        return panel;
    }

    private String openDirectory() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Seleccionar directori: ");
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            if (jFileChooser.getSelectedFile().isDirectory()) return jFileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public VistaCarregarFitxers() {
        carregarUsuarisButton.addActionListener(e -> {
            String fitxer = openFile();
            if (fitxer == null) return;
            try {
                //Indiquem el directori del fitxer
                CtrlPresentacio.getInstance().getCtrlDomini().setFile_usuaris(fitxer);
            } catch (IOException ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
            }
            try {
                //Llegim el conjunt i el carreguem al sistema
                CtrlPresentacio.getInstance().getCtrlDomini().llegirConjuntUsuaris();
                CtrlPresentacio.getInstance().displayInfo(
                        CtrlPresentacio.getInstance().getCtrlDomini().ctrlUsuari.getUsuaris().size() +
                                " usuaris carregats correctament", null);
                CtrlPresentacio.getInstance().mostrarVistaIniciarSessio();
            } catch (Exception ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
            }
        });

        carregarItemsButton.addActionListener(e -> {
            String fitxer = openFile();
            if (fitxer == null) return;
            try {
                //Indiquem el directori del fitxer
                CtrlPresentacio.getInstance().getCtrlDomini().setFile_items(fitxer);
            } catch (IOException ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
                return;
            }
            try {
                //Llegim el conjunt i el carreguem al sistema
                CtrlPresentacio.getInstance().getCtrlDomini().llegirConjuntItems();
                CtrlPresentacio.getInstance().displayInfo(
                        CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.getItems().size() +
                                " items carregats correctament", null);
                CtrlPresentacio.getInstance().mostrarVistaIniciarSessio();
            } catch (Exception ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
            }
        });

        carregarValoracionsButton.addActionListener(e -> {
            String fitxer = openFile();
            if (fitxer == null) return;
            try {
                //Indiquem el directori del fitxer
                CtrlPresentacio.getInstance().getCtrlDomini().setFile_valoracions(fitxer);
            } catch (IOException ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
            }
            try {
                //Llegim el conjunt i el carreguem al sistema
                CtrlPresentacio.getInstance().getCtrlDomini().llegirValoracions();
                CtrlPresentacio.getInstance().displayInfo(
                        CtrlPresentacio.getInstance().getCtrlDomini().ctrlValoracio.getValoracions().size() +
                                " valoracions carregades correctament", null);
                CtrlPresentacio.getInstance().mostrarVistaIniciarSessio();
            } catch (Exception ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
            }
        });

        carregarRecomanacionsButton.addActionListener(e -> {
            String fitxer = openFile();
            if (fitxer == null) return;
            try {
                //Indiquem el directori del fitxer
                CtrlPresentacio.getInstance().getCtrlDomini().setFile_recomanacions(fitxer);
            } catch (IOException ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
            }
            try {
                //Llegim el conjunt i el carreguem al sistema
                CtrlPresentacio.getInstance().getCtrlDomini().llegirRecomanacions();
                CtrlPresentacio.getInstance().displayInfo("Recomanacions carregades correctament", null);
                CtrlPresentacio.getInstance().mostrarVistaIniciarSessio();
            } catch (Exception ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
            }
        });

        carregarDesDeCarpetaButton.addActionListener(e -> {
            String directory = openDirectory();
            if (directory == null) return;
            try {
                //Indiquem el directori des d'on obrirem els fitxers
                CtrlPresentacio.getInstance().getCtrlDomini().setDirectoriDeTreball(directory);
            } catch (IOException ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
            }
            try {
                CtrlPresentacio.getInstance().getCtrlDomini().llegirValoracions();
                CtrlPresentacio.getInstance().getCtrlDomini().llegirConjuntItems();
                CtrlPresentacio.getInstance().getCtrlDomini().llegirConjuntUsuaris();
                CtrlPresentacio.getInstance().getCtrlDomini().llegirRecomanacions();
                CtrlPresentacio.getInstance().displayInfo("Dades carregades correctament", null);
                CtrlPresentacio.getInstance().mostrarVistaIniciarSessio();
            } catch (Exception ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
                ex.printStackTrace();
            }
        });
    }

    private String openFile() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Seleccionar fitxer: ");
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        for (FileFilter fileFilter : jFileChooser.getChoosableFileFilters()) {
            jFileChooser.removeChoosableFileFilter(fileFilter);
        }

        //Creem un filtre que només permet escollir fitxers csv
        jFileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isFile() || file.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "Comma separated values (.csv)";
            }
        });

        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            if (jFileChooser.getSelectedFile().isFile()) return jFileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        carregarUsuarisButton = new JButton();
        carregarUsuarisButton.setText("Carregar usuaris");
        panel.add(carregarUsuarisButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        carregarItemsButton = new JButton();
        carregarItemsButton.setText("Carregar items");
        panel.add(carregarItemsButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carregarValoracionsButton = new JButton();
        carregarValoracionsButton.setText("Carregar valoracions");
        panel.add(carregarValoracionsButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carregarDesDeCarpetaButton = new JButton();
        carregarDesDeCarpetaButton.setText("Carregar des de carpeta");
        panel.add(carregarDesDeCarpetaButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carregarRecomanacionsButton = new JButton();
        carregarRecomanacionsButton.setText("Carregar recomanacions");
        panel.add(carregarRecomanacionsButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
