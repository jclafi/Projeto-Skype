package etl;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import modal.Erros_Skype;
import modal.Mensagens_Skype;

public class EtlDadosServidor {
	
	private SessionFactory objPostgreSQLFactory;
	private SessionFactory objMySQLFactory;
	private String accountName;

	public SessionFactory getObjPostgreSQLFactory() { return this.objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { this.objPostgreSQLFactory = varSessionFactory; };	
	public SessionFactory getObjMySQLFactory() { return objMySQLFactory; }
	public void setObjMySQLFactory(SessionFactory objMySQLFactory) { this.objMySQLFactory = objMySQLFactory; }
	public String getAccountName() { return accountName; }
	public void setAccountName(String accountName) { this.accountName = accountName; }
	
	public void enviaMensagensServidor() {
				
		Mensagens_Skype objMensagensMysQL = new Mensagens_Skype();
		Mensagens_Skype objMensagensPostgreSQL = new Mensagens_Skype();
		try {
						
			//Identifica as conexões com as bases de dados local e remota
			objMensagensMysQL.setObjSessionFactory(this.getObjMySQLFactory());
			objMensagensPostgreSQL.setObjSessionFactory(this.getObjPostgreSQLFactory());
									
			//Identifica o último ID salvo no Servidor MySQL para esta conta do Skype
			int ultimoID = objMensagensMysQL.retornaUltimoID(accountName, true);
					
			//Cria a Session
			Session localSession = this.getObjPostgreSQLFactory().openSession();	
					
			//Valida os Filtros da Consulta SQL
			String whereSQL = "where id > " + ultimoID + " and account_logged = '" + accountName +"' order by id_geral";
					
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
						Erros_Skype.salvaErroSkype("Não foi possível persistir a Mensagem no Servidor: " + objTempMensagensMysQL.getId_geral());							
							
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

			Erros_Skype.salvaErroSkype("Exceção ao Enviar Mensagens ao Servidor. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		
		}
		finally {

			if (objMensagensMysQL != null)
				objMensagensMysQL = null;
			
			if (objMensagensPostgreSQL != null)
				objMensagensPostgreSQL = null;

		}
		
	}
	
	public void atualizaDadosContaContatos() {
		
		
		
	}
	
	public void enviaLogErrosServidor() {
		
		
		
	}

}