package modal;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import jdbc.SqlLiteConnection;

public class Conta_Login {
	
	private int id;
	private int isPermanent;
	private int status;
	private String signinName;
	private SqlLiteConnection connectionSQLLite;
	private final long TEMPO_MINUTOS = 1;	
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public int getIsPermanent() { return isPermanent; }
	public void setIsPermanent(int isPermanent) { this.isPermanent = isPermanent; }
	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }
	public String getSigninName() { return signinName; }
	public void setSigninName(String signinName) { this.signinName = signinName; }
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }	
	
	public Conta_Login carregaContaAtiva() {
		
		String SQL = null;		
		ResultSet resultSet = null;
		Statement statement = null;
		boolean usuarioOk = false;
		
		try {
			
			do {
				
				SQL = " select * from accounts where status = 7 limit 1 ";
				
				statement = connectionSQLLite.getConnection().createStatement();
				resultSet = statement.executeQuery(SQL);
				
				while (resultSet.next()) {
					
					setId(resultSet.getInt("id"));
					setIsPermanent(resultSet.getInt("is_permanent"));
					setStatus(resultSet.getInt("status"));
					setSigninName(resultSet.getString("signin_name"));
					usuarioOk = true;			
					
				}
				
				if (! statement.isClosed())
					statement.close();
								
				if (! usuarioOk) {
					
					try {

						TimeUnit.MINUTES.sleep(TEMPO_MINUTOS);
						
					 }
					 catch (InterruptedException ex) {

						 Erros_Skype_Static.salvaErroSkype("Exceção no sleep Login Skype: " + ex.getMessage());
					 
					 }
					
				}
					
			}			
			while (! usuarioOk);
		}
		catch (Exception ex) {
			usuarioOk = false;
			Erros_Skype_Static.salvaErroSkype("Atenção erro ao consultar o Usuário Login Skype: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
		finally {
			
			if (statement != null)
				statement = null;
			if (resultSet != null)
				resultSet = null;
			
		}
		
		return this;
		
	}
	
	public void carregaContaLogin() { 
		
		String SQL = null;		
		ResultSet resultSet = null;
		Statement statement = null;
		
		try {
						
			SQL = " select * from accounts limit 1 ";
				
			statement = connectionSQLLite.getConnection().createStatement();
			resultSet = statement.executeQuery(SQL);
				
			while (resultSet.next()) {
					
				setId(resultSet.getInt("id"));
				setIsPermanent(resultSet.getInt("is_permanent"));
				setStatus(resultSet.getInt("status"));
				setSigninName(resultSet.getString("signin_name"));
					
			}
				
			if (! statement.isClosed())
				statement.close();
								
							
		}
		catch (Exception ex) {
			Erros_Skype_Static.salvaErroSkype("Atenção erro ao consultar o Usuário Login Skype: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			
			if (statement != null)
				statement = null;
			if (resultSet != null)
				resultSet = null;
			
		}
		
	}

}