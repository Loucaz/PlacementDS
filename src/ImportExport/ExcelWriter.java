package ImportExport;

import java.io.FileNotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import placementds.PlaceRules;

public class ExcelWriter {

    private static final String[] columns = {"NOM", "PRENOM", "TABLE"};

    public void ecrire(PlaceRules salle, String dest) {
        try {
            try (Workbook workbook = new XSSFWorkbook() // new HSSFWorkbook() for generating `.xls` file
                    ) {
                CreationHelper createHelper = workbook.getCreationHelper();
                // Create a Sheet
                Sheet sheet = workbook.createSheet("Placement DS");
                // Create a Font for styling header cells
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                headerFont.setColor(IndexedColors.BLACK.getIndex());
                // Create a CellStyle with the font
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);
                // Create a Row
                Row headerRow = sheet.createRow(0);
                // Create cells
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(headerCellStyle);
                }   // Create Cell Style for formatting Date
                int rowNum = 1;
                Row row;
                for (int x = 0; x < salle.getEtuList().getList().size(); x++) {
                    row = sheet.createRow(rowNum++);

                    row.createCell(0).setCellValue(salle.getEtuList().getById(x).getLastName());

                    row.createCell(1).setCellValue(salle.getEtuList().getById(x).getName());

                    row.createCell(2).setCellValue(salle.numTable(x));

                    //System.out.println(salle.getEtuList().getById(x).getLastName()+" "+salle.getEtuList().getById(x).getName()+": "+salle.numTable(x));
                }   // Resize all columns to fit the content size
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }   // Write the output to a file
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH.mm");
                String heure = sdf.format(new Date());
                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                try (FileOutputStream fileOut = new FileOutputStream(dest + "/" + "Placement_" + date + " " + heure + ".xlsx")) {
                    workbook.write(fileOut);
                    // Closing the workbook
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

}
