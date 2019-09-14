package com.tauqeer.excelparser;

import java.util.*;

public class DataProcessor {

	private List<List<String>> data;
	private Set<String> cellGroupNames;
	public DataProcessor(List<List<String>> data)
	{
		this.data = data;
		cellGroupNames = new HashSet<>();
		List<String> allCellGroupNames = data.get(0);
		Iterator<String> cellGroupNameIterator = allCellGroupNames.iterator();
		
		// ignoring the col name
		cellGroupNameIterator.next();
		while(cellGroupNameIterator.hasNext())
		{
			cellGroupNames.add(cellGroupNameIterator.next());
		}
	}
	
	
	public Map<String, Map<String, Map<String, Integer>>> getDataOfAllParameterValues(List<String> parameterNames)
	{
		/**
		 * Contains the complete data.
		 * The first key corresponds to parameter name, 
		 * and the map map2 contains data of that parameter.
		 * For map2, the key corresponds to cellGroupName 
		 * and map map3 corresponds to the paramValues for that cellGrp.
		 * For map3, the key corresponds to the paramValue, 
		 * and the valure corresponds to the repition frequency.
		 */
		Map<String, Map<String, Map<String, Integer>>> allData = new HashMap<>();
		
		// -1 because the first list correspnds to cellGroupNames.
		int numOfParameters = data.size()-1;
		
		for(int i = 1; i < parameterNames.size(); i++)
		{
			
			String parameterName = parameterNames.get(i);
			allData.put(parameterName, getDataOfOneParameter(i));
		}
		
		return allData;
		
	}
	
	/**
	 * 
	 * @param paramColNum should Start from 1 because index 0 corresponds to Col Name
	 * @return The value of map corresponds to CellGroupName and the value is
	 * a map having key as the parameter, and the value being the frequency of that paramValue
	 */
	private Map<String, Map<String, Integer>> getDataOfOneParameter(final int paramColNum)
	{
		if(paramColNum < 1)
		{
			System.out.println("Invalid paramColNum:" + paramColNum);
		}
		//Map to contain Cellgroup and frequency of parameter values
		Map<String, Map<String, Integer>> parameterData = new HashMap<>();
		List<String> allParameterValues = data.get(paramColNum);
		
		// processing data
		List<String> allCellGroupNames = data.get(0);
		for(int i = 1; i < allParameterValues.size(); i++)
		{
			String parameterValue = allParameterValues.get(i);
			String cellGroupName = allCellGroupNames.get(i);
			
			if(parameterData.containsKey(cellGroupName))
			{
				Map<String, Integer> paramValuesMap = parameterData.get(cellGroupName);
				if(paramValuesMap.containsKey(parameterValue))
				{
					int frequency = paramValuesMap.get(parameterValue);
					frequency++;
					paramValuesMap.put(parameterValue, frequency);
				}
				else
				{
					paramValuesMap.put(parameterValue, 1);
				}
				
				parameterData.put(cellGroupName, paramValuesMap);
			}
			else
			{
				Map<String, Integer> paramValuesMap = new HashMap<String, Integer>();
				paramValuesMap.put(parameterValue, 1);
				parameterData.put(cellGroupName, paramValuesMap);
			}
		}
		
		return parameterData;
	}
	
	
	
}
