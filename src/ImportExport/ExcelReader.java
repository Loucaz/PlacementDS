package ImportExport;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import placementds.Etudiant;

public class ExcelReader {

    private final String fileName; //il s'agit du nom du fichier Excel que l'on veut lire
    private String tab[][];
    private int maxI;
    private int maxJ;

    public ExcelReader(String file) {
        this.fileName = file;
        read();
    }

    public final void read() {

        try {
            Workbook workbook;
            File f = new File(fileName);
            workbook = WorkbookFactory.create(f);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            int i = 0;
            int j = 0;

            for (Row row : sheet) {
                for (Cell cell : row) {
                    j++;
                }
                i++;
                maxJ = j;
                j = 0;
            }
            maxI = i;
            tab = new String[1000][1000];
            i = 0;
            j = 0;
            for (Row row : sheet) {
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    tab[i][j] = cellValue;
                    j++;
                }
                i++;
                j = 0;
            }
        } catch (IOException | InvalidFormatException | EncryptedDocumentException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERREUR");
        }

    }

    public boolean contientNombre(String chaine) {
        for (int i = 0; i < 4; i++) {
            if (chaine.contains(String.valueOf(i))) {
                return true;
            }
        }
        return false;
    }

    public int trouveNum() {
        int num;
        for (int i = 0; i < 4; i++) {
            if ("NUM".equals(tab[0][i])) {
                return i;
            }
        }

        return 4;
    }

    public int trouveNom() {
        int num;
        for (int i = 0; i < 4; i++) {
            if ("NOM".equals(tab[0][i])) {
                return i;
            }
        }

        return 4;
    }

    public int trouvePrenom() {
        int num;
        for (int i = 0; i < 4; i++) {
            if ("PRENOM".equals(tab[0][i])) {
                return i;
            }
        }
        return 4;
    }

    public int trouveGroupe() {
        int num;
        for (int i = 0; i < 4; i++) {
            if ("GROUPE".equals(tab[0][i])) {
                return i;
            }
        }
        return 4;
    }

    public ArrayList<Etudiant> ajout() {
        ArrayList<Etudiant> list = new ArrayList<>();
        for (int i = 1; i < maxI; i++) {
            if (isGood() == false) {
                return list;
            }
            int cNum = trouveNum();
            int cNom = trouveNom();
            int cPrenom = trouvePrenom();
            int cGroupe = trouveGroupe();
            String num = "", nom = "", prenom = "", groupe = "";

            num = tab[i][cNum];
            nom = tab[i][cNom];
            prenom = tab[i][cPrenom];
            groupe = tab[i][cGroupe];
            if (!"".equals(num) && !"".equals(nom) && !"".equals(prenom) && !"".equals(groupe)) {
                list.add(new Etudiant(Integer.parseInt(num), prenom, nom, Integer.parseInt(groupe)));
            }

        }
        return list;
    }

    public boolean isGood() {
        if (maxJ > 3) {
            if (trouveNum() == 4 || trouveNom() == 4 || trouvePrenom() == 4 || trouveGroupe() == 4) {
                return false;
            }
            return true;
        }
        return false;
    }

}
