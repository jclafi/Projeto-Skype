package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import modal.Configuracao_Skype;

public class SqlLiteConnection {
	
	private Connection connection;
	private Configuracao_Skype objConfiguracao;
	
	public Connection getConnection() { return connection; }
	public void setConnection(Connection connection) { this.connection = connection; }
	public Configuracao_Skype getObjConfiguracao() { return objConfiguracao; }
	public void setObjConfiguracao(Configuracao_Skype objConfiguracao) { this.objConfiguracao = objConfiguracao; }
	
	public boolean getSQLLiteConnection() {

		try {

			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + objConfiguracao.getSkypeDatabase());	
			connection.setAutoCommit(true);
			
		} 
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Exceção ao Conectar na Base SQL Lite: " + ex.getMessage());
			System.out.println("Exceção ao conectar JDBC: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		} 
		
		return (connection != null);
		
	}

	public void closeSQLLiteConnection() {
		
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