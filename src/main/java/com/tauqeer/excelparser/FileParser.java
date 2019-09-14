package com.tauqeer.excelparser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileParser {
	
	private Workbook workbook = null;
	private Iterator<Sheet> sheetIterator = null;
	
	public FileParser(final String filePath)
	{
		try
		{
			// Creating a Workbook from an Excel file (.xls or .xlsx)
			workbook = WorkbookFactory.create(new File(filePath));
	        // Retrieving the number of sheets in the Workbook
	        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
	        sheetIterator = workbook.sheetIterator();
	        while (sheetIterator.hasNext()) {
	            Sheet sheet = sheetIterator.next();
	            System.out.println("=> " + sheet.getSheetName());
	        }
		}
		catch (IOException | InvalidFormatException ex)
		{
			System.out.println("Exception caught while parsing excel sheet: " + ex.toString());
		}
	}
	
	public Sheet getSheet(final int sheetNum)
	{
		return workbook.getSheetAt(sheetNum);
	}
	
	public List<List<String>> parseColumnValues(final Sheet sheet, final int parameterNum)
	{
		List<List<String>> parameterValues = new ArrayList<>();
		for(int i = 0; i < parameterNum; i++)
			parameterValues.add(new ArrayList<String>());
		
		DataFormatter dataFormatter = new DataFormatter();
		
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();
            int colIndex = 0;
            
            // Ignoring the first col
            cellIterator.next();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                List<String> colList = parameterValues.get(colIndex);
                if(colList != null && cellValue.length() != 0 && cellValue.trim().length() > 0)
                	colList.add(cellValue);
                parameterValues.set(colIndex, colList);
                colIndex++;
            }
        }
		return parameterValues;
	}
	
	public List<String> parseColumnNames(Sheet sheet)
	{
		Iterator<Row> rowIterator = sheet.rowIterator();
		List<String> columnNames = new ArrayList<>();
		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();
		
		if(!rowIterator.hasNext())
			return columnNames;
		
		Row row = rowIterator.next();
		Iterator<Cell> cellIterator = row.cellIterator();
		if(!cellIterator.hasNext())
			return columnNames;
		
		// Ignoring the First Column
		cellIterator.next();
		
		while(cellIterator.hasNext())
		{
            Cell cell = cellIterator.next();
            String cellValue = dataFormatter.formatCellValue(cell);
            if(cellValue == null || cellValue.length() == 0 || cellValue.trim().length() == 0)
            	continue;
            System.out.print(cellValue + "\t");
            columnNames.add(cellValue);
		}
		return columnNames;
	}
	

}
