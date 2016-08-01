package controller;

import etl.IniciaEtlListener;
import etl.IniciaEtlMensagens;
import etl.IniciaEtlDadosServidor;
import etl.IniciaEtlContaContatos;
import modal.Erros_Skype_Static;

class MainSkypeClass {
	
	public static void main(String[] args) {
		
		DefineEstruturaProjeto objEstruturaSkype = new DefineEstruturaProjeto();
		
		if (! objEstruturaSkype.verificaInstalacaoSkype())
			objEstruturaSkype.finalizaAplicacao(true);
	
		// Cria a Factory de acesso a Dados
		if (objEstruturaSkype.connectPostgreSQLHibernate() && 
			objEstruturaSkype.criaObjetoConfiguracao() && 
			objEstruturaSkype.connectSQLLiteJDBC()) {
			
			//Define que o serviço foi iniciado
			objEstruturaSkype.getObjConfiguracao().defineFlagExecucao('E');
			
			//Define a conexão local para o objeto de Log de Erros
			Erros_Skype_Static.setObjSessionFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
			Erros_Skype_Static.setObjConfiguracao(objEstruturaSkype.getObjConfiguracao());
						
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
				
				//Cria a Thread que gerencia a as mensagens via Listener para o Banco de Dados Local
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
		
			//Cria a Thread que envia as Mensagens e Contatos do Banco Local para Servidor
			IniciaEtlDadosServidor objIniciaCargaServidor = new IniciaEtlDadosServidor();
			objIniciaCargaServidor.setObjPostgreSQLFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
			objIniciaCargaServidor.setObjEstruturaSkype(objEstruturaSkype);
			objIniciaCargaServidor.setAccountName(objEstruturaSkype.getObjConfiguracao().getSkypeAccount());
			objIniciaCargaServidor.start();
			
			//Cria a Thread que envia a Conta e Contatos do Skype para o Banco Local
			IniciaEtlContaContatos objIniciaContaContatos = new IniciaEtlContaContatos();
			objIniciaContaContatos.setObjPostgreSQLFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
			objIniciaContaContatos.setConnectionSQLLite(objEstruturaSkype.getConnectionSQLLite());			
			objIniciaContaContatos.setObjConfiguracao(objEstruturaSkype.getObjConfiguracao());
			objIniciaContaContatos.start();

			//Cria a Thread de Gerenciamento de Sessão do ETL
			GerenciaSessao objGerenciaSessao = new GerenciaSessao();
			objGerenciaSessao.setObjEstruturaSkype(objEstruturaSkype);
			objGerenciaSessao.start();
			
		}
		else
			objEstruturaSkype.finalizaAplicacao(true);

	}
	
}