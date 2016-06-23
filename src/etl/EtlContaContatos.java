package etl;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import org.hibernate.SessionFactory;
import jdbc.SqlLiteConnection;
import modal.Contas_Skype;
import modal.Contatos_Contas_Skype;

public class EtlContaContatos {
	
	private SessionFactory objPostgreSQLFactory;
	private Contas_Skype objContaSkype;
	private Contatos_Contas_Skype objContatosContaSkype;
	private String skypeSigninConfig;
	private SqlLiteConnection connectionSQLLite;

	public SessionFactory getObjPostgreSQLFactory() { return this.objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { this.objPostgreSQLFactory = varSessionFactory; };	
	public Contas_Skype getObjSkypeAccount() { return objContaSkype; }
	public void setObjSkypeAccount(Contas_Skype objSkypeAccount) { this.objContaSkype = objSkypeAccount; }
	public String getSkypeAccountConfig() { return skypeSigninConfig; }
	public void setSkypeAccountConfig(String skypeAccountConfig) { this.skypeSigninConfig = skypeAccountConfig; }
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }	
	public Contas_Skype getObjContaSkype() { return objContaSkype; }
	public void setObjContaSkype(Contas_Skype objContaSkype) { this.objContaSkype = objContaSkype; }
	
	public void validaContaContatos() {		
		
		//Verifica se a conta do usuário já foi definida no Banco Local, caso não define, caso sim atualiza		
		if (! criaAtualizaContaPadrao()) {
			JOptionPane.showMessageDialog(null, "Atenção não foi manipular Conta do Skype !");
			return;
		}
		
		//Verifica se os contatos da conta já foram definidos na base Local, caso não define, caso sim atualiza
		if (! criaAtualizaContatosContaPadrao()) {
			JOptionPane.showMessageDialog(null, "Atenção não foi manipular Contatos do Skype !");
			return;
		}
	
	}
	
	private boolean criaAtualizaContaPadrao() {
		
		objContaSkype = new Contas_Skype();
		objContaSkype.setObjSessionFactory(objPostgreSQLFactory);
		
		//Tenta carregar o usuário do skype caso não encontra cria um novo contato
		if (! objContaSkype.carregaConta(skypeSigninConfig)) { 

			if (! criaContaBaseLocal()) 
				return false;
		}
		else {
		
			//Atualiza a conta
			if (! atualizaConta())
				return false;
		
		}
				
		return (objContaSkype.getId_geral() > 0);
		
	}
	
	private boolean criaAtualizaContatosContaPadrao() {

		objContatosContaSkype = new Contatos_Contas_Skype();
		objContatosContaSkype.setObjSessionFactory(objPostgreSQLFactory);

		//Tenta carregar os  contatos da conta do skype caso não encontra cria um novo contato
		if (! objContatosContaSkype.carregaContatosConta(objContaSkype.getId_geral())) { 

			if (! criaContatosBaseLocal()) 
				return false;
			
		}
		else {
		
			//Atualiza o contato e contas
			if (! atualizaContatosConta())
				return false;
				
		}
				
		return (! objContatosContaSkype.getObjListaContatosContaSkype().isEmpty());
		
	}
	
	private boolean atualizaConta() {
		
		return true;
		
	}
	
	private boolean atualizaContatosConta() {
		
		return true;
		
	}
	
	private boolean criaContaBaseLocal() {
		
		Contas_Skype objPersistente = new Contas_Skype();
		objPersistente.setObjSessionFactory(objPostgreSQLFactory);
		
		String SQL = null;		
		ResultSet resultSet = null;
		PreparedStatement statement = null;			
		try {				
			
			SQL = " select fullname from accounts where signin_name = ? limit 1 ";
			
			statement = connectionSQLLite.getConnection().prepareStatement(SQL);
			statement.setString(1, skypeSigninConfig);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				
				objPersistente.setAccount_name(skypeSigninConfig);
				objPersistente.setAccount_verified("S");
				objPersistente.setDisplay_name(resultSet.getString("fullname"));
				objPersistente.setIp_adress(InetAddress.getLocalHost().getHostAddress());
				objPersistente.setHost_name(InetAddress.getLocalHost().getHostName());
				
				if (! objPersistente.salvaConta())
					return false;
				else
					setObjSkypeAccount(objPersistente);
				
				break;
			}
			
			if (! statement.isClosed())
				statement.close();
			
		}
		catch(Exception ex) {
		
			JOptionPane.showMessageDialog(null, "Atenção erro ao criar a conta Skype. Mensagem: " + ex.getMessage());
			return false;
			
		}
		finally {				
			if (statement != null)
				statement = null;
			if (resultSet != null)
				resultSet = null;
			if (objPersistente != null)
				objPersistente = null;
		}	
		
		return true;
		
	}
	
	private boolean criaContatosBaseLocal() {
		
		Contatos_Contas_Skype objPersistente;
		
		String SQL = null;		
		ResultSet resultSet = null;
		PreparedStatement statement = null;			
		try {				
			
			SQL = " select skypename, fullname from contacts order by id ";
			
			statement = connectionSQLLite.getConnection().prepareStatement(SQL);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				
				objPersistente = new Contatos_Contas_Skype();
				objPersistente.setObjSessionFactory(objPostgreSQLFactory);
				
				objPersistente.setAccount_name(resultSet.getString("skypename"));
				objPersistente.setContact_verified("N");
				objPersistente.setDisplay_name(resultSet.getString("fullname"));
				objPersistente.setId_conta_skype(getObjSkypeAccount().getId_geral());
				
				if (! objPersistente.salvaContatosConta())
					return false;
				else 
					objContatosContaSkype.getObjListaContatosContaSkype().add(objPersistente);
		
			}
			
			if (! statement.isClosed())
				statement.close();
			
		}
		catch(Exception ex) {
		
			JOptionPane.showMessageDialog(null, "Atenção erro ao criar os Contatos da Conta Skype. Mensagem: " + ex.getMessage());
			return false;
			
		}
		finally {				
			if (statement != null)
				statement = null;
			if (resultSet != null)
				resultSet = null;
		}	
		
		return true;
		
	}
	
	public void finalize() {
	
		if (objContaSkype != null)
			objContaSkype = null;
		
		if (objContatosContaSkype != null)
			objContatosContaSkype = null;
	
	}
	
}