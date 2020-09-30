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
public class Tables {
    

    private ArrayList<Table> tableList;
    
    public Tables() {
        tableList = new ArrayList();
    }
    
    public boolean add(int x, int y, int number)
    {
        if(this.findIndex(number) != -1) return false;
        tableList.add(new Table(x, y, number));
        return true;
    }
    
    public void remove(int num) {
        tableList.remove(findIndex(num));
    }
    
    public Table getByNum(int num) {
        return tableList.get(this.findIndex(num));
    }
    
    public Table getById(int id) {
        return tableList.get(id);
    }
    
    public Table getByCoord(int x, int y, int tailleObj) {
        for(int i = 0, d = tableList.size(); i < d; ++i) {
            if(tableList.get(i).getX() < x && tableList.get(i).getY() < y
                    && tableList.get(i).getX()+tailleObj > x && tableList.get(i).getY()+tailleObj > y) {
                return tableList.get(i);
            }
        }
        return null;
    }
    
    public Table getByCoord(int x, int y, int tailleObj, int draggedTableId) {
        for(int i = 0, d = tableList.size(); i < d; ++i) {
            if(tableList.get(i).getX() < x && tableList.get(i).getY() < y
                    && tableList.get(i).getX()+tailleObj > x && tableList.get(i).getY()+tailleObj > y
                    && (draggedTableId == -1 || draggedTableId == tableList.get(i).getNumber())) {
                return tableList.get(i);
            }
        }
        return null;
    }
    
    public int findIndex(int num) {
        for(int i = 0; i < tableList.size(); ++i)
            if(tableList.get(i).getNumber() == num) {
                return i;
            }
        return -1;
    }

    public ArrayList<Table> getList() {
        return tableList;
    }
}
