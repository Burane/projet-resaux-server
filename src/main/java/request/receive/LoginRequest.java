package request.receive;

import BDDconnection.BDDConnection;
import request.GenericRequest;
import request.GenericRequestInterface;
import server.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRequest extends GenericRequest implements GenericRequestInterface {
	public String username;
	public String password;

	public LoginRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequest{" + "username='" + username + '\'' + ", password='" + password + '\'' + '}';
	}

	@Override
	public void handle(Client client) {
		if(client.isAuthentified())
			return;
		boolean isAuthenticateSuccess = authenticate();
		client.setAuthentified(isAuthenticateSuccess);
		String response = isAuthenticateSuccess ? "Authentification success" : "Authentification error";
		client.respond(response);
	}

	public boolean authenticate() {
		Connection conn = BDDConnection.getConnection();
		try {
			PreparedStatement prepareStatement = conn.prepareStatement(
					"SELECT COUNT(*) FROM `Utilisateur` WHERE `username` LIKE ? AND `password` LIKE ?");
			prepareStatement.setString(1, username);
			prepareStatement.setString(2, sha256Hash(password));
			ResultSet resultSet = prepareStatement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count == 1;
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return false;
	}
}
