package main;

import hibernate.Hibernate;
import jdbc.SqlLiteConnection;
import modal.Configuracao_Skype;
import etl.*;

class MainSkypeClass {
	
	public static void main(String[] args) {
		
		if (! verificaInstalacaoSkype())
			finalizaAplicacao();
	
		// Cria a Factory de acesso a Dados
		if (connectHibernate() && connectJDBC()) {
			
			//Verifica se a leitura é via listener ou banco de dados
			if (verificaTipoImportacao()) {			
				
				//Cria a Thread que gerencia a as mensagens via Listener para o Banco de Dados
				IniciaSkypeListener objSkypeListener = new IniciaSkypeListener();
				objSkypeListener.start();

			}
			else {
				
				//Cria a Thread que gerencia a cargas das mensagens para o Banco de Dados
				IniciaSalvaMensagens objSalvaMensagens = new IniciaSalvaMensagens();
				objSalvaMensagens.start();							
			
			}
			
		}
		else
			finalizaAplicacao();

	}
				
	public static void finalizaAplicacao() {
		
		closeHibernate();
		closeJDBC();
		System.gc();
		System.exit(0);
		
	}
	
	public static boolean verificaTipoImportacao() {
		
		boolean objTemp = false;
		
		Configuracao_Skype objConfiguracao = new Configuracao_Skype(Configuracao_Skype.CODIGO_CONFIGURACAO);
		try {
			
			objTemp = objConfiguracao.getSkypeListener().equals("S");
			
		}
		finally {
			if (objConfiguracao != null)
				objConfiguracao = null;
		}
		
		return objTemp;		
	}
	
	public static boolean verificaInstalacaoSkype() {
		
		IniciaSkypeListener objVerificaInstalacao = new IniciaSkypeListener();		
		try {
			
			return (objVerificaInstalacao.verificaInstalação());
				
		}
		finally {
			if (objVerificaInstalacao != null)
				objVerificaInstalacao = null;
				
		}
		
	}
	
	public static boolean connectHibernate() {

		return Hibernate.createFactory();

	}
	
	public static boolean connectJDBC() {
		
		return SqlLiteConnection.getSQLLiteConnection();
		
	}
	
	public static void closeJDBC() {
		
		SqlLiteConnection.closeSQLLiteConnection();
		
	}
	
	public static void closeHibernate() {

		Hibernate.closeFactory();

	}

}