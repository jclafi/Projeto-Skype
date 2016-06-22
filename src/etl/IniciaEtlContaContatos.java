package etl;

import org.hibernate.SessionFactory;
import main.CriaEstruturaSkype;

public class IniciaEtlContaContatos  extends Thread {
	
	//Entre uma carga e outra aguarda 30 minutos
	private final long SLEEP_TIME = 1800000;	
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
		
		criaObjetoContaContatos();
		
	}
	
	/*
	 * M�todo recursivo que gerencia o E.T.L das mensagens
	 */
	private void criaObjetoContaContatos() {
		
		settServerConnection();
		
//		EtlMensagensServidor objCargaMensagensServidor = new EtlMensagensServidor();
//		try {
//			
//			objCargaMensagensServidor.setObjMySQLFactory(this.getObjMySQLFactory());
//			objCargaMensagensServidor.setObjPostgreSQLFactory(this.getObjPostgreSQLFactory());
//			objCargaMensagensServidor.executaEnvioServidor();
//			
//		}
//		finally {
//			
//			if (objCargaMensagensServidor != null)
//				objCargaMensagensServidor = null;
//			
//		}
		
		System.gc();
		
		//M�todo recursivo para carga de Mensagens
		criaObjetoContaContatos();
		
	}
	
	private void settServerConnection() {

		//Inicia o teste da conex�o com delay, utiliza a conex�o criada pela Thread "IniciaEtlServidor"
		do {
			
			//Pausa para a nova Tentativa de Conex�o
			try {

				IniciaEtlContaContatos.sleep(SLEEP_TIME);
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Se o objeto j� existe e est� conectado no servidor aborda a nova conex�o
			if ((getObjMySQLFactory() != null) && 
				(! getObjMySQLFactory().isClosed()))
				break;
			
		} while ((objEstruturaSkype.getObjMySQLFactory() == null) || 
				 (objEstruturaSkype.getObjMySQLFactory().getFactory().isClosed()));
		
		//Define a conex�o a classe Cliente/Servidor
		if (getObjMySQLFactory() == null)
			setObjMySQLFactory(objEstruturaSkype.getObjMySQLFactory().getFactory());		
		
	}
	
}