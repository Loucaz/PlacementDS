package ImportExport;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import placementds.Connexion;
import placementds.Etudiants;
import placementds.PlaceRules;
import placementds.Tables;

public class ImportWindow extends JFrame implements ActionListener {

    private final JButton button;
    private final JButton valider;
    private final JButton retour;
    private final JLabel message;
    private final JFileChooser fc = new JFileChooser();
    private String Nfile;
    private boolean fileCorrect;
    private Etudiants etu;

    public ImportWindow() {
        etu = new Etudiants();

        this.fileCorrect = false;
        this.Nfile = "";
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Import");
        this.setSize(480, 320);
        this.setLocationRelativeTo(null);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        Dimension preferredSize = new Dimension(300, 50);

        gc.gridwidth = 3;
        gc.gridx = 0;
        gc.gridy = 0;
        button = new JButton("Importer");
        button.setPreferredSize(preferredSize);
        button.addActionListener(this);
        this.add(button, gc);

        gc.gridy = 2;
        message = new JLabel();
        this.add(message, gc);

        preferredSize = new Dimension(100, 50);
        gc.gridwidth = 1;
        gc.gridy = 3;
        retour = new JButton("Retour");
        retour.setPreferredSize(preferredSize);
        retour.addActionListener(this);
        this.add(retour, gc);

        gc.gridx = 1;
        gc.gridwidth = 2;
        valider = new JButton("Valider");
        preferredSize = new Dimension(200, 50);
        valider.setPreferredSize(preferredSize);
        valider.addActionListener(this);
        this.add(valider, gc);

        this.setVisible(true);

    }

    public Etudiants getEtu() {
        return etu;
    }

    public void setEtu(Etudiants etu) {
        this.etu = etu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                Nfile = file.toString();
                int length = Nfile.length();
                String result = " ";
                if (length > 4) {
                    result = Nfile.substring(length - 4, length);
                }
                if ("xlsx".equals(result) || ".xls".equals(result)) {
                    message.setText(Nfile);
                    fileCorrect = true;
                } else {
                    message.setText("Ceci n'est pas un fichier Excel");
                    fileCorrect = false;
                }

            } else {
                message.setText("Ceci n'est pas un fichier Excel");
                fileCorrect = false;
            }

        }
        if (e.getSource() == valider) {
            if (fileCorrect) {
                ExcelReader r = new ExcelReader(Nfile);
                if (r.isGood()) {
                    etu.setEtuList(r.ajout());
                    creationPlaceRules();
                    this.dispose();
                } else {
                    message.setText("Ce n'est pas un fichier excel valide");
                }

            }
        }
        if(e.getSource()==retour){
            MainWindow m =  new MainWindow();
            this.dispose();
        }
    }

    public void creationPlaceRules() {
        Etudiants etuList = getEtu();

        Connexion c = new Connexion("bd.sqlite");
        c.connect();
        Tables tabList = c.readTables();
        PlaceRules salle = new PlaceRules(etuList, tabList);
        salle.setRulesPlace(c.readTablesCote(tabList));
        c.close();
        

//         for(int i=0;i<tabList.getList().size();i++){
//            System.out.println(tabList.getList().get(i));
//        }
        Separer2EtuWindow wind = new Separer2EtuWindow(salle);
    }
}
