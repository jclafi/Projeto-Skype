package main;

import etl.IniciaEtlListener;
import etl.IniciaEtlMensagens;
import etl.IniciaEtlMensagensServidor;
import etl.IniciaEtlContaContatos;

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
				IniciaEtlListener objSkypeListener = new IniciaEtlListener();
				objSkypeListener.setObjSessionFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
				objSkypeListener.setConnectionSQLLite(objEstruturaSkype.getConnectionSQLLite());
				objSkypeListener.start();

			}
			else {
				
				//Cria a Thread que gerencia a cargas das mensagens para o Banco de Dados Local
				IniciaEtlMensagens objSalvaMensagens = new IniciaEtlMensagens();
				objSalvaMensagens.setObjSessionFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
				objSalvaMensagens.setConnectionSQLLite(objEstruturaSkype.getConnectionSQLLite());
				objSalvaMensagens.start();							
			
			}
		
			//Cria a Thread que envia as Mensagens do Banco Local para Servidor
			IniciaEtlMensagensServidor IniciaCargaServidor = new IniciaEtlMensagensServidor();
			IniciaCargaServidor.setObjPostgreSQLFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
			IniciaCargaServidor.setObjEstruturaSkype(objEstruturaSkype);
			IniciaCargaServidor.start();
			
			//Cria a Thread de identificação do conta e contatos do Skype
			IniciaEtlContaContatos IniciaContaContatos = new IniciaEtlContaContatos();
			IniciaContaContatos.setObjPostgreSQLFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
			IniciaContaContatos.setObjEstruturaSkype(objEstruturaSkype);
			IniciaContaContatos.start();			
			
		}
		else
			objEstruturaSkype.finalizaAplicacao();

	}
	
}