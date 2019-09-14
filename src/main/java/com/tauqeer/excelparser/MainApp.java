package com.tauqeer.excelparser;

import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class MainApp {
	
	public static final String dataFile = "D:/rawdata.xlsx";
	public static final String outputFile = "D:/output.txt";
	
	public static void main(String[] args) throws IOException
	{
		FileParser parser = new FileParser(dataFile);
        // Getting the Sheet at index zero
        Sheet sheet = parser.getSheet(0);
        List<String> columnNames = parser.parseColumnNames(sheet);
        List<List<String>> parameterValues = parser.parseColumnValues(sheet, columnNames.size());
        
    	DataProcessor dataProcessor = new DataProcessor(parameterValues);
        
    	PrintStream out = new PrintStream(
    	        new FileOutputStream(outputFile, false), true);
    	System.setOut(out);
    	printValues(dataProcessor.getDataOfAllParameterValues(columnNames));
	}
	
	private static void printValues(Map<String, Map<String, Map<String, Integer>>> processedData)
	{
		Iterator pdIter = processedData.entrySet().iterator();
		while(pdIter.hasNext())
    	{
			Map.Entry<String, Map<String, Map<String, Integer>>> pdPair = 
					(Map.Entry<String, Map<String, Map<String, Integer>>>)pdIter.next();
			System.out.println("**************************************************************");
			System.out.println("Parameter Name: " + pdPair.getKey());
			
			// This map contains the value of each cellGroup, where the key corresponds to CellGroup
			Map<String, Map<String, Integer>> parameterValue = pdPair.getValue();
			Iterator pvIter = parameterValue.entrySet().iterator();
			while(pvIter.hasNext())
	    	{
				Map.Entry<String, Map<String, Integer>> pvPair = 
						(Map.Entry<String, Map<String, Integer>>)pvIter.next();
				System.out.println("CellGroup Name: "+ pvPair.getKey());
				
				// This map correspnds to Parameter Values, where the value corresponds to freq of each value
				Map<String, Integer> parameterValueFreq = pvPair.getValue();
				Iterator pvfIter = parameterValueFreq.entrySet().iterator();
				while(pvfIter.hasNext())
				{
					Map.Entry<String, Integer> pvfPair = 
							(Map.Entry<String, Integer>)pvfIter.next();
					System.out.println(pvfPair.getKey() + " " + pvfPair.getValue() +"\t");
				}
	    	}
			
			System.out.println();
			
			
    	}
	}

}
