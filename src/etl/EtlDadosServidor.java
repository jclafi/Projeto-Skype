package etl;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import modal.Configuracao_Skype;
import modal.Erros_Skype;
import modal.Erros_Skype_Static;
import modal.Mensagens_Skype;

public class EtlDadosServidor {
	
	private SessionFactory objPostgreSQLFactory;
	private SessionFactory objMySQLFactory;
	private Configuracao_Skype objConfiguracao;

	public SessionFactory getObjPostgreSQLFactory() { return this.objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { this.objPostgreSQLFactory = varSessionFactory; };	
	public SessionFactory getObjMySQLFactory() { return objMySQLFactory; }
	public void setObjMySQLFactory(SessionFactory objMySQLFactory) { this.objMySQLFactory = objMySQLFactory; }
	public Configuracao_Skype getObjConfiguracao() { return this.objConfiguracao; }
	public void setObjConfiguracao(Configuracao_Skype pobjConfiguracao) { this.objConfiguracao = pobjConfiguracao; }	
	
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
			List<Mensagens_Skype> QryMensagens = localSession.createQuery("FROM Mensagens_Skype " + whereSQL).list();
			try {
						
				//Pega os objetos de Mensagens da base local e os insere no Servidor MysQL
				Iterator<Mensagens_Skype> iterator = QryMensagens.iterator();
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
						
				if (QryMensagens != null) {
					if (QryMensagens.size() > 0)
						QryMensagens.clear();
					QryMensagens = null;
				}
						
			}
		
		}
		catch (Exception ex) {

			Erros_Skype_Static.salvaErroSkype("Exceção ao Enviar Mensagens ao Servidor. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		
		}
		finally {

			if (objMensagensMySQL != null)
				objMensagensMySQL = null;
			
			if (objMensagensPostgreSQL != null)
				objMensagensPostgreSQL = null;

		}
		
	}
	
	public void atualizaDadosContaContatos() {
		
		
		
	}
	
	public void enviaLogErrosServidor() {
		
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

			Erros_Skype_Static.salvaErroSkype("Exceção ao Enviar Log Erros ao Servidor. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
	
		}
		
	}

}