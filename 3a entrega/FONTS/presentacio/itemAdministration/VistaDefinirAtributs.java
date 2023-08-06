package presentacio.itemAdministration;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import presentacio.CtrlPresentacio;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VistaDefinirAtributs {
    public JPanel panel;
    private JList<String> list1;
    private JButton esborrarAtributButton;
    private JButton confirmarAtributsButton;
    private JRadioButton booleaRadioButton;
    private JRadioButton numericRadioButton;
    private JRadioButton categoricRadioButton;
    private JButton afegirButton;
    private JTextField nomTextField;
    private JScrollPane scrollList;

    public VistaDefinirAtributs(JFrame thisFrame) {

        ButtonGroup bg = new ButtonGroup();
        bg.add(booleaRadioButton);
        bg.add(numericRadioButton);
        bg.add(categoricRadioButton);

        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultListModel<String> model = new DefaultListModel<>();

        afegirButton.addActionListener(e -> {
            if (nomTextField.getText().equals(""))
                CtrlPresentacio.getInstance().displayError("El camp del nom no pot estar buit", thisFrame);
            else if (!(booleaRadioButton.isSelected() || numericRadioButton.isSelected() || categoricRadioButton.isSelected()))
                CtrlPresentacio.getInstance().displayError("Cal seleccionar com a mínim un tipus d'atribut", thisFrame);
            else {
                String itemNameBool = "BOOL:" + nomTextField.getText();
                String itemNameNum = "NUM:" + nomTextField.getText();
                String itemNameCat = "CAT:" + nomTextField.getText();
                if (model.contains(itemNameBool) || model.contains(itemNameNum) || model.contains(itemNameCat)) {
                    CtrlPresentacio.getInstance().displayWarning("Aquest element ja existeix", thisFrame);
                    return;
                }
                if (booleaRadioButton.isSelected()) model.addElement(itemNameBool);
                else if (numericRadioButton.isSelected()) model.addElement(itemNameNum);
                else if (categoricRadioButton.isSelected()) model.addElement(itemNameCat);

                list1.setModel(model);
                nomTextField.setText("");
            }
        });
        confirmarAtributsButton.addActionListener(e -> {

            Object[] options = {"Confirmar", "Cancelar"};
            int option = JOptionPane.showOptionDialog(thisFrame
                    , "Aquesta operació eliminarà tots els items carregats i els atributs previament definits"
                    , "Confirmació", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (option != 0) return;

            ArrayList<String> atributsFromModel = Collections.list(model.elements());
            List<List<String>> atributsIItem = new ArrayList<>();
            List<String> atributs = new ArrayList<>();
            atributs.add("id");
            //Ens inventem un item serialitzat per a que la capa domini pugui identificar el tipus automaticament
            List<String> itemAux = new ArrayList<>();
            itemAux.add("0");
            for (String atr : atributsFromModel) {
                String[] parts = atr.split(":");
                atributs.add(parts[1]);
                switch (parts[0]) {
                    case "BOOL":
                        itemAux.add("True");
                        break;
                    case "NUM":
                        itemAux.add("3");
                        break;
                    case "CAT":
                        itemAux.add("asdf");
                }
            }
            atributsIItem.add(atributs);
            atributsIItem.add(itemAux);
            try {
                //Cridem a la funció responsable de la capa de domini per inicialitzar el conjunt amb els valors indicats
                CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.carregarConjuntItems(atributsIItem);
                //Eliminem l'item
                CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.eliminarItem(0);
                model.clear();
                list1.setModel(model);
                CtrlPresentacio.getInstance().displayInfo("La operació s'ha realitzat correctament!\nAquesta finestra es tancarà.", thisFrame);
                thisFrame.dispose();
            } catch (Exception ex) {
                CtrlPresentacio.getInstance().displayError("S'ha produit un error carregant els atributs:\n" + ex.getMessage(), thisFrame);
                ex.printStackTrace();
            }
        });
        esborrarAtributButton.addActionListener(e -> {
            int index = list1.getSelectedIndex();
            if (index >= 0) {
                model.removeElementAt(index);
                list1.setModel(model);
            } else CtrlPresentacio.getInstance().displayWarning("Has de seleccionar un item de la llista", thisFrame);
        });
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
        panel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.setEnabled(true);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Aquesta finestra permet definir atributs per a un tipus d'item");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        booleaRadioButton = new JRadioButton();
        booleaRadioButton.setText("Atribut Booleà");
        panel3.add(booleaRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numericRadioButton = new JRadioButton();
        numericRadioButton.setText("Atribut Numèric");
        panel3.add(numericRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoricRadioButton = new JRadioButton();
        categoricRadioButton.setText("Atribut Categòric");
        panel3.add(categoricRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        afegirButton = new JButton();
        afegirButton.setText("Afegir");
        panel2.add(afegirButton, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomTextField = new JTextField();
        panel2.add(nomTextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nom:");
        panel2.add(label2, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel2.add(spacer3, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel2.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        esborrarAtributButton = new JButton();
        esborrarAtributButton.setText("Esborrar Atribut");
        panel4.add(esborrarAtributButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel4.add(spacer5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel4.add(spacer6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        confirmarAtributsButton = new JButton();
        confirmarAtributsButton.setText("Confirmar Atributs");
        panel4.add(confirmarAtributsButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel4.add(spacer7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel5.add(spacer8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel5.add(scrollPane1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        list1 = new JList();
        scrollPane1.setViewportView(list1);
        final Spacer spacer9 = new Spacer();
        panel5.add(spacer9, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
