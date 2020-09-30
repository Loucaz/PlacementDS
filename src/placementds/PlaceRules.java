/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package placementds;

import java.util.ArrayList;

/**
 *
 * @author Alexey
 */
public class PlaceRules {
    // l'ID d'etudiant dans une table [T]
    private int etuTable[];
    
    // Liste des etudiants
    private Etudiants etuList;
    
    // Liste des tables
    private Tables tabList;
    
    // [][LISTE] des etudiants avec qui l'etudiant [N][] n'est peut pas se placer
    private ArrayList<Integer> [] rulesList;
    
    // la position de [N][] table qui se place au couté de [][AUTRES TABLES]
    private ArrayList<Integer> [] rulesPlace;
    
    // on cree le class avec le tableau des position randomes des etudiants
    public PlaceRules(Etudiants etuList, Tables tabList) {
        this.etuList = etuList;
        rulesList = new ArrayList[etuList.getList().size()];
        for(int i = 0; i < etuList.getList().size(); ++i) {
            rulesList[i] = new ArrayList();
        }
        
        this.tabList = tabList;
        rulesPlace = new ArrayList[tabList.getList().size()];
        for(int i = 0; i < tabList.getList().size(); ++i) {
            rulesPlace[i] = new ArrayList();
        }
        this.etuTable = new int[tabList.getList().size()];
        for(int i = 0; i < etuTable.length; ++i)
            etuTable[i] = -1;
    }
    
    // on cherche l'id d'etudiant 'target' dans le tableau
    public int findEtudiant(int target) {
        // returne -1 si il y n'a pas
        return etuList.findIndex(target);
    }
    
    // on cherche l'id de la table 'target' dans le tableau
    public int findTable(int target) {
        // returne -1 si il y n'a pas
        return tabList.findIndex(target);
    }
    
    // on rajoute l'etudiant 'target' dans la liste avec qui 'etuNumber' ne peut pas etre et enverse
    public void addEtudiantRule(int etuNumber, int target) {
        rulesList[etuList.findIndex(etuNumber)].add(etuList.findIndex(target));
        rulesList[etuList.findIndex(target)].add(etuList.findIndex(etuNumber));
    }
    
    // on rajoute la table 'target' dans la liste des liens de la table 'numTable' et enverse
    public void addTableLink(int numTable, int target) {
        if(tabList.getList().size() > rulesPlace.length)
            resize();
        boolean has = false;
        for(int i = 0, size = rulesPlace[tabList.findIndex(numTable)].size(); i < size; ++i)
            if(rulesPlace[tabList.findIndex(numTable)].get(i) == tabList.findIndex(target)) has = true;
        if(!has) {
            System.out.println("Link added");
            rulesPlace[tabList.findIndex(numTable)].add(tabList.findIndex(target));
            rulesPlace[tabList.findIndex(target)].add(tabList.findIndex(numTable));
            
        }
    }
    
    // on efface les liens entres les tables
    public void removeLink(int numTable) {
        numTable = tabList.findIndex(numTable);
        if(numTable < rulesPlace.length) {
            while(0 < rulesPlace[numTable].size()) {
                // supression lui meme des voisins
                for(int t = 0; t < rulesPlace[rulesPlace[numTable].get(0)].size(); ++t) {
                    if(rulesPlace[rulesPlace[numTable].get(0)].get(t) == numTable) {
                        rulesPlace[rulesPlace[numTable].get(0)].remove(t);
                    }
                }
                // supression les voisins
                rulesPlace[numTable].remove(0);
            }
        
        }
    }
    
    // on efface les liens et la table par son numero
    public void removeTable(int numTable) {
        removeLink(numTable);
        int idTarget = tabList.findIndex(numTable);
        if(rulesPlace.length > idTarget) {
            for(int i = 0, val, size = rulesPlace.length; i < size; ++i) {
                for(int j = 0; j < rulesPlace[i].size(); ++j) {
                    val = rulesPlace[i].get(j);
                    if(val > idTarget)
                        rulesPlace[i].set(j, --val);
                }
            }
            rulesPlace[tabList.findIndex(numTable)] = null;
        }
        if(etuTable[tabList.findIndex(numTable)] != -1) removeEtuFromTable(numTable);
        etuTable[tabList.findIndex(numTable)] = -10;
        tabList.remove(numTable);
        resize();
    }
    
