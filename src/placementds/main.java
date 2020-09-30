/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placementds;

import ImportExport.MainWindow;
import java.util.ArrayList;
import visual.Window;
/**
 * 
 * @author p1701416
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainWindow m = new MainWindow();
//        
//        
//        Etudiants etuList = new Etudiants();
//        Tables tabList = new Tables();
//        
//        for(int i = 1; i <= 20; ++i) {
//            etuList.add(1700000+i, "Etudiant-"+i, "Alkjdfgoo", (int) (Math.random()*3)+1);
//        }
//        
//        // creation des 25 tables (x/y/id)
//        for(int i = 1; i <= 6; ++i)
//            tabList.add(i*100-100+i, 10, i);
//        for(int i = 8; i <= 14; ++i)
//            tabList.add((i-8)*100+i, 150, i);
//        for(int i = 15; i <= 21; ++i)
//            tabList.add((i-15)*100+i, 300, i);
//        
//        tabList.add(100, 400, 22);
//        tabList.add(200, 400, 23);
//        tabList.add(300, 400, 24);
//        tabList.add(400, 400, 25);
//        
//        // creation d'une salle avec les etudiants et les tables
//       // PlaceRules salle = new PlaceRules(etuList, tabList);
//        
//        
//        
//        
//        Connexion c = new Connexion("bd.sqlite");
//       c.connect();
//        Tables tabListC = c.readTables();
//        PlaceRules salle = new PlaceRules(etuList, tabListC);
//        salle.setRulesPlace(c.readTablesCote(tabListC));
//        c.close();
//        
//        
//        // regles de placement pour les etudiants. etu1 peut pas se positioner avec etu2
//        salle.addEtudiantRule(1700006, 1700009);
//        salle.addEtudiantRule(1700004, 1700009);
//        salle.addEtudiantRule(1700004, 1700010);
//        salle.addEtudiantRule(1700006, 1700003);
//        salle.addEtudiantRule(1700006, 1700007);
//        salle.addEtudiantRule(1700006, 1700004);
//        
////        // le lien entre tables
////        for(int i = 1; i < 6; ++i)
////            salle.addTableLink(i, i+1);
////        for(int i = 8; i < 14; ++i)
////            salle.addTableLink(i, i+1);
////        for(int i = 15; i < 21; ++i)
////            salle.addTableLink(i, i+1);
////        for(int i = 22; i < 25; ++i)
////            salle.addTableLink(i, i+1);
////        
////        for(int i = 1; i < 7; ++i) {
////            salle.addTableLink(i, i+7);
////            salle.addTableLink(i+7, i+14);
////        }
////        for(int i = 16; i <= 19; ++i)
////            salle.addTableLink(i, i+6);
//        
//        /*salle.putEtuOnTable(2, 1700006);
//        salle.putEtuOnTable(9, 1700009);
//        salle.putEtuOnTable(3, 1700015);
//        salle.removeEtuFromTable(3);*/
//        //salle.randomise(true);
//        
//        
//        
//        salle.checkEtudiants();
//        salle.checkTables();
//        salle.test();
//        // Verification si il y a des eleves qui sont ensemble (n'est par groupe
//        salle.checkOut(false);
//        salle.checkTables();
//        
//        
////        Connexion c = new Connexion("bd.sqlite");
////        c.connect();
////        c.insertTables(tabList,salle.getRulesPlace());
////        c.close();
//        Window win = new Window(salle);
    }   
}
