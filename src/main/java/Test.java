import BDDconnection.BDDConnection;
import Utils.SQLUtils;

import java.io.*;
import java.sql.*;
import java.util.Base64;

public class Test {



	public static void main(String[] args) {
		File file = new File("C:\\Users\\antoi\\Pictures\\15e31c4b27e4501a8abd64182acbaad5.jpg");
		try {
			FileInputStream in = new FileInputStream(file);

			System.out.println( new String(Base64.getEncoder().encode(in.readAllBytes()), "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Connection connection = BDDConnection.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Tag (Libelle) VALUES ('ezazaaaea') ON DUPLICATE KEY UPDATE Id_Tag=Id_Tag",
					Statement.RETURN_GENERATED_KEYS);

			preparedStatement.execute();

			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			System.out.println("after");
//			System.out.println(SQLUtils.printResultSet(resultSet));
			if (resultSet.next())
				System.out.println("id : "+resultSet.getInt(1));

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}

}

