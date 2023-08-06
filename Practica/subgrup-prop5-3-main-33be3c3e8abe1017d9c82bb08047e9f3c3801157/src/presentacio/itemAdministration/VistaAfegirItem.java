package presentacio.itemAdministration;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import presentacio.CtrlPresentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class VistaAfegirItem {
    public JPanel panel;
    private JButton afegirButton;
    private JButton confirmarAtributsButton;
    private JComboBox<String> comboBox1;
    private JList<String> list1;
    private JButton carregarAtributsButton;
    private JPanel cardPanel;
    private HashMap<String, String> atributeList;

    public VistaAfegirItem(JFrame thisFrame) {

        //Llista per al JList
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultListModel<String> model = new DefaultListModel<>();

        //Conjunt d'elements pel panell que escolleix en funció del tipus d'atribut que s'ha seleccionat
        JTextField numField = new JTextField();
        JTextField strField = new JTextField();
        JRadioButton trueRadioButton = new JRadioButton("True", true);
        JRadioButton falseRadioButton = new JRadioButton("False");
        ButtonGroup bg = new ButtonGroup();
        bg.add(trueRadioButton);
        bg.add(falseRadioButton);
        JPanel numPanel = new JPanel(new BorderLayout());
        JPanel strPanel = new JPanel(new BorderLayout());
        JPanel boolPanel = new JPanel(new BorderLayout());
        JPanel emptyPanel = new JPanel();

        numPanel.add(numField);
        strPanel.add(strField);
        boolPanel.setLayout(new FlowLayout());
        boolPanel.add(trueRadioButton);
        boolPanel.add(falseRadioButton);

        cardPanel.add("numPanel", numPanel);
        cardPanel.add("strPanel", strPanel);
        cardPanel.add("boolPanel", boolPanel);
        cardPanel.add("emptyPanel", emptyPanel);
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "emptyCard");

        numField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                    CtrlPresentacio.getInstance().displayWarning("Aquest atribut és numèric i només accepta valors de 0-9", thisFrame);
                    e.consume();
                }
                super.keyTyped(e);
            }
        });

        afegirButton.addActionListener(e -> {
            String atrName = (String) comboBox1.getSelectedItem();
            String value = null;
            switch (atributeList.get(Objects.requireNonNull(atrName))) {
                case "BOOL":
                    if (trueRadioButton.isSelected()) value = "True";
                    else value = "False";
                    break;
                case "NUM":
                    value = numField.getText();
                    numField.setText("");
                    break;
                case "CAT":
                    value = strField.getText();
                    strField.setText("");
            }
            for (int i = 0; i < model.getSize(); i++) {
                String currentAtrName = model.get(i).split("-->")[0];
                if (atrName.equals(currentAtrName)) {
                    model.set(i, atrName + "-->" + value);
                    break;
                }
            }
            list1.setModel(model);
        });
        carregarAtributsButton.addActionListener(e -> {
            comboBox1.removeAllItems();
            model.clear();
            try {
                atributeList = CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.getAtributesByType();
            } catch (Exception a) {
                CtrlPresentacio.getInstance().displayError("S'ha produit un error llegint els atributs", thisFrame);
                a.printStackTrace();
            }
            for (String name : atributeList.keySet()) {
                comboBox1.addItem(name);
                model.addElement(name + "-->" + "NO DEFINIT");
            }
            list1.setModel(model);
        });
        comboBox1.addActionListener(e -> {
            if (!e.getActionCommand().equals("comboBoxChanged")) return;
            if (comboBox1.getSelectedItem() == null) {
                cl.show(cardPanel, "emptyPanel");
                return;
            }
            switch (atributeList.get(Objects.requireNonNull(comboBox1.getSelectedItem()).toString())) {
                case "BOOL":
                    cl.show(cardPanel, "boolPanel");
                    break;
                case "NUM":
                    cl.show(cardPanel, "numPanel");
                    break;
                case "CAT":
                    cl.show(cardPanel, "strPanel");
            }
        });
        confirmarAtributsButton.addActionListener(e -> {
            int id = CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.mida();
            ArrayList<String> values = new ArrayList<>();
            try {
                //Busquem un identificador lliure
                if (CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.existeixItem(id)) {
                    boolean idAvaliable = false;
                    for (int i = 0; i < 10 && !idAvaliable; i++) {
                        Random r = new Random();
                        int ran = r.nextInt(2 * id + 1);
                        if (!CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.existeixItem(ran)) {
                            idAvaliable = true;
                            id = ran;
                        }
                    }
                    if (!idAvaliable) throw new Exception();
                }

                for (String nomAtr : CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.getAtributeList()) {
                    if (nomAtr.equals("id")) {
                        values.add(String.valueOf(id));
                        continue;
                    }
                    for (int i = 0; i < model.getSize(); i++) {
                        String[] actVal = model.get(i).split("-->");
                        if (actVal[0].equals(nomAtr)) {
                            if (actVal[1].equals("NO DEFINIT"))
                                values.add("");
                            else values.add(actVal[1]);

                        }
                    }
                }
                CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.afegirItem(values);
            } catch (Exception ex) {
                CtrlPresentacio.getInstance().displayError("S'ha produit un error escribint els atributs", thisFrame);
                ex.printStackTrace();
                return;
            }
            CtrlPresentacio.getInstance().displayInfo("L'item amb ID: " + id + ". S'ha afegit correctament!", thisFrame);
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
        panel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(6, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        afegirButton = new JButton();
        afegirButton.setText("Afegir o Canviar");
        panel3.add(afegirButton, new GridConstraints(4, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout(0, 0));
        panel3.add(cardPanel, new GridConstraints(2, 0, 2, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(5, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Introdueix el seu valor:");
        panel3.add(label1, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        confirmarAtributsButton = new JButton();
        confirmarAtributsButton.setText("Confirmar Item");
        panel4.add(confirmarAtributsButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel4.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Selecciona un atribut:");
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        panel1.add(comboBox1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carregarAtributsButton = new JButton();
        carregarAtributsButton.setText("Carregar Atributs");
        panel1.add(carregarAtributsButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel.add(scrollPane1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        list1 = new JList();
        scrollPane1.setViewportView(list1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
