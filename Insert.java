/*
 * API Insert Operation
 */
public class Insert {
	 public static void parseInsertString(String insertString) {
			System.out.println("In INSERT Functionality ");
			System.out.println("Input Parsed:\"" + insertString + "\"");
			
			String[] tokens=insertString.split(" ");
			String table = tokens[2];
			String[] temp = insertString.split("values");
			String temporary=temp[1].trim();
			String[] insertValues = temporary.substring(1, temporary.length()-1).split(",");
			for(int i = 0; i < insertValues.length; i++)
				insertValues[i] = insertValues[i].trim();
			if(!DavisBase.tableExists(table)){
				System.out.println("Table name "+table+" not found!!");
			}
			else
			{
				CreateTable.insertInto(table, insertValues);
			}

		}
	    
}
