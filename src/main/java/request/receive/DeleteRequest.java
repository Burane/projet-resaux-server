package request.receive;

import BDDconnection.BDDConnection;
import request.GenericRequest;
import request.GenericRequestInterface;
import server.Client;

import java.sql.*;

public class DeleteRequest extends GenericRequest implements GenericRequestInterface {
	private int[] imageId;

	@Override
	public void handle(Client client) {

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
				}
			} catch (SQLException throwables) {

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