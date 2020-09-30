package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import javax.swing.JComponent;
import placementds.Etudiant;
import placementds.Table;
import placementds.PlaceRules;
import placementds.Tables;

/*
 * @author Alexey
 */
public class Grille extends JComponent {
    private int mode;
    private final int tailleObj = 50;
    private PlaceRules salle;
    private final int [] size = new int[2];
    private int [] surface = new int[2];    
    private VolatileImage vImg;
    private Graphics2D g2D;
    private OptionGrilleMenu menu = null;
    private Integer [] linkLastPoint = null;
    private Table linkTable = null;
    private View view = null;
    private int changeEtudiants;
    private int draggedTableId = -1;
    
    private Thread renderThread = null;
    
    private enum Mode {
        Etudiant(3), LIENS(2), BLOQUES(1);
        private final int value;
        private Mode(int v) {
            value = v;
        }
    }

    public Grille(int mode, int size_w, int size_h, PlaceRules salle, View view) {
        surface[0] = 200;
        surface[1] = 90;
        size[0] = size_w;
        size[1] = size_h;
        this.salle = salle;
        this.mode = mode;
        this.view = view;
        changeEtudiants = -1;
        startAutoRender((int)(1000/120));
    }
    
    private void startAutoRender(int oncePerXms) {
        renderThread = new Thread(() -> {
            while(true) {
                try {
                this.update(getGraphics());
                } catch (Exception ex) {
                    //
                }
                try {
                    Thread.sleep(oncePerXms);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        renderThread.start();
    }
    
    // rendering to the image
    void renderOffscreen() {
        do {
            if (vImg.validate(getGraphicsConfiguration()) == VolatileImage.IMAGE_INCOMPATIBLE)
                vImg = createVolatileImage(size[0], size[1]);
            g2D = vImg.createGraphics();
            this.setIgnoreRepaint(true);
            CleanBG(g2D, Color.black);
            
            g2D.setColor(Color.white);
            g2D.setFont(new Font("", Font.BOLD, 30)); 
            switch (mode) {
                case 3:
                    g2D.drawString("Le placement des etudiants", 200, tailleObj);

                    // le texte en carret gauche-haut
                    g2D.setFont(new Font("", Font.BOLD, 23));
                    g2D.setColor(Color.green);
                    g2D.drawString("Groupe 1", 10, tailleObj);
                    g2D.setColor(Color.cyan);
                    g2D.drawString("Groupe 2", 10, tailleObj*2);
                    g2D.setColor(Color.orange);
                    g2D.drawString("Groupe 3", 10, tailleObj*3);
                    break;
                case 2:
                    g2D.drawString("Les liasons entre les tables", 200, tailleObj);
                    break;
                case 1:
                    g2D.drawString("Tables bloquées", 200, tailleObj);
                    // le texte en carret gauche-haut
                    g2D.setFont(new Font("", Font.BOLD, 23));
                    g2D.setColor(Color.red);
                    g2D.drawString("Bloquée", 10, tailleObj);
                    g2D.setColor(Color.lightGray);
                    g2D.drawString("Debloquée", 10, tailleObj*2);
                    break;
                default:
                    break;
            }
            
            // 1 - Tables bloquees
            // 2 - Les liasons entre les tables
            // 3 - Le placement des etudiants
            //
            
            
            
            g2D.setColor(Color.white);
            for(int i = 0, d = salle.getTables().getList().size(), x, y; i < d; ++i) {
                x = surface[0]+salle.getTables().getById(i).getX();
                y = surface[1]+salle.getTables().getById(i).getY();
                if(mode == 1) {
                    if(salle.getTables().getById(i).isActive())
                        g2D.setColor(Color.lightGray);
                    else
                        g2D.setColor(Color.red);
                } else if(mode == 3) {
                    if(!salle.getTables().getById(i).getState()) {
                        switch (salle.getEtudiantTable(i).getGroupe()) {
                            case 1:
                                g2D.setColor(Color.green);
                                break;
                            case 2:
                                g2D.setColor(Color.cyan);
                                break;
                            case 3:
                                g2D.setColor(Color.orange);
                                break;
                            default:
                                g2D.setColor(Color.black);
                                break;
                        }
                        addName(x, y, g2D, i);
                    } else 
                        g2D.setColor(Color.white);
                }
                
                if(!salle.getTables().getById(i).isActive() && mode == 3)
                    g2D.setColor(Color.red);
                addRect(x, y, g2D);
                addNumber(x, y, salle.getTables().getById(i).getNumber(), g2D);
            }
            
            if(changeEtudiants != -1) {
                Table t = salle.getTables().getById(changeEtudiants);
                g2D.setColor(Color.red);
                g2D.drawRoundRect(surface[0]+t.getX()-5, surface[1]+t.getY()-5, tailleObj+10, tailleObj+10, 15, 15);
            }
            if(mode == 2)
                addAlias();
            if(menu != null) {
                menu.render(g2D);
            }
            if(linkTable != null) {
                g2D.setColor(Color.red);
                g2D.drawLine(linkLastPoint[0], linkLastPoint[1], linkTable.getX() + tailleObj/2 + surface[0], linkTable.getY() + tailleObj/2 + surface[1]);
            }
            g2D.dispose();
        } while (vImg.contentsLost());
    }
    
    @Override
    public void paintComponent(Graphics g) {
        // copying from the image (here, gScreen is the Graphics
        // object for the onscreen window)
        do {
            vImg = createVolatileImage(size[0], size[1]);
            renderOffscreen();
            g.drawImage(vImg, 0, 0, this);
        } while (vImg.contentsLost());
    }
    
    public void moveSurface(int x, int y) {
        surface[0] += x;
        surface[1] += y;
        //update(getGraphics());
    }
    
    public void moveTable(int [] oldPoint, int [] newPoint) {
        if(linkTable != null) linkTable = null;
        Table tab  = salle.getTables().getByCoord((oldPoint[0] - surface[0]), (oldPoint[1] - surface[1]), tailleObj, draggedTableId);
        if(tab != null) {
            if(draggedTableId == -1) draggedTableId = tab.getNumber();
            
            tab.changeCoord(newPoint[0], newPoint[1]);
            
            //update(getGraphics());
        } else if(draggedTableId != -1) draggedTableId = -1;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size[0], size[1]);
    }
    
    public void CleanBG(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(0, 0, size[0], size[1]);
    }
    
    public void addCercle(int x, int y, Graphics g) {
        g.drawOval(x, y, tailleObj, tailleObj);
    }
    
    public void addName(int x, int y, Graphics g, int id_table) {
        g.setPaintMode();
        Etudiant e = salle.getEtudiantTable(id_table);
        g.setFont(new Font("", Font.BOLD, 14));
        
        String [] msg = new String[2];
        msg[0] = e.getLastName();
        msg[1] = e.getName();
        for(int v = 0, score; v < 2; ++v) {
            score = 0;
            for(int i = 0; i < msg[v].length(); ++i)
                if(msg[v].charAt(i) > 'A' && msg[v].charAt(i) < 'Z' || msg[v].charAt(i) > '1' && msg[v].charAt(i) <= '9' || msg[v].charAt(i) == '0')
                    score += 3;
                else score += 2;
            g.drawString(msg[v], x+tailleObj/3-score-5, y+tailleObj+tailleObj/3+v*20);
        }
    }
    
    public void addRect(int x, int y, Graphics g) {
        g.drawRoundRect(x, y, tailleObj, tailleObj, 15, 15);
    }
    
    public void addAlias() {
        ArrayList<Integer> [] rules = salle.getRulesPlace();
        Tables tables = salle.getTables();
        g2D.setColor(Color.red);
        for(int i = 0, d = rules.length, x1, y1, x2, y2; i < d; ++i) {
            x1 = tables.getById(i).getX() + surface[0] + tailleObj/2;
            y1 = tables.getById(i).getY() + surface[1] + tailleObj/2;
            for(int j = 0, l = rules[i].size(); j < l; ++j) {
                x2 = tables.getById(rules[i].get(j)).getX() + surface[0] + tailleObj/2;
                y2 = tables.getById(rules[i].get(j)).getY() + surface[1] + tailleObj/2;
                g2D.drawLine(x1, y1, x2, y2);
            }
        }
    }
    
    public void addNumber(int x, int y, int number, Graphics g) {
        g.setFont(new Font("italic", 1, 25));
        g.drawString(""+number, x+tailleObj/2-10, y+tailleObj/2+10);
    }
    
    public void openMenu(int x, int y) {
        Table tab = salle.getTables().getByCoord((x - surface[0]), (y - surface[1]), tailleObj);
        int [] mOptions;
        /*            
            0 = Effacer les liens
            1 = Effacer la table
            2 = Retirer l'etudiant
            3 = Placer l'etudiant
            4 = Ajouter une table
            5 = Placer reste
            6 = Placer tous
            7 = Placer tous par groupe
            8 = Placer reste par groupe
            9 = Retirer tous les etudiants
            10 = Separer 2 etudiants
            11 = Valider
            12 = Sauvgarder les tables
        */
        if(tab == null) {
            // Si le click droit n'est pas sur une table
            if(mode == Mode.Etudiant.value) {
                mOptions = new int [7];
                mOptions[0] = 9;
                mOptions[1] = 5;
                mOptions[2] = 6;
                mOptions[3] = 7;
                mOptions[4] = 8;
                mOptions[5] = 10;
                mOptions[6] = 11;
            } else {
                mOptions = new int [2];
                mOptions[0] = 4;
                mOptions[1] = 12;
            }
        } else if(mode == Mode.Etudiant.value) {
            // Si click droit est sur la table
            mOptions = new int [1];
            if(!tab.getState())
                mOptions[0] = 2;
            else
                mOptions[0] = 3;
        } else if(mode == Mode.LIENS.value) {
            mOptions = new int [3];
            mOptions[0] = 1;
            mOptions[1] = 0;
            mOptions[2] = 12;
        } else if(mode == Mode.BLOQUES.value) {
            mOptions = new int [2];
            mOptions[0] = 1;
            mOptions[1] = 12;
        } else {
            mOptions = new int[1];
            mOptions[0] = -1;
        }
        menu = new OptionGrilleMenu(mode, x, y, tailleObj, tab, salle, view, mOptions);
        //update(getGraphics());
    }
    
    public void pressed(int x, int y) {
        if(menu != null) {
            menu.clicked(x, y, surface);
        }
        menu = null;
        try {
            //update(getGraphics());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void clicked(int x, int y) {
        Table tab  = salle.getTables().getByCoord((x - surface[0]), (y - surface[1]), tailleObj);
        System.out.println(menu);
        if(tab != null) {
            if(mode == Mode.BLOQUES.value && menu == null) {
                System.out.println(menu);
                tab.changeActiveState();
            } else if(mode == Mode.Etudiant.value && changeEtudiants == -1) {
                changeEtudiants = salle.getTables().findIndex(tab.getNumber());
            } else if(mode == Mode.Etudiant.value && changeEtudiants > -1) {
                System.out.println(changeEtudiants+"|"+salle.getTables().findIndex(tab.getNumber()));
                salle.echangeEtudiants(changeEtudiants, salle.getTables().findIndex(tab.getNumber()));
                changeEtudiants = -1;
            } 
        } else if(mode == 3) changeEtudiants = -1;
        //update(getGraphics());
    }
    
    public void scroll(int val) {
        menu = null;
        mode += val;
        if(mode > 3) mode = 1;
        else if(mode < 1) mode = 3;
        //update(getGraphics());
    }
    
    public void linkLast() {
        if(mode == 2) {
            if(linkLastPoint != null && linkTable != null) {
                Table tab  = salle.getTables().getByCoord((linkLastPoint[0] - surface[0]), (linkLastPoint[1] - surface[1]), tailleObj);
                if(tab != null && linkTable != tab) {
                    salle.addTableLink(linkTable.getNumber(), tab.getNumber());
                }
            }
        }
        linkTable = null;
        linkLastPoint = null;
        //update(getGraphics());
    }
    
    public void linkFirst(int x, int y) {
        menu = null;
        if(mode == 2) {
            linkTable = salle.getTables().getByCoord((x - surface[0]), (y - surface[1]), tailleObj);
            if(linkTable != null) {
                linkLastPoint = new Integer[2];
                linkLastPoint[0] = new Integer(x);
                linkLastPoint[1] = new Integer(y);
            }
        }
    }
    
    public void addLinkLastPoint(int x, int y) {
        if(linkTable != null) {
            linkLastPoint[0] += x;
            linkLastPoint[1] += y;
        }
        //update(getGraphics());
    }

    public int updateGrille(int x, int y) {
        
        // ICI la verification de la grille
        //System.out.println("X: "+x+" | Y: "+y);
        
        //update(getGraphics());
        System.out.println("updated");
        return -1;
    }
}
