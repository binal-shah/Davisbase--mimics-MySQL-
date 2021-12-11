import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.SortedMap;
public class DeleteTable {
	/*
	 * API to parse inputs to DELETE functionality
	 */
	public static void parseDeleteString(String deleteString) {
		System.out.println("In DELETE Functionality");
		System.out.println("Input parsed:\"" + deleteString + "\"");
		
		String[] tokens=deleteString.split(" ");
		String table = tokens[2];
		String[] temp = deleteString.split("where");
		String cmpTemp = temp[1];
		String[] cmp = DavisBase.parserEquation(cmpTemp);
		if(!DavisBase.tableExists(table)){
			System.out.println("Table name: "+table+" not found!!");
		}
		else
		{
			delete(table, cmp);
		}
	}
	/*
	 * API Delete
	 */
	public static void delete(String table, String[] cmp){
		try{
		int key = new Integer(cmp[2]);

		RandomAccessFile file_pointer = new RandomAccessFile("data/"+table+".tbl", "rw");
		int numPages = Table.pages(file_pointer);
		int page = 0;
		for(int p = 1; p <= numPages; p++)
			if(Page.hasKey(file_pointer, p, key)&Page.getPageType(file_pointer, p)==0x0D){
				page = p;
				break;
			}
		
		if(page==0)
		{
			System.out.println("Key not found!");
			return;
		}
		
		short[] cellsAddr = Page.getCellArray(file_pointer, page);
		int k = 0;
		for(int i = 0; i < cellsAddr.length; i++)
		{
			long loc = Page.getCellLoc(file_pointer, page, i);
			String[] values = Table.retrieveValues(file_pointer, loc);
			int x = new Integer(values[0]);
			if(x!=key)
			{
				Page.setCellOffset(file_pointer, page, k, cellsAddr[i]);
				k++;
			}
		}
		Page.setCellNumber(file_pointer, page, (byte)k);
		
		}catch(Exception e)
		{
			System.out.println(e);
		}
		
	}

}
