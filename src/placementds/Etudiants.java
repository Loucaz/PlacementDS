/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placementds;

import java.util.ArrayList;

/**
 *
 * @author p1701416
 */
public class Etudiants {

    private ArrayList<Etudiant> etuList;
    
    public Etudiants() {
        etuList = new ArrayList();
    }
    
    public boolean add(int num, String name, String lname, int groupe)
    {
        if(this.findIndex(num) != -1) return false;
        etuList.add(new Etudiant(num, name, lname, groupe));
        return true;
    }
    
    public Etudiant getByNum(int num) {
        return etuList.get(this.findIndex(num));
    }
    
    public Etudiant getById(int id) {
        return etuList.get(id);
    }
    
    public int findIndex(int num) {
        for(int i = 0; i < etuList.size(); ++i)
            if(etuList.get(i).getNum() == num)
                return i;
        return -1;
    }

    public ArrayList<Etudiant> getList() {
        return etuList;
    }
    
    ////////// Julien
    public String[] afficheList() {
        String[] s = new String[etuList.size()];
        for (int x = 0; x < etuList.size(); x++) {
            s[x] = etuList.get(x).toString();
        }
        return s;
    }

    public void setEtuList(ArrayList<Etudiant> etuList) {
        this.etuList = etuList;
    }

    public void affiche() {
        for (int i = 0; i < etuList.size(); i++) {
            etuList.get(i).affiche();
        }
    }
    
}
