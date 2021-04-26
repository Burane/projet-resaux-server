package request.receive;

import BDDconnection.BDDConnection;
import request.send.ErrorResponse;
import request.send.SuccessResponse;
import server.Client;

import java.sql.*;

public class DeleteRequest extends GenericRequest implements GenericRequestInterface {
	private int[] imageId;

	@Override
	public void handle(Client client) {

		if (!client.isAuthentified()) {
			client.respond(new ErrorResponse("Not authentified").toJson());
			return;
		}

		Connection connection = BDDConnection.getConnection();

		for (int id : imageId) {

			Savepoint save = null;
			try {
				connection.setAutoCommit(false);
				save = connection.setSavepoint();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}

			try {
				boolean isUserImage = verifyImagePossessor(connection, id, client.getUserId());
				if (isUserImage) {
					deleteFromPossede(connection, id);
					deleteFromUpload(connection, id);
					deleteImage(connection, id);
				} else {
					client.respond(new ErrorResponse("Image : " + id + " is not your image.").toJson());
				}
			} catch (SQLException throwables) {
				client.respond(new ErrorResponse(throwables.getMessage()).toJson());
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
			client.respond(new SuccessResponse("Image : " + id + " successfully deleted.").toJson());
		}

	}

	private boolean verifyImagePossessor(Connection connection, int id, int userId) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT COUNT(*) FROM Upload WHERE Id_Image = ? AND Id_Utilisateur = ? ");
		preparedStatement.setInt(1, id);
		preparedStatement.setInt(2, userId);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getInt(1) > 0;
		}
		return false;
	}

	private void deleteImage(Connection connection, int id) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Image WHERE Id_Image = ?");
		preparedStatement.setInt(1, id);
		preparedStatement.execute();
	}

	private void deleteFromUpload(Connection connection, int id) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Upload WHERE Id_Image = ?");
		preparedStatement.setInt(1, id);
		preparedStatement.execute();
	}

	private void deleteFromPossede(Connection connection, int id) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Possede WHERE Id_Image = ?");
		preparedStatement.setInt(1, id);
		preparedStatement.execute();
	}
}