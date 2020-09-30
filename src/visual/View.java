package visual;

import java.awt.Button;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;	//for CloseListener()
import java.awt.event.WindowAdapter;	//for CloseListener()
import java.util.Observable;		//for update();
import java.util.Observer;
import javax.swing.JList;
import javax.swing.JScrollPane;
import placementds.Connexion;
import placementds.Etudiant;
import placementds.PlaceRules;

class View implements Observer {
    
    private Button [] button;
    private Model model;
    private Grille g;
    private PlaceRules salle;
    private Frame frame;
    private JList eList;
    private int idTable;

    public View(Model model, PlaceRules salle) {
        this.model = model;
        this.salle = salle;
        frame = new Frame("Placement DS | Cartographie");
        button = new Button[2];
        button[0] = new Button("Retourner");
        button[1] = new Button("Choisir");
        initFrame();
        //choseEtudiant(1);

    } //View()

    public void initFrame() {
        frame.removeAll();
        g = new Grille(3, 1280, 720, salle, this);
        
        Panel panel = new Panel();                
        panel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        cont.fill = GridBagConstraints.BOTH;
        cont.gridx = 0;
        cont.gridy = 0;
        panel.add(g,cont);
        
        frame.add("Center", panel);
        
        frame.addWindowListener(new CloseListener());
        //frame.setSize(300, 200);
        frame.setLocation(100, 50);
        frame.pack();
        frame.setVisible(true);
        addGrilleController(g);
    }
    
    public void choseEtudiant(int idTable) {
        this.idTable = idTable;
        frame.removeAll();
        frame.setSize(900, 720);
        frame.setTitle("Placement DS | Choix d'etudiant");
        Panel panel = new Panel();
        
        String [] str = new String[salle.getEtudiants().getList().size()];
        Etudiant e = null;
        for(int i = 0; i < str.length; ++i) {
            e = salle.getEtudiants().getById(i);
            str[i] = (salle.etuOnTable(i) ? "Sur table - " : "[!!] Sans table - ") + e.getNum() + " - G" + e.getGroupe() + " - "+ e.getName() + "  " + e.getLastName();
        }
        System.out.println(str);
        eList = new JList(str);
        eList.setFont(new Font("", Font.ROMAN_BASELINE, 20));
        button[0].setFont(new Font("", Font.ROMAN_BASELINE, 25));
        button[1].setFont(new Font("", Font.ROMAN_BASELINE, 25));
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        cont.weightx = 1;
        cont.weighty = 1;
        cont.gridwidth = 1;
        cont.gridheight = 300;
        
        cont.fill = GridBagConstraints.BOTH;
        cont.gridx = 0;
        cont.gridy = 0;       
        
        JScrollPane scrollPane = new JScrollPane(eList);
        scrollPane.setSize(200, 300);
        panel.add(scrollPane, cont);
        
        cont.gridheight = 1;
        cont.gridx = 1;
        panel.add(button[1],cont);
        
        cont.gridheight = 1;
        cont.gridy = 1;
        panel.add(button[0],cont);
        
        frame.add("Center", panel);

        frame.addWindowListener(new CloseListener());
        frame.setLocation(100, 50);
        
        frame.setVisible(true);
        addButtonController(button[0], 0);
        addButtonController(button[1], 1);
    }

    @Override
    public void update(Observable obs, Object obj) {
        //myTextField.setText("" + ((Integer) obj));
    }

    public void addButtonController(Button button, int id) {
        switch (id) {
            case 0:
                button.addActionListener(new ControllerClosingWin(View.this));
                break;
            case 1:
                button.addActionListener(new Controller(View.this, model, eList, idTable));
                break;
        }
    }
    
    public void addGrilleController(Grille g) {
        GrilleController controller = new GrilleController(View.this, model, g);
        g.addMouseListener(controller);
        g.addMouseMotionListener(controller);
        g.addMouseWheelListener(controller);
    }
    
    public static class CloseListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            e.getWindow().setVisible(false);
            System.exit(0);
                    
        }
    }

    public Frame getFrame() {
        return frame;
    }
    
    

}
