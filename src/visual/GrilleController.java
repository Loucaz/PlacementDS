/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visual;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author p1701416
 */
public class GrilleController implements MouseListener, MouseMotionListener, MouseWheelListener {
    Model model;
    View view;
    private Grille g;
    private int [] coord = new int[2];

    public GrilleController(View view, Model model, Grille g) {	
        this.view = view;
        this.g = g;
        this.model= model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        g.pressed(e.getX(), e.getY());
        switch (e.getButton()) {
            case 3:
                g.openMenu(e.getX(), e.getY());
                break;
            case 1:
                g.clicked(e.getX(), e.getY());
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("Pressed");
        coord[0] = e.getX();
        coord[1] = e.getY();
        if(e.getButton() == 3)
            g.linkFirst(e.getX(), e.getY());
        else
            g.pressed(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == 3) {
            g.linkLast();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println("Entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //System.out.println("Exited");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int [] moved = new int[2];
        int [] oldPoint = new int[2];
        oldPoint[0] = coord[0];
        oldPoint[1] = coord[1];
            
        moved[0] = e.getX() - coord[0];
        coord[0] = e.getX();
        moved[1] = e.getY() - coord[1];
        coord[1] = e.getY();
        switch (e.getModifiersEx()) {
            case 1024:
                g.moveSurface(moved[0], moved[1]);
                break;
            case 2048:
                g.moveTable(oldPoint, moved);
                break;
            case 4096:
                g.addLinkLastPoint(moved[0], moved[1]);
                break;
            case 5120:
                g.moveTable(oldPoint, moved);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        g.scroll(e.getWheelRotation());
    }
}
