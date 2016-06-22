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
	 * Método recursivo que gerencia o E.T.L das mensagens
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
		
		//Método recursivo para carga de Mensagens
		criaObjetoContaContatos();
		
	}
	
	private void settServerConnection() {

		//Inicia o teste da conexão com delay, utiliza a conexão criada pela Thread "IniciaEtlServidor"
		do {
			
			//Pausa para a nova Tentativa de Conexão
			try {

				IniciaEtlContaContatos.sleep(SLEEP_TIME);
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Se o objeto já existe e está conectado no servidor aborda a nova conexão
			if ((getObjMySQLFactory() != null) && 
				(! getObjMySQLFactory().isClosed()))
				break;
			
		} while ((objEstruturaSkype.getObjMySQLFactory() == null) || 
				 (objEstruturaSkype.getObjMySQLFactory().getFactory().isClosed()));
		
		//Define a conexão a classe Cliente/Servidor
		if (getObjMySQLFactory() == null)
			setObjMySQLFactory(objEstruturaSkype.getObjMySQLFactory().getFactory());		
		
	}
	
}