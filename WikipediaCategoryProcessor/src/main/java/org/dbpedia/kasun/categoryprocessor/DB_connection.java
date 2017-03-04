/*
 * DO NOT MODIFY THIS FILE (it is already completed and should not be changed).
 */

package org.dbpedia.kasun.categoryprocessor;
import java.sql.*;

/**
 * This class has Database Connection Details. 
 * If you want to run on your local machine, just update the user name,password and database name.
 */
public class DB_connection {
	public DB_connection() {};

	public Connection dbConnect() {	
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbpediadb","root","shacha");
//			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306://139.59.235.4//dbpedia","root","thanks123");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
