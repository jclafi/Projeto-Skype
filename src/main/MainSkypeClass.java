package main;

import etl.IniciaEtlListener;
import etl.IniciaEtlMensagens;
import etl.IniciaEtlMensagensServidor;
import etl.IniciaEtlContaContatos;

class MainSkypeClass {
	
	public static void main(String[] args) {
		
		DefineEstruturaProjeto objEstruturaSkype = new DefineEstruturaProjeto();
		
		if (! objEstruturaSkype.verificaInstalacaoSkype())
			objEstruturaSkype.finalizaAplicacao();
	
		// Cria a Factory de acesso a Dados
		if (objEstruturaSkype.connectPostgreSQLHibernate() && 
			objEstruturaSkype.criaObjetoConfiguracao() && 
			objEstruturaSkype.connectSQLLiteJDBC()) {
			
			//Realiza a carga inicial da conta e contatos do Skype via processo local, para criar a estrutura das Threads
			IniciaEtlContaContatos objCargaContaContatosInicial = new IniciaEtlContaContatos();
			try {
				objCargaContaContatosInicial.setObjPostgreSQLFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
				objCargaContaContatosInicial.setConnectionSQLLite(objEstruturaSkype.getConnectionSQLLite());			
				objCargaContaContatosInicial.setObjConfiguracao(objEstruturaSkype.getObjConfiguracao());
				objCargaContaContatosInicial.executaCargaInicial();						
			}
			finally {
				if (objCargaContaContatosInicial != null)
					objCargaContaContatosInicial = null;					
			}
			
			System.gc();
			
			//Verifica se a leitura é via listener ou banco de dados
			if (objEstruturaSkype.verificaTipoImportacao()) {			
				
				//Cria a Thread que gerencia a as mensagens via Listener para o Banco de Dados
				IniciaEtlListener objSkypeListener = new IniciaEtlListener();
				objSkypeListener.setObjSessionFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
				objSkypeListener.setConnectionSQLLite(objEstruturaSkype.getConnectionSQLLite());
				objSkypeListener.setAccountName(objEstruturaSkype.getObjConfiguracao().getSkypeAccount());
				objSkypeListener.start();

			}
			else {
				
				//Cria a Thread que gerencia a cargas das mensagens para o Banco de Dados Local
				IniciaEtlMensagens objSalvaMensagens = new IniciaEtlMensagens();
				objSalvaMensagens.setObjSessionFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
				objSalvaMensagens.setConnectionSQLLite(objEstruturaSkype.getConnectionSQLLite());
				objSalvaMensagens.setAccountName(objEstruturaSkype.getObjConfiguracao().getSkypeAccount());
				objSalvaMensagens.start();							
			
			}
		
			//Cria a Thread que envia as Mensagens do Banco Local para Servidor
			IniciaEtlMensagensServidor IniciaCargaServidor = new IniciaEtlMensagensServidor();
			IniciaCargaServidor.setObjPostgreSQLFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
			IniciaCargaServidor.setObjEstruturaSkype(objEstruturaSkype);
			IniciaCargaServidor.setAccountName(objEstruturaSkype.getObjConfiguracao().getSkypeAccount());
			IniciaCargaServidor.start();
			
			//Cria a Thread de identificação do conta e contatos do Skype
			IniciaEtlContaContatos IniciaContaContatos = new IniciaEtlContaContatos();
			IniciaContaContatos.setObjPostgreSQLFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
			IniciaContaContatos.setConnectionSQLLite(objEstruturaSkype.getConnectionSQLLite());			
			IniciaContaContatos.setObjConfiguracao(objEstruturaSkype.getObjConfiguracao());
			IniciaContaContatos.start();
						
		}
		else
			objEstruturaSkype.finalizaAplicacao();

	}
	
}