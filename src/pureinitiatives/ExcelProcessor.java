/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pureinitiatives;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author msesi
 */
public class ExcelProcessor {
    
    private ArrayList<Author> autores;
    private String fileName;
    
    public ExcelProcessor(String fileName)
    {
        this.fileName = fileName;
        this.autores = new ArrayList<Author>();
    }
    
    public void openFile()
    {
        try {
            Workbook wb = WorkbookFactory.create(new FileInputStream(fileName));
            //
            Sheet sheet1 = wb.getSheetAt(0);
            setAUs(sheet1);
            Sheet sheet2 = wb.getSheetAt(1);
            setBool(sheet2);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(ExcelProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setAUs(Sheet sheet)
    {
        for (Row row : sheet) 
        {
            if(row.getCell(0)==null)
                break;
            if (row.getRowNum() >=1)
            {
                if(row.getCell(0)!=null)
                {
                    autores.add(new Author(getCellString(row.getCell(0)), getCellString(row.getCell(1)), getCellString(row.getCell(2)), getCellString(row.getCell(3)), getCellIntValue(row.getCell(4)), getCellIntValue(row.getCell(5)), getCellIntValue(row.getCell(6)), getCellIntValue(row.getCell(7)), getCellIntValue(row.getCell(8)), getCellIntValue(row.getCell(9)), getCellIntValue(row.getCell(10))));                    
                }
            }
        }
        
    }
    
    public void setBool(Sheet sheet)
    {
        for (Row row : sheet) 
        {
            if(row.getCell(0)==null)
                break;
            if (row.getRowNum() >=1)
            {
                if(row.getCell(0)!=null)
                {
                       
                }
            }
        }
        
    }
    
    public String getCellString(Cell cell)
    {
        if(cell!=null)
        {
            if(cell.getCellType() == CellType.NUMERIC)
                return NumberToTextConverter.toText(cell.getNumericCellValue()).trim();
            else
                return cell.toString().trim();
        }
        else
            return "";
    }
    
    public double getCellValue(Cell cell)
    {
        if(cell.getCellType() == CellType.NUMERIC)
            return cell.getNumericCellValue();
        else
            return Double.parseDouble(cell.toString());
    }
    
    public int getCellIntValue(Cell cell)
    {
        if(cell.getCellType() == CellType.NUMERIC)
            return (int) cell.getNumericCellValue();
        else
            return Integer.parseInt(cell.toString());
    }
    
}
