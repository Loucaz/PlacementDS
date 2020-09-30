/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placementds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import placementds.Table;
import placementds.Tables;

/**
 *
 * @author p1701678
 */
public class Connexion {

    String DBPath;
    ResultSet rs;
    Connection connection = null;
    Statement statement = null;

    public Connexion(String dBPath) {
        DBPath = dBPath;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
            statement = connection.createStatement();
            String sql = "PRAGMA synchronous=OFF";
            statement.execute(sql);
            System.out.println("Connexion a " + DBPath + " avec succès");
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erreur de connexion");
        }
    }

    public void insertTables(Tables t, ArrayList<Integer>[] tabCote) {
        deleteTables();
        for (int i = 0; i < t.getList().size(); i++) {
            insertTable(t.getList().get(i).getX(), t.getList().get(i).getY(), t.getList().get(i).getNumber(),0, t.getList().get(i).isActive());
            for (int j = 0; j < tabCote[i].size(); j++) {
                insertCote(t.getList().get(i).getNumber(), tabCote[i].get(j));
            }

        }
    }

    public void insertTable(int x, int y, int num, int groupe, boolean active) {
        try {
            int a = (active) ? 1 : 0;

            String requete = "INSERT INTO 'Table' VALUES ('" + x + "', '" + y + "', '" + num + "', '" + groupe + "', '" + a + "');";
            statement.executeUpdate(requete);
        } catch (SQLException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Echec insertion dans table");

        }
    }

    public void insertCote(int table, int tableCote) {
        try {
            String requete = "INSERT INTO 'Cote' VALUES ('" + table + "', '" + tableCote + "');";
            statement.executeUpdate(requete);
        } catch (SQLException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Echec insertion dans table");

        }
    }

    public Tables readTables() {
        Tables t = new Tables();
        try {
            rs = statement.executeQuery("SELECT * FROM 'Table'");
            while (rs.next()) {
                int i = rs.getInt("active");
                boolean act = (i != 0);
                t.getList().add(new Table(rs.getInt("x"), rs.getInt("y"), rs.getInt("number"), true, act));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erreur bd");
        }
        return t;
    }

    public ArrayList<Integer>[] readTablesCote(Tables tabList) {
        ArrayList<Integer>[] rulesPlace = new ArrayList[tabList.getList().size()];
        for (int i = 0; i < tabList.getList().size(); i++) {
            rulesPlace[i] = new ArrayList();
        }
        System.out.println(tabList.getList().size());
        try {
            rs = statement.executeQuery("SELECT * FROM 'Cote'");
            while (rs.next()) {
                System.out.println(rs.getInt("tableCote"));
                int id =rs.getInt("table");
                rulesPlace[tabList.findIndex(id)].add(rs.getInt("tableCote"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erreur bd");
        }
        return rulesPlace;
    }

    public void deleteTables() {
        try {
            statement.executeUpdate("DELETE FROM 'Table'");
            statement.executeUpdate("DELETE FROM 'Cote'");
        } catch (SQLException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erreur bd");
        }
    }

    public void close() {
        try {
            connection.close();
            System.out.println("Déconnexion réussie");
        } catch (SQLException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erreur bd");
        }
    }

}
