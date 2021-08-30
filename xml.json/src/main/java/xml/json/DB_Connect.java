package xml.json;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class DB_Connect extends Thread {
	
	private static boolean conn = false, conn_done = false;
	// these will be used in setting up a multi-threading environment
	private static String hostname, port, database, username, password, table;
	// necessary to declare static variables here as they cannot be marked static
	// inside a method
	
	public static String getConnectionURL() {
		return "jdbc:mysql://" + hostname + ":" + port + "/" + database;
	}
	public static String getUsername() {
		return username;
	}
	public static String getPassword() {
		return password;
	}
	public static String getTable() {
		return table;
	}
	public static void setTable(String table) {
		DB_Connect.table = table;
	}
	public static boolean connect(String hostname, String port, String database, String username, String password) {
		boolean result = false;
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// creates an instance for the above class
			con = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database,username, password);
			// we will create a connection object, it requires to create a connection first
			// we have setup mysql on xampp on port 3306, by default this is the port but
			// can be changes if needed.
			//we are only using it to check if the connection succeeds. If it fails, we simply throws exception
			System.out.println("\nConnection succeded!\n");
			result = true;
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("\nConnection failed! Try Again.\n");
			result = false;
		}
		finally {
			try {con.close();}catch(Exception e) {}
		}
		//necessary to prevent memory leaking
		//the order to close connections is from resultset, statement and then connection
		//but here only connection was present
		
		return result;
	}


	public static void dbinput(Scanner in) {

		do {
			// Note : I am setting up 2 threads, one is for creating a connection and the 2
			// is to print * after each 500ms
			// This is done to simulate behavior of a terminal shell, when any process in
			// undergoing
			// This gives user a feedback that system is trying to establish a connection
			// This is done as few remote ips may have a higher timeout, thus user might
			// consider it an error if no response is provided

			Thread t1 = new Thread() {
				public void run() {
					conn_done = false;
					conn = DB_Connect.connect(hostname, port, database, username, password);
					conn_done = true;
				}
			};
			// thread one simply checks for connection, if it fails it sets con as false and
			// the loop repeats asking all the details again
			// conn_done act as information sharing for thread 2 to stop its printing of *

			Thread t2 = new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
					while (!conn_done) {
						System.out.print(" * ");
						try {
							Thread.sleep(200);
						} catch (Exception e) {
						}
					}
				}
			};
			// thread simply printing * after every 200ms. Controlled using conn_done
			// variable

			System.out.println("Please enter the hostname :");
			hostname = in.next();
			System.out.println("Please enter the port number :");
			port = in.next();
			System.out.println("Please enter database name :");
			database = in.next();
			System.out.println("Please enter table name :");
			table = in.next();
			System.out.println("Please enter Username :");
			username = in.next();
			System.out.println("Please enter Password :");
			password = in.next();
			// taking user input

			System.out.println("Trying to Establish a Connection : ");
			t1.start();
			t2.start();
			while (t1.isAlive()) {
			}
			// necessary to prevent main loop condition to again ask for details even before
			// threads are terminated
		} while (!conn);
	}
}
