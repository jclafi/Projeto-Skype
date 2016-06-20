package etl;

import org.hibernate.SessionFactory;
import main.CriaEstruturaSkype;

public class IniciaEtlServidor extends Thread {
	
	//Entre uma carga e outra aguarda 1 minuto
	private final long SLEEP_TIME = 60000;
	private SessionFactory objPostgreSQLFactory;
	private SessionFactory objMySQLFactory;
	private CriaEstruturaSkype objEstruturaSkype;

	public SessionFactory getObjPostgreSQLFactory() { return this.objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { this.objPostgreSQLFactory = varSessionFactory; };	
	public SessionFactory getObjMySQLFactory() { return objMySQLFactory; }
	public void setObjMySQLFactory(SessionFactory objMySQLFactory) { this.objMySQLFactory = objMySQLFactory; }
	public CriaEstruturaSkype getObjEstruturaSkype() { return objEstruturaSkype; }
	public void setObjEstruturaSkype(CriaEstruturaSkype objEstruturaSkype) { this.objEstruturaSkype = objEstruturaSkype; }
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		criaObjetoClienteServidor();
		
	}	
	
	/*
	 * Método recursivo que gerencia o E.T.L das mensagens
	 */
	private void criaObjetoClienteServidor() {
		
		//Inicia a conexão com delay, espera primeira carga de mensagens Skype-Banco Local
		do {
			
			//Pausa para a nova Tentativa de Conexão
			try {

				IniciaEtlServidor.sleep(SLEEP_TIME);
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} while (! objEstruturaSkype.connectMySQLHibernate());
		
		//Define a conexão a classe Cliente/Servidor
		setObjMySQLFactory(objEstruturaSkype.getObjMySQLFactory().getFactory());		
		
		EtlMensagensServidor objCargaMensagensServidor = new EtlMensagensServidor();
		try {
			
			objCargaMensagensServidor.setObjMySQLFactory(this.getObjMySQLFactory());
			objCargaMensagensServidor.setObjPostgreSQLFactory(this.getObjPostgreSQLFactory());
			objCargaMensagensServidor.executaEnvioServidor();
			
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