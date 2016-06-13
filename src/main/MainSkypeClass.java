package main;

import hibernate.Hibernate;
import jdbc.SqlLiteConnection;
import listener.SkypeListenerClass;
import dao.Configuracao_Skype;

class MainSkypeClass {
				
	public static void finalizaAplicacao() {
		
		closeHibernate();
		closeJDBC();
		System.gc();
		System.exit(0);
		
	}
	
	public static boolean ativaListener() {
		
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
	
	public static boolean verificaInstalação() {
		
		SkypeListenerClass objSkypeListener = new SkypeListenerClass();		
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
		
		if (! verificaInstalação())
			finalizaAplicacao();
	
		// Cria a Factory de acesso a Dados
		if (connectHibernate() && connectJDBC()) {
			
			//Verifica se a leitura é via listener ou banco de dados
			if (ativaListener()) {			
				
				SkypeListenerClass objSkypeListener = new SkypeListenerClass();
				
				//Valida a conexção do listener no Skype via confirmação do Usuário
				if (objSkypeListener.connectSkype()) {
				
					// Cria o Listener que grava Mensagens enviadas/recebidas
					objSkypeListener.startChatListener();
					
				}
			}
			else {
				
				
				
			}
			
		}
		else
			finalizaAplicacao();

	}

}