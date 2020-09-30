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
public class Table {
    private int x, y, number;
    private boolean libre;
    private boolean active;

    public Table(int x, int y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
        libre = true;
        active = true;
        System.out.println("La table " + number + " etait cree");
    }

    public int getNumber() {
        return number;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public void changeLibreState() {
        libre = !libre;
    }
    
    public void changeCoord(int x, int y) {
        this.x += x;
        this.y += y;
    }
    
    public boolean getState() {
        return libre;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void changeActiveState() {
        active = !active;
    }
    /// Julien
    @Override
    public String toString() {
        return "Table{" + "x=" + x + ", y=" + y + ", number=" + number + ", libre=" + libre + ", active=" + active + '}';
    }
    
    public Table(int x, int y, int number, boolean libre, boolean active) {
        this.x = x;
        this.y = y;
        this.number = number;
        this.libre = libre;
        this.active = active;
    }
    
}
