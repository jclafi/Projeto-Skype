package etl;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import modal.Configuracao_Skype;
import modal.Contas_Skype;
import modal.Contatos_Contas_Skype;
import modal.Erros_Skype;
import modal.Erros_Skype_Static;
import modal.Mensagens_Skype;

public class EtlDadosServidor {
	
	private SessionFactory objPostgreSQLFactory;
	private SessionFactory objMySQLFactory;
	private Configuracao_Skype objConfiguracao;
	private Contas_Skype objContaLocal;

	public SessionFactory getObjPostgreSQLFactory() { return this.objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { this.objPostgreSQLFactory = varSessionFactory; };	
	public SessionFactory getObjMySQLFactory() { return objMySQLFactory; }
	public void setObjMySQLFactory(SessionFactory objMySQLFactory) { this.objMySQLFactory = objMySQLFactory; }
	public Configuracao_Skype getObjConfiguracao() { return this.objConfiguracao; }
	public void setObjConfiguracao(Configuracao_Skype pobjConfiguracao) { this.objConfiguracao = pobjConfiguracao; }
	public Contas_Skype getObjConta() { return objContaLocal; }
	public void setObjConta(Contas_Skype objConta) { this.objContaLocal = objConta; }
	
	public void enviaMensagensServidor() {
				
		Mensagens_Skype objMensagensMySQL = new Mensagens_Skype();
		Mensagens_Skype objMensagensPostgreSQL = new Mensagens_Skype();
		try {
						
			//Identifica as conexões com as bases de dados local e remota
			objMensagensMySQL.setObjSessionFactory(this.getObjMySQLFactory());
			objMensagensPostgreSQL.setObjSessionFactory(this.getObjPostgreSQLFactory());
									
			//Identifica o último ID salvo no Servidor MySQL para esta conta do Skype
			long ultimoID = objMensagensMySQL.retornaUltimoID(objConfiguracao.getSkypeAccount());
					
			//Cria a Session
			Session localSession = this.getObjPostgreSQLFactory().openSession();	
					
			//Valida os Filtros da Consulta SQL
			String whereSQL = "where id > " + ultimoID + " and account_logged = '" + objConfiguracao.getSkypeAccount() +"' order by id_geral";
					
			//Reliza uma consulta das mensagens pendentes de envio da base Local para Servidor
			@SuppressWarnings("unchecked")
			List<Mensagens_Skype> qryMensagens = localSession.createQuery("FROM Mensagens_Skype " + whereSQL).list();
			try {
						
				//Pega os objetos de Mensagens da base local e os insere no Servidor MysQL
				Iterator<Mensagens_Skype> iterator = qryMensagens.iterator();
				while (iterator.hasNext()) {
							
					//Pega o objMensagens_Skype da lista e o persiste no Servidor
					Mensagens_Skype objTempMensagensMysQL = (Mensagens_Skype) iterator.next();
					objTempMensagensMysQL.setObjSessionFactory(this.getObjMySQLFactory());
							
					//Persiste a Mensagem no Servidor
					if (! objTempMensagensMysQL.salvaMensagem())
						Erros_Skype_Static.salvaErroSkype("Não foi possível persistir a Mensagem no Servidor: " + objTempMensagensMysQL.getId_geral());							
							
					}

				}
			finally {
				
				if (localSession != null) {
					if (localSession.isConnected())
						localSession.close();
					localSession = null;
				}
						
				if (qryMensagens != null) {
					if (qryMensagens.size() > 0)
						qryMensagens.clear();
					qryMensagens = null;
				}
						
			}
		
		}
		catch (Exception ex) {

			Erros_Skype_Static.salvaErroSkype("Exception ao Enviar Mensagens ao Servidor. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		
		}
		finally {

			if (objMensagensMySQL != null)
				objMensagensMySQL = null;
			
			if (objMensagensPostgreSQL != null)
				objMensagensPostgreSQL = null;

		}
		
	}
	
	public void enviaDadosConta() {
		
		//Pesquisa a Conta Local do Skype
		objContaLocal = new Contas_Skype();
		objContaLocal.setObjSessionFactory(objPostgreSQLFactory);
		
		//Se não carregar a conta cria log de erro e limpa objeto
		if (! objContaLocal.carregaConta(objConfiguracao.getSkypeAccount())) {
			Erros_Skype_Static.salvaErroSkype("Não foi possível enviar Conta Servidor !");
			objContaLocal = null;			
		}
		else
		{
			
			//Verifica no servidor se a conta existe, caso não insere, caso sim atualiza
			Contas_Skype objContaServidor = new Contas_Skype();
			try {
				objContaServidor.setObjSessionFactory(objMySQLFactory);
				
				//Se não carrega a conta insere uma nova 
				if (! objContaServidor.carregaConta(objConfiguracao.getSkypeAccount())) {
					
					objContaServidor.setAccount_name(objContaLocal.getAccount_name());
					objContaServidor.setAccount_verified(objContaLocal.getAccount_verified());
					objContaServidor.setDisplay_name(objContaLocal.getDisplay_name());
					objContaServidor.setHost_name(objContaLocal.getHost_name());
					objContaServidor.setIp_adress(objContaLocal.getIp_adress());
					
					if (! objContaServidor.salvaConta()) {
						
						Erros_Skype_Static.salvaErroSkype("Não foi possível Salvar a Conta do Skype no Server !");
						objContaLocal = null;		
						return;
						
					}

				}
				else {
					
					//Verifica se o flag da Conta do Servidor foi alterado
					if (! objContaServidor.getAccount_verified().equals(objContaLocal.getAccount_verified())) {
					
						objContaLocal.setAccount_verified(objContaServidor.getAccount_verified());
						if (! objContaLocal.salvaConta())
							Erros_Skype_Static.salvaErroSkype("Não foi possível Atualizar o flag de Conta verificada !");
						
					}						
					
				}
				
			}
			finally {
				
				if (objContaServidor != null)
					objContaServidor = null;
				
			}			
			
		}	
		
	}
	
	public void enviaDadosContatos() {
		
		//Verifica se a Conta Local foi definida, caso não aborta
		if (objContaLocal == null) {
			
			Erros_Skype_Static.salvaErroSkype("Não foi possível atualizar os Contatos no Server. Conta Local não definida !");
			return;
			
		}
		
		//Identifica se deve realizar a carga inicial para o servidor
		boolean cargaInicialServidor = true;
		boolean identificouContatoServidor = false;
		
		//Objeto Lista de Contatos da Base Local
		Contatos_Contas_Skype objContatos_Local = new Contatos_Contas_Skype();
		objContatos_Local.setObjSessionFactory(objPostgreSQLFactory);
		
		if (! objContatos_Local.carregaContatosConta(objContaLocal.getId_geral())) {
			
			Erros_Skype_Static.salvaErroSkype("Não foi possível consultar os Contatos da Conta. !");
			return;			
		
		}
		
		//Cria a Session do Servidor
		Session localSession = this.getObjMySQLFactory().openSession();	
				
		//Valida os Filtros da Consulta SQL
		String whereSQL = "where id_conta_skype = (select id_geral from Contas_Skype where account_name = '" + objConfiguracao.getSkypeAccount()+ "')";		
		
		try {
			//Realiza uma consulta dos contatos e valida o flag com os dados do Servidor
			@SuppressWarnings("unchecked")
			List<Contatos_Contas_Skype> qryContatosServidor = localSession.createQuery("FROM Contatos_Contas_Skype " + whereSQL).list();
			
			//Pega os objetos de Contatos e verifica se insere ou atualiza
			Iterator<Contatos_Contas_Skype> iterator = qryContatosServidor.iterator();
			while (iterator.hasNext()) {
				
				//Flags de Controle
				cargaInicialServidor = false;
				identificouContatoServidor = false;
				
				//Identifica que já existem Contatos para esta Conta no Servidor, se sim 
				//verifica flag Contato Certificado, caso não cadastra o Contato
				Contatos_Contas_Skype objContatosServidor = (Contatos_Contas_Skype) iterator.next();
				
				//Percorre os contatos locais e valida o flag de autorizado
				for (Contatos_Contas_Skype objContatosLocal : objContatos_Local.getObjListaContatosContaSkype()) {
										
					//Localiza o Contato
					if (objContatosServidor.getAccount_name().equals(objContatosLocal.getAccount_name())) {

						identificouContatoServidor = true;
						
						//Testa o flag autorizado da base cliente e servisdor
						if (! objContatosServidor.getContact_verified().equals(objContatosLocal.getContact_verified())) {
							
							//Atualiza o flag na base cliente
							objContatos_Local.setObjSessionFactory(objPostgreSQLFactory);
							objContatos_Local.setContact_verified(objContatosServidor.getContact_verified());
							
							if (! objContatos_Local.salvaContatosConta())
								Erros_Skype_Static.salvaErroSkype("Erro ao atualizar o Flag de Contato Verificado na base cliente.");
							
						}
													
						break;
						
					}
											
				}
				
				//Insere o Contato noServidor
				if (! identificouContatoServidor) {
					
					objContatos_Local.setObjSessionFactory(objMySQLFactory);
					
					if (! objContatos_Local.salvaContatosConta())
						Erros_Skype_Static.salvaErroSkype("Erro ao atualizar o Flag de Contato Verificado na base Servidor.");						
					
				}
						
			}
			
			//Se não foi identificada nenhuma conta no Servidor realiza a carga inicial
			if (cargaInicialServidor) {
				
				for (Contatos_Contas_Skype listaContatosLocal : objContatos_Local.getObjListaContatosContaSkype()) {
					
					listaContatosLocal.setObjSessionFactory(objMySQLFactory);
					if (! listaContatosLocal.salvaContatosConta())
						Erros_Skype_Static.salvaErroSkype("Não foi possível persistir o Contato no Servidor: " + listaContatosLocal.getAccount_name()); 
					
				}
				
			}

		}
		catch (Exception ex) {
			
			Erros_Skype_Static.salvaErroSkype("Exception ao consultar os Contatos da Conta Skype. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
			
		}
		finally {
			
			if (localSession != null) {
				if (localSession.isConnected())
					localSession.close();
				localSession = null;
			}
			
			if (objContatos_Local != null) {
				if (objContatos_Local.getObjListaContatosContaSkype() != null)
					objContatos_Local.getObjListaContatosContaSkype().clear();
				objContatos_Local = null;
			}
					
		}
		
	}
		
	public void enviaLogErroServidor() {
		
		try {
			
			//Objeto Erros Skype
			Erros_Skype objErroMySQL = new Erros_Skype();
			objErroMySQL.setObjSessionFactory(objMySQLFactory);
			
			//Identifica o último ID salvo no Servidor MySQL para esta conta do Skype
			long ultimoID = objErroMySQL.retornaUltimoID(objConfiguracao.getSkypeAccount());
			
			//Cria a Session
			Session localSession = this.getObjPostgreSQLFactory().openSession();	
					
			//Valida os Filtros da Consulta SQL
			String whereSQL = "where id > " + ultimoID + " and account_name = '" + objConfiguracao.getSkypeAccount() +"' order by id_geral";
					
			//Reliza uma consulta das erros pendentes de envio da base Local para Servidor
			@SuppressWarnings("unchecked")
			List<Erros_Skype> qryErros = localSession.createQuery("FROM Erros_Skype " + whereSQL).list();
			try {
						
				//Pega os objetos de Erro da base local e os insere no Servidor MysQL
				Iterator<Erros_Skype> iterator = qryErros.iterator();
				while (iterator.hasNext()) {
					
					Erros_Skype objTemp = (Erros_Skype) iterator.next();
					objTemp.setObjSessionFactory(objMySQLFactory);
					
					//Persiste o Erro no Servidor
					if (! objTemp.salvaErroSkype())
						Erros_Skype_Static.salvaErroSkype("Não foi possível persistir o Log de Erro no Servidor. ");							
							
					}

				}
			finally {

				if (localSession != null) {
					if (localSession.isConnected())
						localSession.close();
					localSession = null;
				}
							
				if (qryErros != null) {
					if (qryErros.size() > 0)
						qryErros.clear();
					qryErros = null;
				}
				

				if (objErroMySQL != null) {
					objErroMySQL = null;
				}
				
			}
		
		}
		catch (Exception ex) {

			Erros_Skype_Static.salvaErroSkype("Exception ao Enviar Log Erros ao Servidor. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
	
		}
		
	}

}