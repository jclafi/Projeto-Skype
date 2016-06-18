package main;

import etl.IniciaSkypeListener;
import etl.IniciaEtlMensagens;

class MainSkypeClass {
	
	public static void main(String[] args) {
		
		CriaEstruturaSkype objEstruturaSkype = new CriaEstruturaSkype();
		
		if (! objEstruturaSkype.verificaInstalacaoSkype())
			objEstruturaSkype.finalizaAplicacao();
	
		// Cria a Factory de acesso a Dados
		if (objEstruturaSkype.connectPostgreSQLHibernate() && 
			objEstruturaSkype.criaObjetoConfiguracao() && 
			objEstruturaSkype.connectSQLLiteJDBC()) {
						
			//Verifica se a leitura é via listener ou banco de dados
			if (objEstruturaSkype.verificaTipoImportacao()) {			
				
				//Cria a Thread que gerencia a as mensagens via Listener para o Banco de Dados
				IniciaSkypeListener objSkypeListener = new IniciaSkypeListener();
				objSkypeListener.setObjSessionFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
				objSkypeListener.setConnectionSQLLite(objEstruturaSkype.getConnectionSQLLite());
				objSkypeListener.start();

			}
			else {
				
				//Cria a Thread que gerencia a cargas das mensagens para o Banco de Dados
				IniciaEtlMensagens objSalvaMensagens = new IniciaEtlMensagens();
				objSalvaMensagens.setObjSessionFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
				objSalvaMensagens.setConnectionSQLLite(objEstruturaSkype.getConnectionSQLLite());
				objSalvaMensagens.start();							
			
			}
			
			// Valida conexão com servidor Remoto
			if (objEstruturaSkype.connectMySQLHibernate()) {
				
				
				
			}
			
		}
		else
			objEstruturaSkype.finalizaAplicacao();

	}
	
}