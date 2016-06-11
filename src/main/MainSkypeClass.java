package main;

import source.SkypeListenerClass;
import hibernate.Hibernate;
import jdbc.SqlLiteConnection;

class MainSkypeClass {
		
	public static void main(String[] args) {
		
		// Cria a Factory de acesso a Dados
		if (connectHibernate() && connectJDBC()) {
			
			SkypeListenerClass objSkypeListener = new SkypeListenerClass();
			
			//Valida a conexção do listener no Skype via confirmação do Usuário
			if (objSkypeListener.connectSkype()) {
			
				// Cria o Listener que grava Mensagens enviadas/recebidas
				objSkypeListener.startChatListener();
			
				//Identifica Mensagens Recentes e as Insere na Base de Dados
				objSkypeListener.startChatLogBackup();
				
			}
			
		}
		else
		{
			//Se não conectar na base de dados sai do Sistema
			closeHibernate();
			closeJDBC();
			System.gc();
			System.exit(0);
			
		}

	}

	public static void closeHibernate() {

		Hibernate.closeFactory();

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

}