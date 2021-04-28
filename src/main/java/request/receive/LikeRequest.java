package request.receive;

import BDDconnection.BDDConnection;
import request.send.ErrorResponse;
import request.send.LikeResponse;
import server.Client;

import java.sql.*;

public class LikeRequest extends GenericRequest implements GenericRequestInterface {
	private int imageId;

	@Override
	public void handle(Client client) {

		if (!client.isAuthentified()) {
			client.respond(new ErrorResponse("Not authentified"));
			return;
		}

		Connection connection = BDDConnection.getConnection();

		Savepoint save = null;
		try {
			connection.setAutoCommit(false);
			save = connection.setSavepoint();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		boolean isLikedByUser = false;
		try {
			isLikedByUser = changeLikeStatus(connection, imageId, client.getUserId());

			if (isLikedByUser)
				deleteLike(connection, imageId, client.getUserId());
			else
				like(connection, imageId, client.getUserId());

		} catch (SQLException throwables) {
			client.respond(new ErrorResponse(throwables.getMessage()));
			try {
				connection.rollback(save);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			throwables.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}
		client.respond(new LikeResponse(imageId, !isLikedByUser));

	}

	private boolean changeLikeStatus(Connection connection, int idImage, int idUser) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT COUNT(*) as isLikedByUser FROM Note WHERE Id_Image = ? AND Id_Utilisateur = ? ");
		preparedStatement.setInt(1, idImage);
		preparedStatement.setInt(2, idUser);

		ResultSet resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getBoolean("isLikedByUser");
	}

	private void like(Connection connection, int idImage, int idUser) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("INSERT INTO Note (Id_Image, Id_Utilisateur) VALUES (?, ?)");
		preparedStatement.setInt(1, idImage);
		preparedStatement.setInt(2, idUser);
		preparedStatement.execute();
	}

	private void deleteLike(Connection connection, int idImage, int idUser) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM Note WHERE Id_Image = ? AND Id_Utilisateur = ? ");
		preparedStatement.setInt(1, idImage);
		preparedStatement.setInt(2, idUser);
		preparedStatement.execute();
	}
}