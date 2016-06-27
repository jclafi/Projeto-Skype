package etl;

import modal.Conta_Login;
import modal.Contas_Skype;
import modal.Contatos_Contas_Skype;
import modal.Mensagens_Skype;
import jdbc.SqlLiteConnection;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;

public class EtlMensagens {
	
	private Conta_Login objUsuarioRegras; 
	private SessionFactory objSessionFactory;
	private SqlLiteConnection connectionSQLLite;	
	private Contas_Skype objContasSkype;
	private Set<Contatos_Contas_Skype> objListaContatosContaSkype = new HashSet<Contatos_Contas_Skype>();
	
	public Conta_Login getObjUsuario() { return objUsuarioRegras; }
	public void setObjUsuario(Conta_Login objUsuario) { this.objUsuarioRegras = objUsuario; }
	public SessionFactory getObjSessionFactory() { return this.objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }	
	public Contas_Skype getObjContasSkype() { return objContasSkype; }
	public void setObjContasSkype(Contas_Skype objContasSkype) { this.objContasSkype = objContasSkype; }
	public Set<Contatos_Contas_Skype> getObjListaContatosContaSkype() { return objListaContatosContaSkype; }
	public void setObjListaContatosContaSkype(Set<Contatos_Contas_Skype> objListaContatosContaSkype) { this.objListaContatosContaSkype = objListaContatosContaSkype; }
	
	public void executaCargaMensagens() {
		
		//Identifica o Usuario logado no Sistema
		objUsuarioRegras = carregaUsuario();
				
		if (objUsuarioRegras != null)
			executaETL();
		else
			JOptionPane.showMessageDialog(null, "Atenção falha ao validar o Usuário Logado no Skype !");
		
	}
	
	private void executaETL() {
		
		int ultimoID = 0;
	
		Mensagens_Skype objMensagensRegra = new Mensagens_Skype();
		try {
			
			//Identifica o último ID salvo na base local de backup
			objMensagensRegra.setObjSessionFactory(objSessionFactory);
			ultimoID = objMensagensRegra.retornaUltimoID(objUsuarioRegras.getSigninName());
			
			//Consulta na base do Skype as Mensagens lançadas com ID maior que o retornado acima
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
					
					//Identifica a origem das mensagens de acordo com a estação Cliente
					if (resultSet.getString("author").equals(objUsuarioRegras.getSigninName())) {
					
						objMensagensRegra.setMessage_type("E");
						objMensagensRegra.setAccount_verified(objContasSkype.getAccount_verified());
						objMensagensRegra.setContact_verified("*");			
					
					}
					else {
					
						objMensagensRegra.setMessage_type("R");			
						objMensagensRegra.setContact_verified("N");
						
						//Verifica se o Contato da mensagem recebida está autorizado
						for (Contatos_Contas_Skype index : objListaContatosContaSkype) {
							
							if (index.getAccount_name().equals(resultSet.getString("author"))) {
								objMensagensRegra.setContact_verified(index.getContact_verified());
								break;
							}
							
						}
						objMensagensRegra.setAccount_verified("*");
					
					}
										
					if (! objMensagensRegra.salvaMensagem())
						JOptionPane.showMessageDialog(null, "Falha ao inserir a Mensagem via Listener!");

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
			JOptionPane.showMessageDialog(null, "Exceção ao Importar Dados Skype. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (objMensagensRegra != null)
				objMensagensRegra = null;
		}
			
	}
	
	private Conta_Login carregaUsuario() {
		
		Conta_Login objUser = new Conta_Login();
		
		objUser.setConnectionSQLLite(connectionSQLLite);
		objUser.carregaContaAtiva();
		
		return objUser;
		
	}
	
	public void finalize() {
		
		if (objUsuarioRegras != null)
			objUsuarioRegras = null;
		
	}

}