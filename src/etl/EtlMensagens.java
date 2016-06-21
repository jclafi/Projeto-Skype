package etl;

import modal.Usuario_Logado;
import modal.Mensagens_Skype;
import jdbc.SqlLiteConnection;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;

public class EtlMensagens {
	
	private Usuario_Logado objUsuarioRegras; 
	private SessionFactory objSessionFactory;
	private SqlLiteConnection connectionSQLLite;	

	public Usuario_Logado getObjUsuario() { return objUsuarioRegras; }
	public void setObjUsuario(Usuario_Logado objUsuario) { this.objUsuarioRegras = objUsuario; }
	public SessionFactory getObjSessionFactory() { return this.objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }		
	
	public void executaCargaMensagens() {
		
		//Identifica o Usuario logado no Sistema
		objUsuarioRegras = carregaUsuario();
				
		if (objUsuarioRegras != null)
			executaETL();
		else
			JOptionPane.showMessageDialog(null, "Aten��o falha ao validar o Usu�rio Logado no Skype !");
		
	}
	
	private void executaETL() {
		
		int ultimoID = 0;
	
		Mensagens_Skype objMensagensRegra = new Mensagens_Skype();
		try {
			
			//Identifica o �ltimo ID salvo na base local de backup
			objMensagensRegra.setObjSessionFactory(objSessionFactory);
			ultimoID = objMensagensRegra.retornaUltimoID(objUsuarioRegras.getSigninName());
			
			//Consulta na base do Skype as Mensagens lan�adas com ID maior que o retornado acima
			String SQL = null;		
			ResultSet resultSet = null;
			PreparedStatement statement = null;			
			try {				
				
				SQL = " select id, chatname, author, from_dispname, body_xml, timestamp__ms " + 
						" from messages where body_xml is not null and id > ? order by timestamp__ms ";
				
				statement = connectionSQLLite.getConnection().prepareStatement(SQL);
				statement.setInt(1, ultimoID);
				resultSet = statement.executeQuery();
				
				while (resultSet.next()) {
					
					objMensagensRegra.setId(resultSet.getInt("id"));
					objMensagensRegra.setChat(resultSet.getString("chatname"));
					objMensagensRegra.setSender_display_name(resultSet.getString("from_dispname"));
					objMensagensRegra.setContent(resultSet.getString("body_xml"));
					objMensagensRegra.setId_sender(resultSet.getString("author"));						
					objMensagensRegra.setMessage_date(resultSet.getDate("timestamp__ms"));					
					objMensagensRegra.setAccount_logged(objUsuarioRegras.getSigninName());
					objMensagensRegra.setHost_name(InetAddress.getLocalHost().getHostName());
					objMensagensRegra.setIp_adress(InetAddress.getLocalHost().getHostAddress());
					objMensagensRegra.setAccount_verified("N");
					objMensagensRegra.setContact_verified("N");			
					
					//Identifica a origem das mensagens de acordo com a esta��o Cliente
					if (resultSet.getString("author").equals(objUsuarioRegras.getSigninName()))
						objMensagensRegra.setMessage_type("E");
					else
						objMensagensRegra.setMessage_type("R");					
					
					objMensagensRegra.salvaMensagem();
				}
				
				if (! statement.isClosed())
					statement.close();
				
			}
			finally {				
				if (statement != null)
					statement = null;
				if (resultSet != null)
					resultSet = null;
			}			
			
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Exce��o ao Importar Dados Skype. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (objMensagensRegra != null)
				objMensagensRegra = null;
		}
			
	}
	
	private Usuario_Logado carregaUsuario() {
		
		Usuario_Logado objUser = new Usuario_Logado();
		
		objUser.setConnectionSQLLite(connectionSQLLite);
		objUser.getUsuarioLogado();
		
		return objUser;
		
	}
	
	public void finalize() {
		
		if (objUsuarioRegras != null)
			objUsuarioRegras = null;
		
	}

}