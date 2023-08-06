package presentacio;

import com.formdev.flatlaf.intellijthemes.FlatHiberbeeDarkIJTheme;
import domini.CtrlDomini;
import domini.item.Item;
import domini.usuari.UserException;
import domini.usuari.Usuari;
import presentacio.iniciarSessio.VistaIniciarSessio;
import presentacio.itemAdministration.VistaManagementItems;
import presentacio.recomanacionsAdministration.VistaManagementRecomanacions;
import presentacio.registrarUsuari.VistaRegistrarUsuari;
import presentacio.userAdministration.VistaManagementUsuaris;
import presentacio.vistaCarregarFitxers.VistaCarregarFitxers;
import presentacio.vistaEstablirPuntuacioMaxima.VistaEstablirPuntuacioMaxima;
import presentacio.vistaPrincipal.VistaPrincipalAdmin;
import presentacio.vistaPrincipal.VistaPrincipalUsuari;
import presentacio.vistaRecomanacionsUsuari.VistaRecomanacionsUsuari;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class CtrlPresentacio {
    private static CtrlPresentacio instance;
    private final CtrlDomini ctrlDomini;
    public JFrame frame;
    private JPanel mainPanel;
    private CardLayout cLayout;

    private final VistaManagementItems vistaManagementItems;
    private final VistaPrincipalUsuari vistaPrincipalUsuari;
    private final VistaPrincipalAdmin vistaPrincipalAdmin;
    private final VistaIniciarSessio vistaIniciarSessio;
    private final VistaRegistrarUsuari vistaRegistrarUsuari;
    private final VistaRecomanacionsUsuari vistaRecomanacionsUsuari;
    private final VistaManagementUsuaris vistaManagementUsuaris;
    private final VistaManagementRecomanacions vistaManagementRecomanacions;

    public CtrlPresentacio() {
        frame = new JFrame("Sistema Recomanador");
        ctrlDomini = CtrlDomini.getInstance();
        FlatHiberbeeDarkIJTheme.setup();

        vistaManagementItems = new VistaManagementItems();
        vistaPrincipalUsuari = new VistaPrincipalUsuari();
        vistaPrincipalAdmin = new VistaPrincipalAdmin();
        vistaIniciarSessio = new VistaIniciarSessio();
        vistaRegistrarUsuari = new VistaRegistrarUsuari();
        vistaRecomanacionsUsuari = new VistaRecomanacionsUsuari();
        vistaManagementUsuaris = new VistaManagementUsuaris();
        vistaManagementRecomanacions = new VistaManagementRecomanacions();
    }

    public static CtrlPresentacio getInstance(){
        if (instance == null) instance = new CtrlPresentacio();
        return instance;
    }

    public void inicialitzarPresentacio() {
        mainPanel = new JPanel();
        cLayout = new CardLayout();
        mainPanel.setLayout(cLayout);

        mainPanel.add("vistaManagementItems", vistaManagementItems.getPanel());
        mainPanel.add("vistaPrincipalUsuari", vistaPrincipalUsuari.getPanel1());
        mainPanel.add("vistaPrincipalAdmin", vistaPrincipalAdmin.getPanel1());
        mainPanel.add("vistaIniciarSessio", vistaIniciarSessio.getPanel());
        mainPanel.add("vistaRegistrarUsuari", vistaRegistrarUsuari.getPanel());
        mainPanel.add("vistaRecomanacionsUsuari", vistaRecomanacionsUsuari.getPanel());
        mainPanel.add("vistaManagementUsuaris", vistaManagementUsuaris.getPanel());
        mainPanel.add("vistaManagementRecomanacions", vistaManagementRecomanacions.getPanel());

        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        mostrarVistaIniciarSessio();
    }

    final public CtrlDomini getCtrlDomini(){
        return ctrlDomini;
    }

    public void displayError(String errorMessage, JFrame viewFrame){
        if(viewFrame == null) viewFrame = frame;
        JOptionPane.showMessageDialog(viewFrame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayWarning(String warningMessage, JFrame viewFrame) {
        if (viewFrame == null) viewFrame = frame;
        JOptionPane.showMessageDialog(viewFrame, warningMessage, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public void displayInfo(String infoMessage, JFrame viewFrame) {
        if (viewFrame == null) viewFrame = frame;
        JOptionPane.showMessageDialog(viewFrame, infoMessage, "Informació", JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayCarregarFitxers() {
        JFrame frameDefinirAtributs = new JFrame("Carregar fitxers");
        frameDefinirAtributs.pack();
        frameDefinirAtributs.setContentPane(new VistaCarregarFitxers().getPanel());
        frameDefinirAtributs.setSize(new Dimension(600, 400));
        frameDefinirAtributs.setLocationRelativeTo(CtrlPresentacio.getInstance().frame);
        frameDefinirAtributs.setMinimumSize(new Dimension(550, 400));
        frameDefinirAtributs.setVisible(true);
    }

    public void displayEstablirMaxValoracio() {
        JFrame frameEstablirMaximaPuntuacio = new JFrame("Establir màxima valoració");
        frameEstablirMaximaPuntuacio.pack();
        frameEstablirMaximaPuntuacio.setContentPane(new VistaEstablirPuntuacioMaxima(frameEstablirMaximaPuntuacio).getPanel());
        frameEstablirMaximaPuntuacio.setSize(new Dimension(600, 400));
        frameEstablirMaximaPuntuacio.setLocationRelativeTo(CtrlPresentacio.getInstance().frame);
        frameEstablirMaximaPuntuacio.setMinimumSize(new Dimension(550, 400));
        frameEstablirMaximaPuntuacio.setVisible(true);
    }

    public void switchPanel(String nom) {
        cLayout.show(mainPanel, nom);
    }

    public void switchMenu(JMenuBar menuBar) {
        frame.setJMenuBar(menuBar);
    }

    public void initRecomanacionsList(List<Item> items) {
        new Thread(() -> vistaPrincipalUsuari.initList(items)).start();
    }

    public void initUserList(Map<String, Usuari> usuaris) {
        new Thread(() -> vistaManagementUsuaris.initList(usuaris)).start();
    }

    public void reloadUserList() {
        initUserList(ctrlDomini.ctrlUsuari.getUsuaris());
    }

    public void mostrarVistaUsuari() {
        switchPanel("vistaPrincipalUsuari");
        switchMenu(VistaPrincipalUsuari.getMenuBar());
        initRecomanacionsList(ctrlDomini.ctrlItems.getItems());
        vistaRecomanacionsUsuari.mostrarRecomanacions();
    }

    public void mostrarVistaAdmin() {
        switchPanel("vistaPrincipalAdmin");
        switchMenu(VistaPrincipalAdmin.getMenuBar());
        initUserList(ctrlDomini.ctrlUsuari.getUsuaris());
    }

    public void mostrarVistaIniciarSessio() {
        switchPanel("vistaIniciarSessio");
        switchMenu(VistaIniciarSessio.getMenuBar());
        try {
            //Tancar sessió en cas que hi hagi una iniciada
            ctrlDomini.ctrlUsuari.tancarSessio();
        } catch (UserException e) {
            // No hi havia sessió iniciada
        }
    }

}
