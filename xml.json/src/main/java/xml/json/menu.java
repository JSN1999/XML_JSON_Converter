package xml.json;

import java.util.Scanner;

public class menu extends Thread{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.println("Hi ! Welcome to this Data Migration Tool. This tool will fetch your table data in XML as well as JSON format.\nCheck the present directory of this application to view the data.\nBefore continuing, plz connect to your database.\n");
		DB_Connect.dbinput(in);
		//first checking data credentials
		base.run(DB_Connect.getConnectionURL(),DB_Connect.getTable(),DB_Connect.getUsername(),DB_Connect.getPassword());
		//executing tool
		System.out.println("\nThank you! Program will exit in 10 seconds....");
		Thread t = new Thread() {public void run(){try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}};
		//adding delay of 10 seconds before closing program.
		//It was done because we will call this .jar file from a .bat file
		//Thus, this dealy is added to give time to the user to read the status
		
		t.start();
	}

}
