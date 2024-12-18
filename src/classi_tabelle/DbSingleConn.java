package classi_tabelle;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton class that provides the connection to the database with parameters: data about
 *  connection, database, user, password, etc. are read from properties file
 */
public class DbSingleConn {

	private static Connection pConn;
	private DbSingleConn() throws SQLException, IOException {}

	protected static Connection getConnection() throws SQLException,
			IOException {
		if (pConn == null) {
			try {
				pConn = createConnection();
			}catch (Exception ce){ 
				ce.printStackTrace();
			}
		}
		return pConn;
	}

	protected static void closeConnection() throws SQLException, IOException {
		pConn.close();
	}

	protected static void releaseConnection() throws SQLException, IOException {
		pConn.close();
		pConn = null;
	}

	protected static void commitConnection() throws SQLException , IOException{
		pConn.commit();
	}
	
	protected static void rollbackConnection() throws SQLException , IOException{
		pConn.rollback();
	}
	
	private static Connection createConnection() throws SQLException, IOException {
		Properties props = new Properties();
		FileInputStream in = new FileInputStream("src/TRM/database.properties");
		props.load(in);
		in.close();

		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null)
			System.setProperty("jdbc.drivers", drivers);
		String url = props.getProperty("jdbc.url");
		String server = props.getProperty("jdbc.server");
		String db = props.getProperty("jdbc.db");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");
		String s = url + "//" + server + "/" + db  + "?serverTimezone=UTC" + "&user=" + username
				+ "&password=" + password;

		return DriverManager.getConnection(s);	
	}
}
