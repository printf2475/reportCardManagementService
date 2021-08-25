package reportCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtility {

	public static Connection getConnection() {

		Connection con = null;
		
		try {
			//properties conect
			Properties properties = new Properties();
			String path = StudentReportCardManagementService.class.getResource("db.properties").getPath();
			path = URLDecoder.decode(path, "utf-8");
			properties.load(new FileReader(path));
			//properties data binding
			String DRIVER = properties.getProperty("DRIVER");
			String URL = properties.getProperty("URL");
			String userId = properties.getProperty("USERID");
			String userPassword = properties.getProperty("USERPASSWORD");
			
			
			
			// JDBC driver load
			Class.forName(DRIVER);

			//  DB connect
			con = DriverManager.getConnection(URL, userId, userPassword);
			

		} catch (ClassNotFoundException e) {
			System.out.println("driver load fail");
		} catch (SQLException e2) {
			System.out.println("SQL fail");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Encoding fail");
		} catch (FileNotFoundException e) {
			System.out.println("properties FileNotFound");
		} catch (IOException e) {
			System.out.println("IO Exception");
		}
		
		
		return con;
	}
}
