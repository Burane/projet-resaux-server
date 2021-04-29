package request.receive;

import BDDconnection.BDDConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gson.LocalDateAdapter;
import request.send.ErrorResponse;
import request.send.OneSearchDayResponse;
import request.send.SearchPerDayResponse;
import server.Client;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

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

			System.out.println("avant");
			ArrayList<OneSearchDayResponse> searchPerDays = selectStats(connection);
			System.out.println("apres");

			client.respond(new SearchPerDayResponse(searchPerDays));
			System.out.println("apres response");

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

		System.out.println("1");
		ArrayList<OneSearchDayResponse> searchPerDays = new ArrayList<>();
		System.out.println("2");

		long daysBetween = ChronoUnit.DAYS.between(dateFrom,dateTo);
		System.out.println("avant boucle");
		for (int i = 0; i < daysBetween; i++) {
			System.out.println("dans boucle "+i);

			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT COUNT(*) AS Nb_Recherche, CURDATE() - INTERVAL ? DAY AS Date FROM Recherche WHERE Date_Recherche BETWEEN CURDATE() - INTERVAL ? DAY AND CURDATE() - INTERVAL ? DAY ");

			preparedStatement.setInt(1, i);
			preparedStatement.setInt(2, i);
			preparedStatement.setInt(3, i - 1);

			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			LocalDate date = resultSet.getDate("Date").toLocalDate();
			int nbSearch = resultSet.getInt("Nb_Recherche");
			searchPerDays.add(new OneSearchDayResponse(date, nbSearch));
		}

		return searchPerDays;
	}

}