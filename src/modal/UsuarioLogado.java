package modal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import jdbc.SqlLiteConnection;

public class UsuarioLogado {
	
	private int id;
	private int isPermanent;
	private int status;
	private String signinName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIsPermanent() {
		return isPermanent;
	}
	public void setIsPermanent(int isPermanent) {
		this.isPermanent = isPermanent;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSigninName() {
		return signinName;
	}
	public void setSigninName(String signinName) {
		this.signinName = signinName;
	}
	
	private final int TEMPO_MINUTOS = 10;
	
	public UsuarioLogado getUsuarioLogado() {
		
		String SQL = null;		
		ResultSet resultSet = null;
		Statement statement = null;
		boolean usuarioOk = false;
		
		try {
			
			do {
				
				SQL = " select * from accounts where status = 7 limit 1 ";
				
				statement = SqlLiteConnection.getConnection().createStatement();
				resultSet = statement.executeQuery(SQL);
				
				while (resultSet.next()) {
					
					setId(resultSet.getInt("id"));
					setIsPermanent(resultSet.getInt("is_permanent"));
					setStatus(resultSet.getInt("status"));
					setSigninName(resultSet.getString("signin_name"));
					usuarioOk = true;			
					
				}
				
				if (! resultSet.isClosed())
					resultSet.close();
				
				if (! usuarioOk) {
					
					try {

						TimeUnit.MINUTES.sleep(TEMPO_MINUTOS);
						
					 }
					 catch (InterruptedException ex) {

						 JOptionPane.showMessageDialog(null, "Exceção ao rodar sleep: " + ex.getMessage());
					 
					 }
					
				}
					
			}			
			while (! usuarioOk);
		}
		catch (SQLException ex) {
			usuarioOk = false;
			JOptionPane.showMessageDialog(null, "Atenção erro ao consultar o Usuário Skype: " + ex.getMessage());
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

}