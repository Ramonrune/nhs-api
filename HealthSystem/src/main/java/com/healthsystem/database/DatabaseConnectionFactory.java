package com.healthsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionFactory {
	public static Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
//			jdbc:sqlserver://localhost;user=MyUserName;password=*****;
			return DriverManager.getConnection("jdbc:sqlserver://;database=;user=@;password=;encrypt=;trustServerCertificate=;hostNameInCertificate=;loginTimeout=;");
		  
	
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