    // on redemensionne les tables
    public void resize() {
        ArrayList<Integer> [] tmpRulesPlace = new ArrayList[tabList.getList().size()];
        System.out.println("New size > "+tabList.getList().size());
        System.out.println("Old size > "+rulesPlace.length);
        for(int i = 0; i < tmpRulesPlace.length; ++i) {
            tmpRulesPlace[i] = new ArrayList();
        }
        for(int i = 0, nullTimes = 0; i < rulesPlace.length; ++i) {
            System.out.println("rule "+i+" > "+rulesPlace[i]);
            if(rulesPlace[i] == null) {
                ++nullTimes;
            }
            else {
                tmpRulesPlace[i-nullTimes] = rulesPlace[i];
                System.out.println("rule "+i+" > "+tmpRulesPlace[i-nullTimes]);
            }
        }
        rulesPlace = tmpRulesPlace;
        System.out.println("size tables "+tabList.getList().size());
        System.out.println("Size tmpRulesPlace "+tmpRulesPlace.length);
        for(int i = 0; i <  rulesPlace.length; ++i)
            System.out.println(""+rulesPlace[i]);
        
        int [] tmpEtuTable = new int[tabList.getList().size()];
        for(int i = 0; i < tmpEtuTable.length; ++i)
            tmpEtuTable[i] = -1;
        for(int i = 0, nullTimes = 0; i < etuTable.length; ++i) {
            if(etuTable[i] == -10) ++nullTimes;
            else tmpEtuTable[i-nullTimes] = etuTable[i];
        }
        etuTable = tmpEtuTable;
        System.out.println("Was resized");
    }
    
    public void echangeEtudiants(int idTable_1, int idTable_2) {
        if(etuTable[idTable_1] == -1 && etuTable[idTable_2] > -1
        || etuTable[idTable_2] == -1 && etuTable[idTable_1] > -1) {
            tabList.getById(idTable_1).changeLibreState();
            tabList.getById(idTable_2).changeLibreState();
        }
        int tmp = etuTable[idTable_1];
        etuTable[idTable_1] = etuTable[idTable_2];
        etuTable[idTable_2] = tmp;
    }
    
    // on regarde si l'etudiant a deja une table (ID d'etudiant et pas son numero!)
    public boolean etuOnTable(int id) {
        for(int i = 0; i < etuTable.length; ++i)
            if(etuTable[i] == id) return true;
        return false;
    }
    
    // position randome des etudiants sur les tables.
    public boolean randomise(boolean clean, boolean per_group) {
        if(rulesPlace != null) {
            // clean les tables
            if(clean)
                for(int i = 0; i < rulesPlace.length; ++i) {
                    if(!tabList.getById(i).getState()) {
                        this.removeEtuFromTable(tabList.getById(i).getNumber());
                    }
                }
            
            boolean autorised = false;
            ArrayList<Integer> freeTables = new ArrayList();
            for(int i = 0; i < tabList.getList().size(); ++i)
                if(tabList.getById(i).isActive() && (clean || tabList.getById(i).getState()))
                    freeTables.add(i);
            ArrayList<Integer> etu = new ArrayList();
            for(int i = 0; i < etuList.getList().size(); ++i)
                if(clean || !etuOnTable(i))
                    etu.add(i);
            
            for(int rand, id = 0; etu.size() > 0; id = 0) {
                // on trouve le maximum contrants pour faciliter le placement
                for(int i = 1; i < etu.size(); ++i) {
                    if(rulesList[etu.get(i)].size() > rulesList[etu.get(id)].size()) {
                        id = i;
                    }
                }
                // on va trouver le bon placement
                do {
                    rand = (int)(Math.random()*freeTables.size());
                    // on evite le random de 2 ou plus fois, a la meme table.
                    if(freeTables.size() <= 1) {
                        cleanTables(freeTables.get(rand));
                        System.out.println("return randomise");
                        return randomise(false, per_group);
                    }
                    if(tabList.getById(freeTables.get(rand)).getState()) {
                        putEtuOnTable(tabList.getById(freeTables.get(rand)).getNumber(), etuList.getById(etu.get(id)).getNum());
                        if(ckeckOut(tabList.getById(freeTables.get(rand)).getNumber(), per_group) != 1) {
                            freeTables.remove(rand);
                            autorised = true;
                            etu.remove(id);
                        }
                        else {
                            removeEtuFromTable(tabList.getById(freeTables.get(rand)).getNumber());
                            freeTables.remove(rand);
                        }
                    } else
                        freeTables.remove(rand);
                    
                } while(!autorised);
            }
        }
        return true;
    }
    
