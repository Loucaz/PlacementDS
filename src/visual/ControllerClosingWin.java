package visual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Lionel  Buathier (2017)
//inspired by  (C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)

//Controller.java
class ControllerClosingWin implements ActionListener {

	//Joe: Controller has Model and View hardwired in
	private View view;

	public ControllerClosingWin(View view) {	
            this.view = view;
	}

	//invoked when a button is pressed
	@Override
        public void actionPerformed(ActionEvent e){
            view.initFrame();
	}



    
}
