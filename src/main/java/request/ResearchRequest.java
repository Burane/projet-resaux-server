package request;

import BDDconnection.BDDConnection;
import server.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResearchRequest extends GenericRequest implements GenericRequestInterface {
	private String champ;

	@Override
	public void handle(Client client) {
		if(!client.isAuthentified())
			return;

//		Connection conn = BDDConnection.getConnection();
//		try {
//			PreparedStatement prepareStatement = conn.prepareStatement(
//					"SELECT COUNT(*) FROM `Utilisateur` WHERE `username` LIKE ? AND `password` LIKE ?");
//			prepareStatement.setString(1, username);
//			prepareStatement.setString(2, sha256Hash(password));
//			ResultSet resultSet = prepareStatement.executeQuery();
//			if (resultSet.next()) {
//				int count = resultSet.getInt(1);
//				return count == 1;
//			}
//
//		} catch (SQLException throwables) {
//			throwables.printStackTrace();
//		}
	}
}
