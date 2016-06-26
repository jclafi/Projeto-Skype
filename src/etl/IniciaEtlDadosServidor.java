package etl;

import org.hibernate.SessionFactory;
import main.DefineEstruturaProjeto;

public class IniciaEtlDadosServidor extends Thread {
	
	//Entre uma carga e outra aguarda 10 minutos
	private final long SLEEP_TIME = 600000;
	private String accountName;
	private SessionFactory objPostgreSQLFactory;
	private SessionFactory objMySQLFactory;
	private DefineEstruturaProjeto objEstruturaSkype;

	public SessionFactory getObjPostgreSQLFactory() { return this.objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { this.objPostgreSQLFactory = varSessionFactory; };	
	public SessionFactory getObjMySQLFactory() { return objMySQLFactory; }
	public void setObjMySQLFactory(SessionFactory objMySQLFactory) { this.objMySQLFactory = objMySQLFactory; }
	public DefineEstruturaProjeto getObjEstruturaSkype() { return objEstruturaSkype; }
	public void setObjEstruturaSkype(DefineEstruturaProjeto objEstruturaSkype) { this.objEstruturaSkype = objEstruturaSkype; }
	public String getAccountName() { return accountName; }
	public void setAccountName(String accountName) { this.accountName = accountName; }
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		criaObjetoClienteServidor();
		
	}
	
	private void connectServer() {

		//Inicia a conex�o com delay, espera primeira carga de mensagens Skype-Banco Local
		do {
			
			//Pausa para a nova Tentativa de Conex�o
			try {

				IniciaEtlDadosServidor.sleep(SLEEP_TIME);
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Se o objeto j� existe e est� conectado no servidor aborda a nova conex�o
			if ((getObjMySQLFactory() != null) && 
				(! getObjMySQLFactory().isClosed()))
				break;
			
		} while (! objEstruturaSkype.connectMySQLHibernate());
		
		//Define a conex�o a classe Cliente/Servidor
		if (getObjMySQLFactory() == null)
			setObjMySQLFactory(objEstruturaSkype.getObjMySQLFactory().getFactory());		
		
	}
	
	/*
	 * M�todo recursivo que gerencia o E.T.L das mensagens
	 */
	private void criaObjetoClienteServidor() {
		
		connectServer();
		
		EtlDadosServidor objCargaMensagensServidor = new EtlDadosServidor();
		try {
			
			objCargaMensagensServidor.setObjMySQLFactory(this.getObjMySQLFactory());
			objCargaMensagensServidor.setObjPostgreSQLFactory(this.getObjPostgreSQLFactory());
			objCargaMensagensServidor.setAccountName(accountName);
			objCargaMensagensServidor.executaEnvioServidor();
			
		}
		finally {
			
			if (objCargaMensagensServidor != null)
				objCargaMensagensServidor = null;
			
		}
		
		System.gc();
		
		//M�todo recursivo para carga de Mensagens
		criaObjetoClienteServidor();
		
	}

}