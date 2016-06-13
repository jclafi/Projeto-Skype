package main;

import hibernate.Hibernate;
import jdbc.SqlLiteConnection;
import modal.Configuracao_Skype;
import etl.*;

class MainSkypeClass {
				
	public static void finalizaAplicacao() {
		
		closeHibernate();
		closeJDBC();
		System.gc();
		System.exit(0);
		
	}
	
	public static boolean ativaSkypeListener() {
		
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
		
		IniciaSkypeListener objSkypeListener = new IniciaSkypeListener();		
		try {
			
			return (objSkypeListener.verificaInstalação());
				
		}
		finally {
			if (objSkypeListener != null)
				objSkypeListener = null;
				
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
	
	public static void main(String[] args) {
		
		if (! verificaInstalacaoSkype())
			finalizaAplicacao();
	
		// Cria a Factory de acesso a Dados
		if (connectHibernate() && connectJDBC()) {
			
			//Verifica se a leitura é via listener ou banco de dados
			if (ativaSkypeListener()) {			
				
				IniciaSkypeListener objSkypeListener = new IniciaSkypeListener();
				
				// Cria o Listener que grava Mensagens enviadas/recebidas
				if (objSkypeListener.connectSkype())
					objSkypeListener.startChatListener();

			}
			else {
	
				//Cria a Thread que gerencia a cargas das mensagens para o Banco de Dados
				DatabaseExtractLoad objDataETL = new DatabaseExtractLoad();
				
				objDataETL.start();
				
			}
			
		}
		else
			finalizaAplicacao();

	}

}