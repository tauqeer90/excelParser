package com.tauqeer.excelparser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainApp {
	
	public static final String RAW_DATA_FILE = "D:/rawdata.xlsx";
	
	public static void main(String[] args) throws IOException, InvalidFormatException
	{
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(new File(RAW_DATA_FILE));
        
        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        System.out.println("Retrieving Sheets using Iterator");
        
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            System.out.println("=> " + sheet.getSheetName());
        }
        
        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        List<String> columnNames = parseColumnNames(sheet);
        List<List<String>> parameterValues = parseParameterValues(sheet, columnNames.size());
	}
	
	
	private static List<List<String>> parseParameterValues(Sheet sheet, int parameterNum)
	{
		List<List<String>> parameterValues = new ArrayList<>();
		for(int i = 0; i < parameterNum; i++)
			parameterValues.add(new ArrayList<String>());
		
		DataFormatter dataFormatter = new DataFormatter();
		
        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
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
                parameterValues.add(colIndex, colList);
            }
        }
		System.out.println(parameterValues.get(0).size());
		return parameterValues;
	}
	
	private static List<String> parseColumnNames(Sheet sheet)
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
