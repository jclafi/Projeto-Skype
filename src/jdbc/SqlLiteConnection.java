package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
import javax.swing.JOptionPane;
import dao.Configuracao_Skype;

public class SqlLiteConnection {
	
	private static Connection connection;
	public static Connection getConnection() { return connection; }
	public static void setConnection(Connection connection) { SqlLiteConnection.connection = connection; }
	
	private static String getDatabasePath() {	
		
		String objTemp = "";
		
		Configuracao_Skype objConfiguracao = new Configuracao_Skype(Configuracao_Skype.CODIGO_CONFIGURACAO);
		try {
	
			objTemp = objConfiguracao.getSkypeDatabase();
			
		}
		finally {
			if (objConfiguracao != null)
				objConfiguracao = null;
		}
		
		return objTemp;
		
	}
	
	public static boolean getSQLLiteConnection() {

//		ResultSet resultSet = null;
//		Statement statement = null;
//		"jdbc:sqlite:C:\\Users\\Julio\\AppData\\Roaming\\Skype\\juliocesar_floripa\\main.db"

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(getDatabasePath());			
			connection.setAutoCommit(true);
			
//			statement = connection.createStatement();
//			resultSet = statement.executeQuery("SELECT EMPNAME FROM EMPLOYEEDETAILS");
//			while (resultSet.next()) {
//				System.out.println("EMPLOYEE NAME:" + resultSet.getString("EMPNAME"));
//			}
		} 
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Exceção ao Conectar na Base SQL Lite: " + ex.getMessage());
			System.out.println("Exceção ao conectar JDBC: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		} 
		
		return true;
		
	}

	public static void closeSQLLiteConnection() {
		
		if (connection != null) {
			
			try {
				if (! connection.isClosed())
					connection.close();
				connection = null;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Exceção ao Des-Conectar na Base SQL Lite: " + ex.getMessage());
				ex.printStackTrace();
			} 
			
		}
		
	}
	
}