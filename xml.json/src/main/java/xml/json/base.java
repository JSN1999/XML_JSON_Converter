package xml.json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;


public class base {
	
	//private String hostname,port,database,username,password,table;
	
	public static void run(String connection_URL,String table,String username,String password) {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// creates an instance for the above class
			Connection con = DriverManager.getConnection(connection_URL,username,password);
			// we will create a connection object, it requires to create a connection first
			// we have setup mysql on xampp on port 3306, by default this is the port but
			// can be changes if needed.
			//we are only using it to check if the connection succeeds. If it fails, we simply throws exception
			
			PreparedStatement stmt = con.prepareStatement("desc "+table);
			//using prepared statement rather than statement object, to process query, thus preventing the chances of sql injection
			
			ResultSet rs = stmt.executeQuery();
			System.out.println("\nCollecting column data :");
			ArrayList<String> col = new ArrayList<String>();
			//creating arraylist collection to store columns
			
			while(rs.next()) {
				col.add(rs.getString(1));
			}
			System.out.println(col);
			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
			//creating arraylist to store records
			
			stmt = con.prepareStatement("select * from "+table);
			rs = stmt.executeQuery();
			System.out.println("\nAdding all Employee Records to arraylist:");
			int size = col.size();
			
			while(rs.next()) {
				ArrayList<String> temp = new ArrayList<String>();
				for(int i=1;i<=size;++i) {
					temp.add(rs.getString(i));
				}						
				data.add(temp);
			}
			for(ArrayList<String> i : data) {
				System.out.println(i);
			}
			
			System.out.println("\n\nCreating JSON & XML\n");
			
			JSONArray xml_data = new JSONArray();
			//making json array to store each json entry inside
			
			for(ArrayList<String> i : data) {
				JSONObject temp_json = new JSONObject();

				for(int a =0;a<size;++a) {
					temp_json.put(col.get(a),i.get(a));
				}
				xml_data.put(temp_json);
			}
			//adding records to json array
			
			System.out.println("\nJSON Data Generated :\n\n"+xml_data.toString()+"\n");
			System.out.println("\nXML Data Generated :\n\n"+XML.toString(xml_data));
			
			FileWriter writer = new FileWriter("data_output.json");  
		    BufferedWriter buffer = new BufferedWriter(writer);  
		    //using buffered writer to increase performance of writing as it uses internal buffer to write data
		    
		    buffer.write(xml_data.toString());  
		    buffer.close();  
		    System.out.println("\nJSON File Write Success");  
		    
		    writer = new FileWriter("data_output.xml");  
		    buffer = new BufferedWriter(writer);  
		    buffer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?><root>"+XML.toString(xml_data)+"</root>");
		    buffer.close();  
		    System.out.println("\nXML File Write Success");  

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
