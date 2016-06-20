package etl;

import org.hibernate.SessionFactory;

import jdbc.SqlLiteConnection;

public class IniciaEtlMensagens extends Thread {
	
	//Entre uma carga e outra aguarda 1 minutos
	private final long SLEEP_TIME = 60000;
	private SessionFactory objSessionFactory;
	private SqlLiteConnection connectionSQLLite;	

	public SessionFactory getObjSessionFactory() { return this.objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }	

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		criaObjetoEtl();
		
	}	
	
	/*
	 * Método recursivo que gerencia o E.T.L das mensagens
	 */
	private void criaObjetoEtl() {
		
		EtlMensagens objMensagens = new EtlMensagens();
		try {
			
			objMensagens.setConnectionSQLLite(connectionSQLLite);
			objMensagens.setObjSessionFactory(objSessionFactory);
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