import java.io.RandomAccessFile;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ShowTables {

	/*
	 * API Display Tables
	 */
	public static void showTables() {
		System.out.println("In SHOW Functionality");
		System.out.println("Parsing the string:\"show tables\"");

		String table_name = "davisbase_tables";
		String[] columns = { "table_name" };
		String[] cmptr = new String[0];
		select(table_name, columns, cmptr);
	}

	public static void select(String table, String[] cols, String[] cmp) {
		try {

			RandomAccessFile file = new RandomAccessFile("data/" + table + ".tbl", "rw");
			String[] columns = Table.getColName(table);
			String[] type = Table.getDataType(table);

			Buffer buffer = new Buffer();

			Table.filter(file, cmp, columns, type, buffer);
			buffer.display(cols);
			file.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
