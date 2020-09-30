package visual;

import java.util.Observable;
import placementds.PlaceRules;

// Lionel  Buathier (2017)
//inspired by  (C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)
//Model.java
//Model holds an int counter (that's all it is).
//Model is an Observable
//Model doesn't know about View or Controller
public class Model extends Observable {

    private int counter;	//primitive, automatically initialised to 0
    private PlaceRules salle;

    public Model(PlaceRules salle) {
        this.salle = salle;
        System.out.println("Model()");

    } //Model()

    //uncomment this if View is using Model Pull to get the counter
    //not needed if getting counter from notifyObservers()
    //public int getValue(){return counter;}
    //notifyObservers()
    //model sends notification to view because of RunMVC: myModel.addObserver(myView)
    //myView then runs update()
    //
    //model Push - send counter as part of the message
    public void setValue(int value) {
        this.counter = value;
        System.out.println("Model init: counter = " + counter);
        setChanged();
        //model Push - send counter as part of the message
        notifyObservers(counter);
        //if using Model Pull, then can use notifyObservers()
        //notifyObservers()

    }
    
    public void placeEtudiant(int idEtudiant, int idTable, View view) {
        System.out.println("On place l'etudiant "+salle.getEtudiants().getById(idEtudiant).getNum()+" sur la table "+salle.getTables().getById(idTable).getNumber());
        salle.forcedPutEtuOnTable(salle.getTables().getById(idTable).getNumber(), salle.getEtudiants().getById(idEtudiant).getNum());
        view.initFrame();
        setChanged();
        //model Push - send counter as part of the message
        notifyObservers(counter);
        //if using Model Pull, then can use notifyObservers()
        //notifyObservers()
        
    }

}
