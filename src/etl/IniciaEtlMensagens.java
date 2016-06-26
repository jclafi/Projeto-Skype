package etl;

import org.hibernate.SessionFactory;
import jdbc.SqlLiteConnection;
import modal.Contas_Skype;
import modal.Contatos_Contas_Skype;

public class IniciaEtlMensagens extends Thread {
	
	//Entre uma carga e outra aguarda 1 minuto
	private final long SLEEP_TIME = 60000;
	private String accountName;	
	private SessionFactory objSessionFactory;
	private SqlLiteConnection connectionSQLLite;	
	private Contas_Skype objContasSkype;
	private Contatos_Contas_Skype objContatosContasSkype;

	public SessionFactory getObjSessionFactory() { return this.objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }	
	public String getAccountName() { return accountName; }
	public void setAccountName(String accountName) { this.accountName = accountName; }
	public Contas_Skype getObjContasSkype() { return objContasSkype; }
	public void setObjContasSkype(Contas_Skype objContasSkype) { this.objContasSkype = objContasSkype; }
	public Contatos_Contas_Skype getObjContatosContasSkype() { return objContatosContasSkype; }
	public void setObjContatosContasSkype(Contatos_Contas_Skype objContatosContasSkype) { this.objContatosContasSkype = objContatosContasSkype; }
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		//Cria objetos para verificação dos dados da Conta e Contatos
		criaContaContatos();
		
		//Cria o objeto para a carga das mensagens do SQL Lite para Banco Local
		criaObjetoEtl();
		
	}
	
	private void criaContaContatos() {
		
		objContasSkype = new Contas_Skype();
		objContasSkype.setObjSessionFactory(objSessionFactory);
		objContasSkype.carregaConta(accountName);

		objContatosContasSkype = new Contatos_Contas_Skype(); 
		objContatosContasSkype.setObjSessionFactory(objSessionFactory);
		objContatosContasSkype.carregaContatosConta(objContasSkype.getId_geral());
	
	}
	
	/*
	 * Método recursivo que gerencia o E.T.L das mensagens
	 */
	private void criaObjetoEtl() {
				
		EtlMensagens objMensagens = new EtlMensagens();
		try {
			
			objMensagens.setConnectionSQLLite(connectionSQLLite);
			objMensagens.setObjSessionFactory(objSessionFactory);
			objMensagens.setObjContasSkype(objContasSkype);
			objMensagens.setObjListaContatosContaSkype(objContatosContasSkype.getObjListaContatosContaSkype());
			objMensagens.executaCargaMensagens();	
			
		}		
		finally {
		
			if (objMensagens != null)
				objMensagens = null;			
		
		}
		
		System.gc();
		
		//Pausa para a nova carga de Mensagens
		try {

			IniciaEtlMensagens.sleep(SLEEP_TIME);
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Método recursivo para carga de Mensagens
		criaObjetoEtl();

	}
	
}