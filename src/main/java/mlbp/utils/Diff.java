package mlbp.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diff {
	public List<Map<Integer, String>> diff(String diff){
    	List<String> lines = Arrays.asList(diff.split("\\n"));
    	Map<Integer, String> deletedLines = new HashMap<Integer, String>();
    	Map<Integer, String> addedLines = new HashMap<Integer, String>();
    	
    	int countDeletions = 0;
    	int countAdditions = 0;
    	
    	for (String line : lines){
    		countDeletions ++; countAdditions++;
    		
    		if (line.startsWith("---") || line.startsWith("+++"))
    			continue;
    		
    		if (line.startsWith("@@")){
    			List<Integer> numbers = getLineNumbers(line);
    			countDeletions = numbers.get(0);
    			countAdditions = numbers.get(1);
    		}
    		if (line.startsWith("-")){
    			deletedLines.put(countDeletions, formatLine(line));
    			countAdditions --;
    		} else if (line.startsWith("+")){
    			addedLines.put(countAdditions, formatLine(line));
    			countDeletions --;
    		}
    		
    	}
    	List<Map<Integer, String>> res = new ArrayList<Map<Integer, String>>();
    	res.add(addedLines);
    	res.add(deletedLines);
    	
    	return res;
    }

	private String formatLine(String line) {
		return line.replace("-", " ").replace("+", " ");
	}

	public List<Integer> getLineNumbers(String line) {
		String token[] = line.split(" ");
		String numbersOldFile = token[1];
		String numbersNewFile = token[2];
		int deleteLineNumber = Integer.parseInt(numbersOldFile.split(",")[0].replace("-", ""));
		int additionsLineNumber = Integer.parseInt(numbersNewFile.split(",")[0]);
		return Arrays.asList(-- deleteLineNumber, -- additionsLineNumber);
	}
}