    /*
    public boolean randomise(boolean clean) {
        if(rulesPlace != null) {
            // clean les tables
            if(clean)
                for(int i = 0; i < rulesPlace.length; ++i) {
                    if(!tabList.getById(i).getState()) {
                        this.removeEtuFromTable(tabList.getById(i).getNumber());
                    }
                }
            // adapter le max pour la liste des etudiants
            boolean autorised;
            ArrayList<Table> freeTables = new ArrayList();
            for(int i = 0; i < tabList.getList().size(); ++i)
                //if(tabList.getById(i).isActive())
                if(tabList.getById(i).isActive() && (clean || tabList.getById(i).getState()))
                    freeTables.add(tabList.getList().get(i));
            ArrayList<Integer> etu = new ArrayList();
            for(int i = 0; i < etuList.getList().size(); ++i)
                if(clean || !etuOnTable(i))
                    etu.add(i);
            int targetsNb = etu.size();
            for(int rand, times = 0, id; times < targetsNb; ++times) {
                // on trouve le maximum contrants
                id = 0;
                for(int i = 1; i < etu.size(); ++i) {
                    if(rulesList[etu.get(i)].size() > rulesList[etu.get(id)].size()) {
                        id = i;
                    }
                }
                // on va voir le chifre randome pour la table et metre l'etudiant
                // dans ce table SSI -> Table est libre, les etudiant au tour
                // peuvent etre avec lui, si non, on relance le random
                autorised = false;
                do {
                    rand = (int)(Math.random()*freeTables.size());
                    System.out.println(times+") random: "+rand);
                    // on evite le random de 2 ou plus fois, a la meme table.
                    if(tabList.getByNum(freeTables.get(rand).getNumber()).getState()) {
                        putEtuOnTable(freeTables.get(rand).getNumber(), etuList.getById(etu.get(id)).getNum());
                        if(ckeckOut(freeTables.get(rand).getNumber()) != 1) {
                            freeTables.remove(rand);
                            autorised = true;
                        } else if(freeTables.size() == 1) return randomise(clean);
                        else removeEtuFromTable(freeTables.get(rand).getNumber());
                    }
                    
                } while(!autorised);
                etu.remove(id);
            }
        }
        return true;
    }    
    */
    
    public void checkEtudiants() {
        if(rulesList != null)
            for(int i = 0; i < rulesList.length; ++i) {
                System.out.print("\nL'etudiant "+etuList.getById(i).getName()+" ne peut pas d'etre avec: ");
                for(int e = 0; e < rulesList[i].size(); ++e) {
                    if(rulesList[i].get(e) >= 0) {
                        System.out.print(etuList.getById(rulesList[i].get(e)).getName());
                        if(e < rulesList[i].size()-1) System.out.print(" | ");
                    }
                }
            }
        System.out.println("");
    }
    
    public void checkTables() {
        if(rulesPlace != null)
            for(int i = 0; i < rulesPlace.length; ++i) {
                
            System.out.println(i);
                if(!tabList.getById(i).getState())
                    System.out.println("\n\nLa table "+tabList.getById(i).getNumber()+" est avec "+etuList.getById(etuTable[i]).getName());
                System.out.print("\nLa table "+tabList.getById(i).getNumber()+" est proche au: ");
                for(int e = 0; e < rulesPlace[i].size(); ++e) {
                    if(rulesPlace[i].get(e) >= 0) {
                        System.out.print(tabList.getById(rulesPlace[i].get(e)).getNumber());
                        if(e < rulesPlace[i].size()-1) System.out.print(" | ");
                    }
                }
            }
        System.out.println("");
    }
    
    public void test() {
        for(int i = 0; i < etuList.getList().size(); ++i)
            System.out.println(i+"| "+etuList.getById(i).getName()+" a "+rulesList[i].size()+" contrantes");
    }
    
    public boolean putEtuOnTable(int numTable, int numEtudiant) {
        if(etuTable[this.findTable(numTable)] == -1 && tabList.getByNum(numTable).isActive()) {
            tabList.getByNum(numTable).changeLibreState();
            etuTable[this.findTable(numTable)] = this.findEtudiant(numEtudiant);
            return true;
        } else return false;
    }
    
    public boolean forcedPutEtuOnTable(int numTable, int numEtudiant) {
        if(tabList.getByNum(numTable).isActive()) {
            for(int i = 0; i < etuTable.length; ++i) {
                if(etuTable[i] == etuList.findIndex(numEtudiant)) {
                    removeEtuFromTable(tabList.getById(i).getNumber());
                    System.out.println(""+tabList.getById(i).isActive());
                    i = etuTable.length;
                }
            }
            removeEtuFromTable(numTable);
            return putEtuOnTable(numTable, numEtudiant);
        } else return false;
    }
    
