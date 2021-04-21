package BDDconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDDConnection {
	private static BDDConnection instance;
	private Connection connection;

	private BDDConnection() {
		try {
			connection = DriverManager
					.getConnection(System.getProperty("database.url"), System.getProperty("database.user"),
							System.getProperty("database.password"));
		} catch (SQLException throwable) {
			throwable.printStackTrace();
		}
	}

	public static Connection getConnection() {
		if (instance == null) {
			instance = new BDDConnection();
		}
		return instance.connection;
	}

	public static void closeConnection() {
		if (instance != null) {
			try {
				instance.connection.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}
	}

}
