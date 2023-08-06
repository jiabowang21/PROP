package presentacio.vistaRecomanacionsUsuari;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import domini.recomanacio.CtrlRecomanacio;
import domini.recomanacio.Recomanacio;
import domini.usuari.UserException;
import presentacio.CtrlPresentacio;
import presentacio.vistaLlistaItems.FilaLlistaItems;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.invokeLater;

public class VistaRecomanacionsUsuari {
    private JPanel panel;
    private JRadioButton contentBasedRadioButton;
    private JRadioButton collaborativeFilteringRadioButton;
    private JRadioButton hibridRadioButton;
    private JButton generarButton;
    private JScrollPane jScrollPane;

    public VistaRecomanacionsUsuari() {
        ButtonGroup b = new ButtonGroup();
        b.add(contentBasedRadioButton);
        b.add(collaborativeFilteringRadioButton);
        b.add(hibridRadioButton);
        contentBasedRadioButton.addActionListener(e -> mostrarRecomanacions());
        collaborativeFilteringRadioButton.addActionListener(e -> mostrarRecomanacions());
        hibridRadioButton.addActionListener(e -> mostrarRecomanacions());

        generarButton.addActionListener(e -> {
            JPanel loadingPanel = new JPanel(new BorderLayout());
            loadingPanel.add(new JLabel("Carregant...", JLabel.CENTER), BorderLayout.CENTER);
            jScrollPane.setViewportView(loadingPanel);
            String estrategia;
            //Comprovem el mètode seleccionat
            if (contentBasedRadioButton.isSelected()) estrategia = CtrlRecomanacio.ESTRATEGIA_CONTENT_BASED;
            else if (collaborativeFilteringRadioButton.isSelected())
                estrategia = CtrlRecomanacio.ESTRATEGIA_COLLABORATIVE;
            else estrategia = CtrlRecomanacio.ESTRATEGIA_HIBRID;
            //Iniciem el càlcul en un nou Thread per no congelar la interfície
            new Thread(() -> {
                try {
                    CtrlPresentacio.getInstance().getCtrlDomini().ctrlRecomanacio.obtenirRecomanacions(estrategia,
                            CtrlPresentacio.getInstance().getCtrlDomini().ctrlUsuari.getUsuariIniciat().getId());
                } catch (UserException ex) {
                    CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                mostrarRecomanacions();
            }).start();
        });
    }

    public void mostrarRecomanacions() {
        List<Recomanacio> recomanacions = new ArrayList<>();
        try {
            int estrategiaEscollida = contentBasedRadioButton.isSelected() ? 0 : collaborativeFilteringRadioButton.isSelected() ? 1 : 2;
            switch (estrategiaEscollida) {
                case 0:
                    recomanacions = CtrlPresentacio.getInstance().getCtrlDomini().ctrlRecomanacio.getLlistaConjuntRecomanacionsContent(
                            CtrlPresentacio.getInstance().getCtrlDomini().ctrlUsuari.getUsuariIniciat().getId()
                    );
                    break;
                case 1:
                    recomanacions = CtrlPresentacio.getInstance().getCtrlDomini().ctrlRecomanacio.getLlistaConjuntRecomanacionsCollab(
                            CtrlPresentacio.getInstance().getCtrlDomini().ctrlUsuari.getUsuariIniciat().getId()
                    );
                    break;
                case 2:
                    recomanacions = CtrlPresentacio.getInstance().getCtrlDomini().ctrlRecomanacio.getLlistaConjuntRecomanacionsHibrid(
                            CtrlPresentacio.getInstance().getCtrlDomini().ctrlUsuari.getUsuariIniciat().getId()
                    );
                    break;
            }
        } catch (UserException ex) {
            CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initList(recomanacions);
    }

    public void initList(List<Recomanacio> recomanacions) {
        if (recomanacions.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.add(new JLabel("No hi ha recomanacions per tu", JLabel.CENTER), BorderLayout.CENTER);
            jScrollPane.setViewportView(emptyPanel);
        } else {
            JPanel listPanel = new JPanel(new GridLayout(recomanacions.size(), 1));
            for (Recomanacio recomanacio : recomanacions) {
                try {
                    listPanel.add(new FilaLlistaItems(
                            CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.getItem(recomanacio.getItemID())).getItemPanel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            invokeLater(() -> {
                jScrollPane.setViewportView(listPanel);
                jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
            });
        }
    }

    public JPanel getPanel() {
        return panel;
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
        panel.setLayout(new GridLayoutManager(4, 6, new Insets(0, 0, 0, 0), -1, -1));
        jScrollPane = new JScrollPane();
        panel.add(jScrollPane, new GridConstraints(0, 0, 2, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contentBasedRadioButton = new JRadioButton();
        contentBasedRadioButton.setSelected(true);
        contentBasedRadioButton.setText("Content Based");
        panel.add(contentBasedRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Metode de recomanacio");
        panel.add(label1, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        collaborativeFilteringRadioButton = new JRadioButton();
        collaborativeFilteringRadioButton.setText("Collaborative Filtering");
        panel.add(collaborativeFilteringRadioButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hibridRadioButton = new JRadioButton();
        hibridRadioButton.setText("Hibrid");
        panel.add(hibridRadioButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generarButton = new JButton();
        generarButton.setText("Generar");
        panel.add(generarButton, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
