/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visual;

import placementds.PlaceRules;

/**
 *
 * @author p1701416
 */
public class Window {
    
    public Window(PlaceRules salle) {
        Model myModel = new Model(salle);
        View myView = new View(myModel, salle);
        myModel.addObserver(myView);
        myModel.setValue(100);
    }
    
}
