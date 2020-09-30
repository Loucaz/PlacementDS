/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placementds;

/**
 *
 * @author p1701416
 */
public class Etudiant {
    final private String  name,
                          lname;
    final private int num,
                      groupe;

    public Etudiant(int num, String name, String lname, int groupe) {
        this.name = name;
        this.lname = lname;
        this.num = num;
        this.groupe = groupe;
    }
    
    public int getNum() {
        return num;
    }

    public int getGroupe() {
        return groupe;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lname;
    }
    
    ////////// Julien
    @Override
    public String toString() {
        return name + " " + lname + " " + groupe;
    }

    public void affiche() {
        System.out.println(lname + "  " + name + "  " + "  G" + groupe + "  " + num);
    }
    
}
