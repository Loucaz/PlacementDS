/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visual;

import ImportExport.ExportWindow;
import ImportExport.Separer2EtuWindow;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.lang.reflect.Method;
import placementds.Connexion;
import placementds.PlaceRules;
import placementds.Table;

/**
 *
 * @author p1701416
 */
public class OptionGrilleMenu {
    private final int [] coord = new int[2];
    private final int size, mode;
    private PlaceRules salle;
    private Table table;
    private View view;
    private String [] btnNames;
    private int [] menu = null;
    private int btnLarge = 5;
    
    public enum t {S}
    
    public OptionGrilleMenu(int mode, int x, int y, int size, Table table, PlaceRules salle, View view, int [] menu) {
        coord[0] = x;
        coord[1] = y;
        
        btnNames = new String[13];
        btnNames[0] = "Effacer les liens";
        btnNames[1] = "Effacer la table";
        btnNames[2] = "Retirer l'etudiant";
        btnNames[3] = "Placer l'etudiant";
        btnNames[4] = "Ajouter une table";
        btnNames[5] = "Placer reste";
        btnNames[6] = "Placer tous";
        btnNames[7] = "Placer tous par groupe";
        btnNames[8] = "Placer reste par groupe";
        btnNames[9] = "Retirer tous les etudiants";
        btnNames[10] = "Separer 2 etudiants";
        btnNames[11] = "Valider";
        btnNames[12] = "Sauvgarder les tables";
        
        this.mode = mode;
        this.size = size;
        this.salle = salle;
        this.table = table;
        this.view = view;
        this.menu = menu;
    }
    
    public void render(Graphics g) {
        if(menu != null) {
            g.setColor(Color.white);
            g.drawRect(coord[0], coord[1], size*btnLarge, size*menu.length);
            g.setXORMode(Color.GRAY);
            for(int i = 0; i < menu.length; ++i)
                g.fillRect(coord[0]+1, coord[1]+size*i+1, size*btnLarge-1, size-1);
            g.setPaintMode();
            g.setColor(Color.black);
            g.setFont(new Font("Italic", Font.BOLD, 18));
            
            for(int i = 0; i < menu.length; ++i) {
                if(menu[i] >= 0 && menu[i] < btnNames.length) {
                    g.drawString(btnNames[menu[i]], coord[0]+4, coord[1]+size*i+size/2+3);
                }
            }
        }
    }
    
    public Table getTable() {
        return table;
    }
    
    public void clicked(int x, int y, int [] surface) {
        System.out.print("Main -> ");
        int clickedOn = -1;
        for(int i = 0; i < menu.length; ++i) {
            if(x > coord[0] && x < (coord[0]+size*btnLarge) && y > (coord[1]+size*i) && y < (coord[1]+size+size*i)) {
                System.out.println(btnNames[menu[i]]);
                clickedOn = menu[i];
                i = menu.length;
            }
        }
        Thread bgTask = null;
        
        switch (clickedOn) {
            // Effacer les liens
            case 0:
                salle.removeLink(table.getNumber());
                break;
                
            // Effacer la table
            case 1:
                salle.removeTable(table.getNumber());
                break;
                
            // Retirer l'etudiant
            case 2:
                salle.removeEtuFromTable(table.getNumber());
                break;
                
            // Placer l'etudiant
            case 3:
                view.choseEtudiant(salle.getTables().findIndex(table.getNumber()));
                break;
                
            // Ajouter une table
            case 4:
                int num = salle.getTables().getList().size()+1;
                for(int i = 1, size = salle.getTables().getList().size(); i < size; ++i) {
                    if(salle.getTables().findIndex(i) == -1) {
                        num = i;
                        i = size;
                    }
                }
                salle.getTables().add(coord[0] - surface[0] - size/2, coord[1] - surface[1] - size/2, num);
                salle.resize();
                break;
                
            // Random reste
            case 5:
                bgTask = new Thread(() -> 
                    {
                        salle.randomise(false, false);
                    }
                );
                bgTask.start();
                break;
                
            // Random tout
            case 6:
                bgTask = new Thread(() -> 
                    {
                        salle.randomise(true, false);
                    }
                );
                bgTask.start();
                break;
                
            // Random tout par groupe
            case 7:
                bgTask = new Thread(() -> 
                    {
                        salle.randomise(true, true);
                    }
                );
                bgTask.start();
                break;
                
            // Random reste par groupe
            case 8:
                bgTask = new Thread(() -> 
                    {
                        salle.randomise(false, true);
                    }
                );
                bgTask.start();
                break;
            
             // Retirer tous les etudiants
            case 9:
                bgTask = new Thread(() -> 
                    {
                        salle.removeAllEtuFromTable();
                    }
                );
                bgTask.start();
                break;
            
            // Separer 2 etudiants
            case 10:
                Separer2EtuWindow wind = new Separer2EtuWindow(salle);
                view.getFrame().dispose();
                break;
            
            // valider
            case 11:
                ExportWindow export = new ExportWindow(salle);
                view.getFrame().dispose();
                break;
                
            case 12:
                bgTask = new Thread(() -> 
                    {
                        Connexion c = new Connexion("bd.sqlite");
                        c.connect();
                        c.insertTables(salle.getTabList(), salle.getRulesPlace());
                        c.close();
                    }
                );
                bgTask.start();
                break;

            // Aucun choix
            default:
                System.out.println("NaN");
                break;
        }
    }
}
