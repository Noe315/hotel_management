package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
	//private static volatile DBConnection connectionSingleton = null;
	private Connection connection;
	private final String url = "jdbc:mysql:///hotel";
	private final String user = "sudo";
	private final String password = "12345";
	
	public DBConnection() throws SQLException
	{
		this.connection = DriverManager.getConnection(url, user, password); 
	}
	
	/*public static DBConnection GetDBConnectionInstance() throws SQLException
	{
		if (connectionSingleton == null)
		{
			synchronized(DBConnection.class)
			{
				if (connectionSingleton == null)
				{
					connectionSingleton = new DBConnection();
				}
			}
		}
		return connectionSingleton;
	}*/
	
	public Connection GetConnection()
	{
		return this.connection;
	}
	
	public void CloseConnection() throws SQLException
	{
		connection.close();
	}
}