/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImportExport;

import ImportExport.ExportWindow;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import placementds.PlaceRules;
import visual.Window;

/**
 *
 * @author Loucaz
 */
public class Separer2EtuWindow extends JFrame implements ActionListener {

    private final JButton valider;
    private final JButton supprimer;
    private final JButton retour;
    private final JComboBox list1;

    private final JComboBox list2;
    private final JComboBox listSeparer;
    private final JLabel etu, text1, text2, text3;

    private final PlaceRules salle;

    public Separer2EtuWindow(PlaceRules s) {
        this.salle = s;
        this.setTitle("Séparer 2 etudiants");
        this.setSize(720, 480);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        cont.fill = GridBagConstraints.BOTH;

        cont.insets = new Insets(10, 10, 0, 0);
        cont.gridx = 0;
        cont.gridy = 0;
        this.text1 = new JLabel("Séparer", JLabel.RIGHT);
        this.add(text1, cont);

        cont.gridx = 1;
        this.list1 = new JComboBox(salle.getEtuList().afficheList());
        this.add(list1, cont);
        this.list1.addActionListener(this);

        cont.gridx = 2;
        this.text2 = new JLabel("  de  ", JLabel.CENTER);
        this.add(text2, cont);

        cont.gridx = 3;
        this.list2 = new JComboBox(salle.getEtuList().afficheList());
        this.add(list2, cont);
        this.list2.addActionListener(this);

        cont.gridx = 4;
        this.valider = new JButton("Valider");
        this.add(valider, cont);
        this.valider.addActionListener(this);

        cont.gridy = 1;
        cont.gridx = 0;
        cont.gridwidth = 2;
        this.etu = new JLabel(list1.getSelectedItem().toString(), JLabel.RIGHT);
        this.add(etu, cont);

        cont.gridx = 2;
        cont.gridwidth = 1;
        this.text3 = new JLabel("est séparé de:", JLabel.RIGHT);
        this.add(text3, cont);

        cont.gridx = 3;
        this.listSeparer = new JComboBox();
        this.add(listSeparer, cont);
        this.listSeparer.addActionListener(this);

        cont.gridx = 4;
        this.supprimer = new JButton("Supprimer");
        this.add(supprimer, cont);
        this.supprimer.addActionListener(this);

        cont.gridy = 2;
        cont.gridx = 1;
        cont.gridwidth = 3;
        this.retour = new JButton("Continuer");
        this.add(retour, cont);
        this.retour.addActionListener(this);

        initComboboxEtu();

        //this.pack();
        this.setVisible(true);
    }

    public void initComboboxEtu() {
        this.listSeparer.removeAllItems();
        for (int x = 0; x < salle.getRulesList()[list1.getSelectedIndex()].size(); x++) {

            this.listSeparer.addItem(salle.getEtuList().getById(salle.getRulesList()[list1.getSelectedIndex()].get(x)));
        }

    }

    public void updateWindo() {
        this.etu.setText(list1.getSelectedItem().toString());
        initComboboxEtu();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == valider) {
            if (list1.getSelectedIndex() != list2.getSelectedIndex()) {
                if (salle.findIndexID(list1.getSelectedIndex(), list2.getSelectedIndex()) == -1) {
                    int i = list1.getSelectedIndex();
                    int j = list2.getSelectedIndex();
                    salle.addEtudiantRuleS(i, j);
                    //salle.checkEtudiants();

                    updateWindo();
                }
            }
        }

        if (e.getSource() == supprimer) {
            if (listSeparer.getItemCount() != 0) {
                int i = list1.getSelectedIndex();
                int j = salle.getRulesList()[list1.getSelectedIndex()].get(this.listSeparer.getSelectedIndex());
                //System.out.println(i + " " + j);
                salle.removeEtudiantRuleS(i, j);
                //salle.checkEtudiants();

                updateWindo();
            }
        }
        if (e.getSource() == retour) {
            Window win = new Window(salle);
            
            this.dispose(); //ferme fenetre import
        }

        if (e.getSource() == list1) {
            updateWindo();
        }
    }

}
