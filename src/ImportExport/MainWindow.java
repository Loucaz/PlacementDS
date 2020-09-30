package ImportExport;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import placementds.PlaceRules;

public class MainWindow extends JFrame implements ActionListener {

    private final JButton nouveau;
    private final JButton quitter;

    public MainWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Export");
        this.setSize(480, 320);
        this.setLocationRelativeTo(null);

        nouveau = new JButton("Nouveau placement");
        Dimension preferredSize = new Dimension(300, 50);
        nouveau.setPreferredSize(preferredSize);
        preferredSize = new Dimension(300, 50);
        quitter = new JButton("Quitter");
        quitter.setPreferredSize(preferredSize);
        quitter.addActionListener(this);
        nouveau.addActionListener(this);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.gridwidth = 3;
        gc.gridx = 0;
        gc.gridy = 0;
        this.add(nouveau, gc);

        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.gridy = 3;
        this.add(quitter, gc);
        gc.gridx = 1;
        gc.gridwidth = 2;

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nouveau) {
            ImportWindow i = new ImportWindow();
            this.dispose();
        }

        if (e.getSource() == quitter) {
            
            this.dispose();
        }
    }
}
