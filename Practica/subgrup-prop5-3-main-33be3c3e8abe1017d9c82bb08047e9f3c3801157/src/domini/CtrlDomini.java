package domini;

import domini.item.CtrlItems;
import domini.recomanacio.CtrlRecomanacio;
import domini.usuari.CtrlUsuari;
import domini.valoracio.CtrlValoracio;
import persistencia.CtrlPersistencia;
import presentacio.CtrlPresentacio;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static javax.swing.SwingUtilities.invokeLater;


public class CtrlDomini {

    private static CtrlDomini instance;

    public CtrlUsuari ctrlUsuari;
    public CtrlItems ctrlItems;
    public CtrlValoracio ctrlValoracio;
    public CtrlRecomanacio ctrlRecomanacio;

    public CtrlDomini() {
        ctrlUsuari = CtrlUsuari.getInstance();
        ctrlItems = CtrlItems.getInstance();
        ctrlValoracio = CtrlValoracio.getInstance();
        ctrlRecomanacio = CtrlRecomanacio.getInstance();
    }

    public boolean atributsNoDefinits() {
        try {
            return ctrlItems.getAtributeList().isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    public static CtrlDomini getInstance() {
        if (instance == null) instance = new CtrlDomini();
        return instance;
    }

    public void llegirConjuntItems() throws Exception {
        ctrlItems.carregarConjuntItems(CtrlPersistencia.getInstance().readItems());
    }

    public List<String> escriureItem(int id) throws Exception {
        return ctrlItems.serialitzarItemPerId(id);
    }

    public void llegirConjuntUsuaris() {
        ctrlUsuari.llegirUsuaris(CtrlPersistencia.getInstance().readUsuaris());
    }

    public void llegirValoracions() {
        ctrlValoracio.carregarValoracions(CtrlPersistencia.getInstance().readValoracions());
    }

    public void llegirRecomanacions() throws ParseException {
        ctrlRecomanacio.carregaRecomanacions(CtrlPersistencia.getInstance().readRecomanacions());
    }

    public void llegirTest() throws ParseException {
        ctrlRecomanacio.carregaTest(CtrlPersistencia.getInstance().readRecomanacionsTestKnown());
    }

    public void desarEstat() {
        new Thread(() -> {
            try {
                ctrlUsuari.escriureUsuaris();
                ctrlItems.escriureItems();
                ctrlRecomanacio.escriureRecomanacions();
                ctrlValoracio.escriureValoracions();
                invokeLater(() -> CtrlPresentacio.getInstance().displayInfo("Dades guardades correctament", null));
            } catch (Exception e) {
                e.printStackTrace();
                invokeLater(() -> CtrlPresentacio.getInstance().displayError("Error guardant les dades", null));
            }
        }).start();
    }

    ///////////////////////////////////
    // Funcions per administrar la localització dels arxius de dades
    ///////////////////////////////////

    public void setFile_usuaris(String file_usuaris) throws IOException {
        CtrlPersistencia.getInstance().setFile_usuaris(file_usuaris);
    }

    public void setFile_items(String file_items) throws IOException {
        CtrlPersistencia.getInstance().setFile_items(file_items);
    }

    public void setFile_valoracions(String file_valoracions) throws IOException {
        CtrlPersistencia.getInstance().setFile_valoracions(file_valoracions);
    }

    public void setFile_recomanacions(String file_recomanacions) throws IOException {
        CtrlPersistencia.getInstance().setFile_recomanacions(file_recomanacions);
    }

    public void setFile_ratings_test_known(String file_ratings_test_known) throws IOException {
        CtrlPersistencia.getInstance().setFile_ratings_test_known(file_ratings_test_known);
    }

    public void setFile_ratings_test_unknown(String file_ratings_test_unknown) throws IOException {
        CtrlPersistencia.getInstance().setFile_ratings_test_unknown(file_ratings_test_unknown);
    }

    public void setDirectoriDeTreball(String directoriDeTreball) throws IOException {
        CtrlPersistencia.getInstance().setDirectoriDeTreball(directoriDeTreball);
    }
}
