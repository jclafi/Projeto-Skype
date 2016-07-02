package etl;

import org.hibernate.SessionFactory;
import jdbc.SqlLiteConnection;
import modal.Configuracao_Skype;
import modal.Conta_Login;
import modal.Erros_Skype_Static;

public class IniciaEtlContaContatos  extends Thread {
	
	//Entre uma carga e outra aguarda 30 minutos
	private final long SLEEP_TIME = 1800000;	
	private SessionFactory objPostgreSQLFactory;
	private Configuracao_Skype objConfiguracao;
	private SqlLiteConnection connectionSQLLite;
	private Conta_Login objLoginRegras;
	
	public SessionFactory getObjPostgreSQLFactory() { return this.objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { this.objPostgreSQLFactory = varSessionFactory; };	
	public Configuracao_Skype getObjConfiguracao() { return objConfiguracao; }
	public void setObjConfiguracao(Configuracao_Skype objConfiguracao) { this.objConfiguracao = objConfiguracao; }
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }
	public Conta_Login getUsuarioRegras() { return objLoginRegras; }
	public void setUsuarioRegras(Conta_Login objSkypeUser) { this.objLoginRegras = objSkypeUser; }
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		//Carrega o Usuário do Skype que logou no SQLLite
		objLoginRegras = carregaUsuario();
		
		//Valida se o Operador conectou no Skype e Cria o cria/atualiza os Contatos e Contas
		if (objLoginRegras != null) 
			criaObjetoContaContatos();
		else
			Erros_Skype_Static.salvaErroSkype("Falha ao indetificar Usuário Conta Contatos ! !");			
		
	}
	
	private Conta_Login carregaUsuario() {
		
		Conta_Login objUser = new Conta_Login();
		
		objUser.setConnectionSQLLite(connectionSQLLite);
		objUser.carregaContaAtiva();
		
		return objUser;
		
	}
	
	public void executaCargaInicial() {
		
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
			Erros_Skype_Static.salvaErroSkype("Interrupted Exception no Timer Conta Contatos !");
			e.printStackTrace();
		}
		
		//Método recursivo para carga de Contatos
		criaObjetoContaContatos();
		
	}
		
}