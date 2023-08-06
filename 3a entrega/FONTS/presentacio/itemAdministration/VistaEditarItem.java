package presentacio.itemAdministration;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import presentacio.CtrlPresentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;

public class VistaEditarItem {
    public JPanel panel;
    private JButton canviarButton;
    private JPanel cardPanel;
    private JButton confirmarItemButton;
    private JComboBox<String> comboBox1;
    private JButton carregarAtributsButton;
    private JList<String> list1;
    private JSpinner spinner1;
    private HashMap<String, String> atributeMap;
    private ArrayList<String> atributeList;
    private List<String> itemAtr;
    private final HashMap<String, String> atributesChanged;
    private Integer idActual;

    @SuppressWarnings("SuspiciousMethodCalls")
    public VistaEditarItem(JFrame thisFrame) {

        idActual = 0;
        itemAtr = new ArrayList<>();
        atributesChanged = new HashMap<>();

        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultListModel<String> model = new DefaultListModel<>();

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
        canviarButton.addActionListener(e -> {
            String atrName = (String) comboBox1.getSelectedItem();
            String value = null;
            switch (atributeMap.get(Objects.requireNonNull(atrName))) {
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
            atributesChanged.put(atrName, value);
            list1.setModel(model);
        });
        carregarAtributsButton.addActionListener(e -> {
            if (!CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.existeixItem((Integer) spinner1.getValue())) {
                CtrlPresentacio.getInstance().displayError("L'item amb aquest identificador no existeix", thisFrame);
                spinner1.setValue(idActual);
                return;
            }
            idActual = (Integer) spinner1.getValue();
            comboBox1.removeAllItems();
            model.clear();
            try {
                atributeMap = CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.getAtributesByType();
                atributeList = CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.getAtributeList();
                itemAtr = CtrlPresentacio.getInstance().getCtrlDomini().escriureItem((Integer) spinner1.getValue());
            } catch (Exception a) {
                CtrlPresentacio.getInstance().displayError("S'ha produit un error llegint els atributs", thisFrame);
                a.printStackTrace();
            }
            for (int i = 0; i < atributeList.size(); i++) {
                String nomAtr = atributeList.get(i);
                if (nomAtr.equals("id")) continue;
                String val = itemAtr.get(i);
                if (val.equals("")) val = "NO DEFINIT";
                comboBox1.addItem(nomAtr);
                model.addElement(nomAtr + "-->" + val);
            }
            list1.setModel(model);
        });
        comboBox1.addActionListener(e -> {
            if (!e.getActionCommand().equals("comboBoxChanged")) return;
            if (comboBox1.getSelectedItem() == null) {
                cl.show(cardPanel, "emptyPanel");
                return;
            }
            switch (atributeMap.get(Objects.requireNonNull(comboBox1.getSelectedItem()).toString())) {
                case "BOOL":
                    boolean b = Boolean.parseBoolean(itemAtr.get(atributeList.indexOf(comboBox1.getSelectedItem())));
                    trueRadioButton.setSelected(b);
                    falseRadioButton.setSelected(!b);
                    cl.show(cardPanel, "boolPanel");
                    break;
                case "NUM":
                    numField.setText(itemAtr.get(atributeList.indexOf(comboBox1.getSelectedItem())));
                    cl.show(cardPanel, "numPanel");
                    break;
                case "CAT":
                    strField.setText(itemAtr.get(atributeList.indexOf(comboBox1.getSelectedItem())));
                    cl.show(cardPanel, "strPanel");
                    break;
                default:
                    cl.show(cardPanel, "emptyPanel");
            }
        });
        confirmarItemButton.addActionListener(e -> {
            for (Map.Entry<String, String> atr : atributesChanged.entrySet()) {
                try {
                    CtrlPresentacio.getInstance().getCtrlDomini().ctrlItems.modificarItem(idActual, atr.getKey(), atr.getValue());
                } catch (Exception ex) {
                    CtrlPresentacio.getInstance().displayError("S'ha produit un error afegint les modificacions al conjunt de l'atribut: " + atr.getKey(), thisFrame);
                    ex.printStackTrace();
                    return;
                }
            }
            CtrlPresentacio.getInstance().displayInfo("L'item s'ha actualitzat correctament!", thisFrame);
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(6, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        canviarButton = new JButton();
        canviarButton.setText("Canviar");
        panel4.add(canviarButton, new GridConstraints(4, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout(0, 0));
        panel4.add(cardPanel, new GridConstraints(2, 0, 2, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(5, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Introdueix el seu valor:");
        panel4.add(label1, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        confirmarItemButton = new JButton();
        confirmarItemButton.setText("Confirmar Item");
        panel5.add(confirmarItemButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Selecciona un atribut:");
        panel2.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        panel2.add(comboBox1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        carregarAtributsButton = new JButton();
        carregarAtributsButton.setText("Carregar Atributs");
        panel2.add(carregarAtributsButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinner1 = new JSpinner();
        panel2.add(spinner1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Introdueix l'identificador:");
        panel2.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel.add(scrollPane1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        list1 = new JList();
        scrollPane1.setViewportView(list1);
    }
}
