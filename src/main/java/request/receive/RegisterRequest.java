package request.receive;

import BDDconnection.BDDConnection;
import request.GenericRequest;
import request.GenericRequestInterface;
import server.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterRequest extends GenericRequest implements GenericRequestInterface {
	public String username;
	public String password;

	public RegisterRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegisterRequest{" + "username='" + username + '\'' + ", password='" + password + '\'' + '}';
	}

	@Override
	public void handle(Client client) {
		Connection conn = BDDConnection.getConnection();
		try {
			PreparedStatement prepareStatement = conn
					.prepareStatement("INSERT INTO `Utilisateur` (`username`, `password`) VALUES (?, ?) ");
			prepareStatement.setString(1, username);
			prepareStatement.setString(2, sha256Hash(password));
			prepareStatement.execute();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}



}
