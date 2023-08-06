package presentacio.vistaLlistaItems;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import domini.item.Item;
import domini.item.atributs.AtributInvalidException;
import domini.usuari.UserException;
import domini.valoracio.Valoracio;
import domini.valoracio.ValoracioException;
import presentacio.CtrlPresentacio;
import presentacio.vistaUnItem.vistaUnItem;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Classe que genera la vista d'una fila corresponent a un ítem a
 * valorar a la pantalla principal
 *
 * @author Hector Godoy Creus
 */
public class FilaLlistaItems {
    private JTextArea descItem;
    private JSlider slider1;
    private JButton valorarButton;
    private JPanel itemPanel;
    private JLabel nomItem;
    private JLabel valoracioLabel;
    private JButton informacioButton;

    public FilaLlistaItems(Item item) {
        slider1.setMaximum((int) CtrlPresentacio.getInstance().getCtrlDomini().ctrlValoracio.getValoracioMaxima());
        Set<String> atributs = item.GetAtrC().keySet();
        boolean hasTitle = false;
        for (String atribut : atributs) {
            //Cerquem un atribut que es digui title, si no el trobem mostrem l'id
            if (atribut.contains("title")) {
                try {
                    //Utilitzem html pel text en negreta
                    nomItem.setText("<html><b>" + item.GetAtrC().get(atribut).get(0).getStringValue() + "</b></html>");
                    hasTitle = true;
                    break;
                } catch (AtributInvalidException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!hasTitle) {
            nomItem.setText(String.valueOf(item.getID()));
        }

        boolean hasDesc = false;
        for (String atribut : atributs) {
            //Cerquem els noms típics que té una descripció d'ítem
            if (atribut.contains("overview") || atribut.contains("description") || atribut.contains("synopsis")) {
                try {
                    descItem.setText(item.GetAtrC().get(atribut).get(0).getStringValue());
                    hasDesc = true;
                    break;
                } catch (AtributInvalidException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!hasDesc) {
            descItem.setText(String.valueOf(item.getID()));
        }

        descItem.setLineWrap(true);
        descItem.setWrapStyleWord(true);
        descItem.setEditable(false);
        descItem.setPreferredSize(descItem.getPreferredSize());

        //Si l'ítem ja estava valorat, posem el valor a l'slider i l'etiqueta
        try {
            int idUsuari = CtrlPresentacio.getInstance().getCtrlDomini().ctrlUsuari.getUsuariIniciat().getId();
            Set<Valoracio> valoracionsUsuari = CtrlPresentacio.getInstance().getCtrlDomini().ctrlValoracio.getValoracionsUsuari(idUsuari);
            boolean valorat = false;
            for (Valoracio val : valoracionsUsuari) {
                if (val.getItemID() == item.getID()) {
                    slider1.setValue(val.getValoracio().intValue() * 10);
                    valoracioLabel.setText(String.valueOf(val.getValoracio()));
                    valorat = true;
                    break;
                }
            }
            if (!valorat) valoracioLabel.setText("N/A");
        } catch (UserException e) {
            e.printStackTrace();
        } catch (ValoracioException e) {
            valoracioLabel.setText("N/A");
        }

        //Sincronitzem l'etiqueta amb l'slider
        slider1.addChangeListener(e -> valoracioLabel.setText(String.valueOf((double) slider1.getValue() / 10.0)));

        valorarButton.addActionListener(e -> {
            double valoracio = (double) slider1.getValue() / 10;
            try {
                CtrlPresentacio.getInstance().getCtrlDomini().ctrlValoracio.valorarItem(
                        CtrlPresentacio.getInstance().getCtrlDomini().ctrlUsuari.getUsuariIniciat().getId(),
                        item.getID(),
                        valoracio);
                CtrlPresentacio.getInstance().displayInfo("Item valorat correctament amb un " + valoracio, null);
            } catch (UserException ex) {
                CtrlPresentacio.getInstance().displayError(ex.getMessage(), null);
            }
        });

        informacioButton.addActionListener(e -> {
            JFrame frameUnItem = new JFrame("Detalls");
            frameUnItem.pack();
            frameUnItem.setLocationRelativeTo(CtrlPresentacio.getInstance().frame);
            frameUnItem.setContentPane(new vistaUnItem(item, nomItem.getText()).getItemPanel());
            frameUnItem.setSize(new Dimension(600, 400));
            frameUnItem.setMinimumSize(new Dimension(600, 400));
            frameUnItem.setVisible(true);
        });
    }

    public JPanel getItemPanel() {
        return itemPanel;
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
        itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        nomItem = new JLabel();
        nomItem.setText("Nom item");
        itemPanel.add(nomItem, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        descItem = new JTextArea();
        descItem.setText("Descripcio de l'item");
        itemPanel.add(descItem, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        itemPanel.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        slider1 = new JSlider();
        slider1.setMaximum(50);
        slider1.setPaintLabels(false);
        slider1.setPaintTicks(false);
        slider1.setValue(0);
        panel1.add(slider1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valoracioLabel = new JLabel();
        valoracioLabel.setText("0");
        panel1.add(valoracioLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        itemPanel.add(panel2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorarButton = new JButton();
        valorarButton.setText("Valorar");
        panel2.add(valorarButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        informacioButton = new JButton();
        informacioButton.setText("Informació");
        panel2.add(informacioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        separator1.setBackground(new Color(-983553));
        separator1.setForeground(new Color(-1));
        itemPanel.add(separator1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 5), new Dimension(-1, 5), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return itemPanel;
    }

}
