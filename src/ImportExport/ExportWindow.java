package ImportExport;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import placementds.Connexion;
import placementds.PlaceRules;
import visual.Window;

public class ExportWindow extends JFrame implements ActionListener {

    private final JButton button;
    private final JButton retour;
    private final PlaceRules salle;

    public ExportWindow(PlaceRules s) {
        this.salle = s;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Export");
        this.setSize(480, 320);
        this.setLocationRelativeTo(null);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        Dimension preferredSize = new Dimension(300, 50);

        gc.gridwidth = 3;
        gc.gridx = 0;
        gc.gridy = 0;
        button = new JButton("Exporter");
        button.setPreferredSize(preferredSize);
        button.addActionListener(this);
        this.add(button, gc);

        gc.gridwidth = 1;
        gc.gridy = 3;
        retour = new JButton("Annuler");
        retour.setPreferredSize(preferredSize);
        retour.addActionListener(this);
        this.add(retour, gc);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            JFileChooser choix = new JFileChooser();
            choix.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (choix.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String s = choix.getSelectedFile().getAbsolutePath();
                ExcelWriter reader = new ExcelWriter();
                reader.ecrire(salle, s);

                Connexion c = new Connexion("bd.sqlite");
                c.connect();
                c.insertTables(salle.getTabList(), salle.getRulesPlace());
                c.close();

                JOptionPane.showMessageDialog(null, "Export réussi");
                this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
            }

        }

        if (e.getSource() == retour) {
            Window win = new Window(salle);
            
            this.dispose();
        }
    }
}
