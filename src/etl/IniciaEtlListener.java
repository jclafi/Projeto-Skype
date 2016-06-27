package etl;

import modal.Erros_Skype;

import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;
import com.skype.Skype;
import jdbc.SqlLiteConnection;
import modal.Contas_Skype;
import modal.Contatos_Contas_Skype;
import modal.Conta_Login;

public class IniciaEtlListener extends Thread {
	
	//Entre uma carga e outra aguarda 5 minutos
	private final long SLEEP_TIME = 300000;
	private String accountName;
	private SessionFactory objSessionFactory;
	private SqlLiteConnection connectionSQLLite;
	private Conta_Login objLoginRegras;
	private Contas_Skype objContasSkype;
	private Contatos_Contas_Skype objContatosContasSkype;
	
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }	
	public Conta_Login getUsuarioRegras() { return objLoginRegras; }
	public void setUsuarioRegras(Conta_Login objSkypeUser) { this.objLoginRegras = objSkypeUser; }
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
				
		//Carrega o Usuário do Skype que logou no SQLLite
		objLoginRegras = carregaUsuario();
		
		//Valida se o Operador conectou no Skype e Cria o Listener que grava Mensagens enviadas/recebidas
		if ((objLoginRegras != null) && (connectSkype())) {

			//Cria objetos para verificação dos dados da Conta e Contatos
			criaContaContatos();

			//Inicia o Listener direto via API Skype
			startChatListener();

		}
	}	
	
	private void criaContaContatos() {
		
		objContasSkype = new Contas_Skype();
		objContasSkype.setObjSessionFactory(objSessionFactory);
		objContasSkype.carregaConta(accountName);

		objContatosContasSkype = new Contatos_Contas_Skype(); 
		objContatosContasSkype.setObjSessionFactory(objSessionFactory);
		objContatosContasSkype.carregaContatosConta(objContasSkype.getId_geral());
	
	}
	
	private Conta_Login carregaUsuario() {
		
		Conta_Login objUser = new Conta_Login();
		
		objUser.setConnectionSQLLite(connectionSQLLite);
		objUser.getUsuarioLogado();
		
		return objUser;
		
	}
	
	public boolean verificaInstalação() {

		if (! Skype.isInstalled()) {
		
			JOptionPane.showMessageDialog(null, "Atenção instalação do Skype não Identificada !");
			return false;
		
		}
		
		return true;
		
	}
		
	private boolean connectSkype() {
		
		boolean connected = false;
		
		do {
			try {
				
				if (Skype.isRunning())
					connected = true;
				else
					Skype.addApplication("javaw.exe");
				
			} catch (Exception ex) {

				Erros_Skype.salvaErroSkype("Permissão de Acesso via Listener Skype solicitada !");
				ex.printStackTrace();
		
			}
			
			//Se não conectou aguarda para a próxima tentativa
			if (! connected) {

				//Pausa para a nova carga de Mensagens
				try {

					IniciaEtlListener.sleep(SLEEP_TIME);
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		while ( ! connected);
		
		return true;
	}

	@SuppressWarnings("deprecation")
	private void startChatListener() {

		EtlListener skypeListener = new EtlListener();;
		try {
			
			//Define os dados da Conta e Contatos do Skype
			skypeListener.setObjContasSkype(objContasSkype);
			skypeListener.setObjListaContatosContaSkype(objContatosContasSkype.getObjListaContatosContaSkype());			
			
			//Objeto com dados do Usuário Skype
			skypeListener.setUsuarioRegras(objLoginRegras);
			
			//Objeto Session Factory Hibernate
			skypeListener.setObjSessionFactory(objSessionFactory);
			
			//Adiciona o Listener para Mensagens On The Fly
			Skype.addChatMessageListener(skypeListener);
			
			// Para não permitir desabilitar o programa
			Skype.setDeamon(false);
			
		} catch (Exception ex) {
			Erros_Skype.salvaErroSkype("Exceção ao definir o Listener Skype: " + ex.getMessage());
			ex.printStackTrace();
		}
		
	}
	
	public void finalize() {
		
		if (objLoginRegras != null)
			objLoginRegras = null;
		if (objContasSkype != null)
			objContasSkype = null;
		if (objContatosContasSkype != null)
			objContatosContasSkype = null;
		
	}

}