    public boolean removeEtuFromTable(int numTable) {
        System.out.println("Le num de table liberee "+numTable);
        if(etuTable[this.findTable(numTable)] == -1) return false;
        tabList.getByNum(numTable).changeLibreState();
        etuTable[this.findTable(numTable)] = -1;
        return true;
    }
    
    public void removeAllEtuFromTable() {
        for(int i = 0; i < etuTable.length; ++i) {
            if(etuTable[i] != -1) {
                tabList.getById(i).changeLibreState();
                etuTable[i] = -1;
            }
        }
    }
    
    public boolean checkEtudiants(int etu1, int etu2) {
        for(int e = 0; e < rulesList[etu1].size(); ++e) {
            if(rulesList[etu1].get(e) == etu2) return true;
        }
        return false;
    }
    
    // enleve les etudiants en tour de ce table.
    public void cleanTables(int tab) {
        tab = findTable(tab);
        
        if(tab >= 0 && tab < rulesPlace.length) {
           for(int t = 0; t < rulesPlace[tab].size() && rulesPlace[tab].get(t) != -1; ++t) {
                int etudiant = etuTable[rulesPlace[tab].get(t)];
                if(etuTable[rulesPlace[tab].get(t)] == -1) return;
                tabList.getById(rulesPlace[tab].get(t)).changeLibreState();
                etuTable[rulesPlace[tab].get(t)] = -1;
            }
        }        
    }
    
    public int ckeckOut(int tab, boolean per_group) {
        tab = findTable(tab);
        if(tab >= 0 && tab < rulesPlace.length) {
           for(int t = 0; t < rulesPlace[tab].size() && rulesPlace[tab].get(t) != -1; ++t) {
                int etudiants[] = {etuTable[tab], etuTable[rulesPlace[tab].get(t)]};
                
                if(etudiants[0] != -1 && etudiants[1] != -1)
                    if(checkEtudiants(etudiants[0], etudiants[1])
                    || (per_group && etuList.getById(etudiants[0]).getGroupe() == etuList.getById(etudiants[1]).getGroupe())) {                        
                        System.out.println(etuList.getById(etudiants[0]).getName()+" avec "+etuList.getById(etudiants[1]).getName());
                        // si l'etudiant ne peut pas situer avec lui
                        return 1;
                    }
            }
            // si ils sont loin
            return 0;
        }
        // si la table n'est pas connecte avec les autres
        return -1;
    }
    
    public void checkOut(boolean per_group) {
        for(int i = 0; i < etuTable.length; ++i) {
            ckeckOut(i, per_group);
        }
    }
    
    public Tables getTables() {
        return tabList;
    }
    
    public Etudiants getEtudiants() {
        return etuList;
    }
    
    public Etudiant getEtudiantTable(int numTable) {
        return etuList.getById(etuTable[numTable]);
    }
    
    public ArrayList<Integer> [] getRulesPlace() {
        return rulesPlace;
    }
    
    /////////// Julien
    public Etudiants getEtuList() {
        return etuList;
    }

    public int findIndexID(int etuNumber, int num) {
        for (int i = 0; i < rulesList[etuNumber].size(); ++i) {
            //System.out.println(rulesList[etuNumber].get(i));
            if (rulesList[etuNumber].get(i) == num) {
                return i;
            }
        }
        return -1;
    }

    public int[] getEtuTable() {
        return etuTable;
    }

    public Tables getTabList() {
        return tabList;
    }

    public ArrayList<Integer>[] getRulesList() {
        return rulesList;
    }

    public int numTable(int id) {
        for (int x = 0; x < etuTable.length; x++) {
            if (id == etuTable[x]) {
                return x;
            }
        }

        return -1;
    }

    public void setRulesPlace(ArrayList<Integer>[] rulesPlace) {
        this.rulesPlace = rulesPlace;
    }
     public void addEtudiantRuleS(int etuNumber, int target) {
        rulesList[etuNumber].add(target);
        rulesList[target].add(etuNumber);
    }
     

    public void removeEtudiantRuleS(int etuNumber, int target) {
        rulesList[etuNumber].remove(findIndexID(etuNumber, target));
        rulesList[target].remove(findIndexID(target, etuNumber));
    }
}
