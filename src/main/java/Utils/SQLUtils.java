package Utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class SQLUtils {

	public static String printResultSet(ResultSet resultSet) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) {
						stringBuilder.append("  |  ");
					}
					String columnValue = resultSet.getString(i);
					stringBuilder.append(rsmd.getColumnName(i)).append("  ").append(columnValue);
				}
				stringBuilder.append("\n");
			}
		} catch (SQLException throwable) {
			throwable.printStackTrace();
		}
		return stringBuilder.toString();
	}
}
