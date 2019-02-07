package com.healthsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionFactory {
	public static Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
//			jdbc:sqlserver://localhost;user=MyUserName;password=*****;
			return DriverManager.getConnection("jdbc:sqlserver://healthsystem.database.windows.net:1433;database=HEALTHSYSTEM;user=health_admin@healthsystem;password=nfc_care_tcc_1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
		  
			/*return DriverManager.getConnection(
					"jdbc:sqlserver://healthsystem.database.windows.net:1433;databaseName=HEALTHSYSTEM",
					"health_admin", "nfc_care_tcc_1");
					*/
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
