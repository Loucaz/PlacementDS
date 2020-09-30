package visual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;

// Lionel  Buathier (2017)
//inspired by  (C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)

//Controller.java
class Controller implements ActionListener {

	//Joe: Controller has Model and View hardwired in
	private Model model;
	private View view;
        private JList eList;
        private int table;

	public Controller(View view, Model model, JList eList, int table) {	
            this.view = view;
            this.model= model;
            this.eList = eList;
            this.table = table;
	}

	//invoked when a button is pressed
	@Override
        public void actionPerformed(ActionEvent e){
            if(eList.getSelectedIndex() > -1) {
                model.placeEtudiant(eList.getSelectedIndex(), table, view);
            }
	}



    
}
