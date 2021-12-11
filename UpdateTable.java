import java.io.RandomAccessFile;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UpdateTable {
	
	public static void parseUpdateString(String updateString) {
		System.out.println("In UPDATE Functionality");
		System.out.println("Input parsed:\"" + updateString + "\"");
		
		String[] tokens=updateString.split(" ");
		String table_name = tokens[1];
		String[] tmp_1 = updateString.split("set");
		String[] tmp_2 = tmp_1[1].split("where");
		String cmp_temp = tmp_2[1];
		String set_temp = tmp_2[0];
		String[] cmp = DavisBase.parserEquation(cmp_temp);
		String[] set = DavisBase.parserEquation(set_temp);
		if(!DavisBase.tableExists(table_name)){
			System.out.println("Table "+table_name+" not found!!");
		}
		else
		{
			update(table_name, cmp, set);
		}
		
	}
	
	public static void update(String table, String[] cmp, String[] set){
		try{
			
			int keyValue = new Integer(cmp[2]);
			
			RandomAccessFile file_pointer = new RandomAccessFile("data/"+table+".tbl", "rw");
			int numberOfPages = Table.pages(file_pointer);
			int page = 0;
			for(int p = 1; p <= numberOfPages; p++)
				if(Page.hasKey(file_pointer, p, keyValue)&Page.getPageType(file_pointer, p)==0x0D){
					page = p;
				}
			
			if(page==0)
			{
				System.out.println("key not found!!");
				return;
			}
			
			int[] keys = Page.getKeyArray(file_pointer, page);
			int x = 0;
			for(int i = 0; i < keys.length; i++)
				if(keys[i] == keyValue)
					x = i;
			int offset = Page.getCellOffset(file_pointer, page, x);
			long loc = Page.getCellLoc(file_pointer, page, x);
			
			String[] columns = Table.getColName(table);
			String[] values = Table.retrieveValues(file_pointer, loc);

			String[] datatype = Table.getDataType(table);
			for(int i=0; i < datatype.length; i++)
				if(datatype[i].equals("DATE") || datatype[i].equals("DATETIME"))
					values[i] = "'"+values[i]+"'";

			for(int i = 0; i < columns.length; i++)
				if(columns[i].equals(set[0]))
					x = i;
			values[x] = set[2];

			String[] nullable = Table.getNullable(table);
			for(int i = 0; i < nullable.length; i++){
				if(values[i].equals("null") && nullable[i].equals("NO")){
					System.out.println("Violation of IS_NULLABLE constraint");
					return;
				}
			}

			byte[] stc = new byte[columns.length-1];
			int payloadsize = Table.calPayloadSize(table, values, stc);
			Page.updateLeafCell(file_pointer, page, offset, payloadsize, keyValue, stc, values);

			file_pointer.close();

		}catch(Exception e){
			System.out.println(e);
		}
	}

}
