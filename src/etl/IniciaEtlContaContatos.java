package etl;

import org.hibernate.SessionFactory;

import jdbc.SqlLiteConnection;
import modal.Configuracao_Skype;

public class IniciaEtlContaContatos  extends Thread {
	
	//Entre uma carga e outra aguarda 30 minutos
	private final long SLEEP_TIME = 1800000;	
	private SessionFactory objPostgreSQLFactory;
	private Configuracao_Skype objConfiguracao;
	private SqlLiteConnection connectionSQLLite;
	
	public SessionFactory getObjPostgreSQLFactory() { return this.objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { this.objPostgreSQLFactory = varSessionFactory; };	
	public Configuracao_Skype getObjConfiguracao() { return objConfiguracao; }
	public void setObjConfiguracao(Configuracao_Skype objConfiguracao) { this.objConfiguracao = objConfiguracao; }
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		criaObjetoContaContatos();
		
	}
	
	/*
	 * Método recursivo que gerencia o E.T.L dos contatos
	 */
	private void criaObjetoContaContatos() {
				
		EtlContaContatos objContaContato = new EtlContaContatos();
		try {
			
			objContaContato.setSkypeAccountConfig(getObjConfiguracao().getSkypeAccount());
			objContaContato.setObjPostgreSQLFactory(this.getObjPostgreSQLFactory());
			objContaContato.setConnectionSQLLite(this.getConnectionSQLLite());			
			
			//Valida se o contato já foi inserido na base e se sim, é o mesmo definido nas configurações
			objContaContato.validaContaContatos();

		}
		finally {
			
			if (objContaContato != null)
				objContaContato = null;
			
		}
		
		System.gc();
		

		//Aguarda um período para nova consulta de atualização de dados
		try {

			IniciaEtlContaContatos.sleep(SLEEP_TIME);
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Método recursivo para carga de Mensagens
		criaObjetoContaContatos();
		
	}
		
}