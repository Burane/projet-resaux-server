package request.receive;

import BDDconnection.BDDConnection;
import request.send.ErrorResponse;
import request.send.OneSearchDayResponse;
import request.send.SearchPerDayResponse;
import server.Client;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SearchPerDayRequest extends GenericRequest implements GenericRequestInterface {
	LocalDate dateFrom;
	LocalDate dateTo;

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
		try {

			ArrayList<OneSearchDayResponse> searchPerDays = selectStats(connection);

			client.respond(new SearchPerDayResponse(searchPerDays));

		} catch (SQLException throwable) {
			throwable.printStackTrace();
			client.respond(new ErrorResponse(throwable.getMessage()));
			try {
				connection.rollback(save);
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}

	}

	private ArrayList<OneSearchDayResponse> selectStats(Connection connection) throws SQLException {

		ArrayList<OneSearchDayResponse> searchPerDays = new ArrayList<>();


		for (LocalDate date : dateFrom.datesUntil(dateTo.plusDays(1)).collect(Collectors.toList())) {

			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT COUNT(*) AS Nb_Recherche from Recherche WHERE DATE(`Date_Recherche`) = ? ");

			preparedStatement.setDate(1, Date.valueOf(date));

			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int nbSearch = resultSet.getInt("Nb_Recherche");
			searchPerDays.add(new OneSearchDayResponse(date, nbSearch));
		}

		return searchPerDays;
	}

}