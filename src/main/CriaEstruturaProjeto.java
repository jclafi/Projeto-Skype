package main;

import etl.IniciaEtlListener;
import hibernate.HibernatePostgreSQL;
import hibernate.HibernateMySQL;
import jdbc.SqlLiteConnection;
import modal.Configuracao_Skype;

public class CriaEstruturaSkype {

	private HibernatePostgreSQL objPostgreSQLFactory;	
	private HibernateMySQL objMySQLFactory;
	private SqlLiteConnection connectionSQLLite;
	private Configuracao_Skype objConfiguracao;
	
	public HibernatePostgreSQL getObjPostgreSQLFactory() { return objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(HibernatePostgreSQL objPostgreSQLFactory) { this.objPostgreSQLFactory = objPostgreSQLFactory; }
	public Configuracao_Skype getObjConfiguracao() { return objConfiguracao; }
	public void setObjConfiguracao(Configuracao_Skype objConfiguracao) { this.objConfiguracao = objConfiguracao; }
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }
	public HibernateMySQL getObjMySQLFactory() { return objMySQLFactory; }
	public void setObjMySQLFactory(HibernateMySQL objMySQLFactory) { this.objMySQLFactory = objMySQLFactory; }
	
	public boolean criaObjetoConfiguracao() {
		
		objConfiguracao = new Configuracao_Skype();
		objConfiguracao.setObjSessionFactory(objPostgreSQLFactory.getFactory());
		objConfiguracao.setConfiguracao(objConfiguracao.getCODIGO_CONFIGURACAO());
		
		return (objConfiguracao != null);
	}
	
	public boolean verificaTipoImportacao() {
		
		boolean objTemp = false;
		
		objTemp = objConfiguracao.getSkypeListener().equals("S");	
		
		return objTemp;		
	}
	
	public boolean verificaInstalacaoSkype() {
		
		IniciaEtlListener objVerificaInstalacao = new IniciaEtlListener();		
		try {
			
			return (objVerificaInstalacao.verificaInstalação());
				
		}
		finally {
			if (objVerificaInstalacao != null)
				objVerificaInstalacao = null;
				
		}
		
	}
	
	public boolean connectPostgreSQLHibernate() {

		objPostgreSQLFactory = new HibernatePostgreSQL();
		
		return objPostgreSQLFactory.createFactory();
		
	}
	
	public boolean connectMySQLHibernate() {

		objMySQLFactory = new HibernateMySQL();
		
		objMySQLFactory.setObjConfiguracao(objConfiguracao);
		return objMySQLFactory.createFactory();
		
	}
	
	public boolean connectSQLLiteJDBC() {
		
		connectionSQLLite = new SqlLiteConnection();
		
		connectionSQLLite.setObjConfiguracao(objConfiguracao);
		return connectionSQLLite.getSQLLiteConnection();
		
	}
	
	public void closeSQLLiteJDBC() {

		
		connectionSQLLite.closeSQLLiteConnection();
		
	}
	
	public void closePostgreSQLHibernate() {

		objPostgreSQLFactory.closeFactory();

	}
	
	public void finalizaAplicacao() {
		
		closePostgreSQLHibernate();
		closeSQLLiteJDBC();
		System.gc();
		System.exit(0);
		
	}
	
}