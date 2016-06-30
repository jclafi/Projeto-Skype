package etl;

import org.hibernate.SessionFactory;

import controller.DefineEstruturaProjeto;

public class IniciaEtlDadosServidor extends Thread {
	
	//Entre uma carga e outra aguarda 15 minutos
	private final long SLEEP_TIME = 900000;
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

		//Inicia a conexão com delay, espera primeira carga de mensagens Skype-Banco Local
		do {
			
			//Pausa para a nova Tentativa de Conexão
			try {

				IniciaEtlDadosServidor.sleep(SLEEP_TIME);
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Se o objeto já existe e está conectado no servidor aborda a nova conexão
			if ((getObjMySQLFactory() != null) && 
				(! getObjMySQLFactory().isClosed()))
				break;
			
		} while (! objEstruturaSkype.connectMySQLHibernate());
		
		//Define a conexão a classe Cliente/Servidor
		if (getObjMySQLFactory() == null)
			setObjMySQLFactory(objEstruturaSkype.getObjMySQLFactory().getFactory());		
		
	}
	
	/*
	 * Método recursivo que gerencia o E.T.L das mensagens
	 */
	private void criaObjetoClienteServidor() {
		
		connectServer();
		
		EtlDadosServidor objCargaMensagensServidor = new EtlDadosServidor();
		try {
			
			objCargaMensagensServidor.setObjMySQLFactory(this.getObjMySQLFactory());
			objCargaMensagensServidor.setObjPostgreSQLFactory(this.getObjPostgreSQLFactory());
			objCargaMensagensServidor.setObjConfiguracao(objEstruturaSkype.getObjConfiguracao());
			
			//Verifica e atualzia a Conta Servidor e Máquina Local
			objCargaMensagensServidor.enviaDadosConta();
			
			//Verifica e atualzia os Contatos no Servidor e Máquina Local
			objCargaMensagensServidor.enviaDadosContatos();
			
			//Envia as mensagens pendentes para o Servidor
			objCargaMensagensServidor.enviaMensagensServidor();
			
			//Envia os Erros gerados na estação cliente para o Servidor
			objCargaMensagensServidor.enviaLogErroServidor();
						
		}
		finally {
			
			if (objCargaMensagensServidor != null)
				objCargaMensagensServidor = null;
			
		}
		
		System.gc();
		
		//Método recursivo para carga de Mensagens
		criaObjetoClienteServidor();
		
	}

}