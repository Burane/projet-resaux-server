import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConBDD {
	public static void main(String args[]) {
		System.out.println("con bdd");
		try {
			Connection con = DriverManager
					.getConnection(System.getProperty("database.url"), System.getProperty("database.user"),
							System.getProperty("database.password"));
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `Utilisateur` ");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
