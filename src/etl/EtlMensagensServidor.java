package etl;

import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import modal.Mensagens_Skype;

public class EtlMensagensServidor {
	
	private SessionFactory objPostgreSQLFactory;
	private SessionFactory objMySQLFactory;

	public SessionFactory getObjPostgreSQLFactory() { return this.objPostgreSQLFactory; }
	public void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { this.objPostgreSQLFactory = varSessionFactory; };	
	public SessionFactory getObjMySQLFactory() { return objMySQLFactory; }
	public void setObjMySQLFactory(SessionFactory objMySQLFactory) { this.objMySQLFactory = objMySQLFactory; }

	public void executaEnvioServidor() {
				
		Mensagens_Skype objMensagensMysQL = new Mensagens_Skype();
		Mensagens_Skype objMensagensPostgreSQL = new Mensagens_Skype();
		try {
			
			int ultimoID = 0;
			
			//Identifica as conexões com as bases de dados local e remota
			objMensagensMysQL.setObjSessionFactory(this.getObjMySQLFactory());
			objMensagensPostgreSQL.setObjSessionFactory(this.getObjPostgreSQLFactory());

			//Percorre todas as contas salvas na base local para enviar as Mensagens para o Servidor
			String[] objContasSkype = objMensagensPostgreSQL.retornaListaUsuarios();
			
			if (objContasSkype != null) {
				
				for (String usuarioSkype : objContasSkype) {
					
					//Identifica o último ID salvo no Servidor MySQL para esta conta do Skype
					ultimoID = objMensagensMysQL.retornaUltimoID(usuarioSkype);
					
					//Cria a Session
					Session localSession = this.getObjPostgreSQLFactory().openSession();	
					
					//Valida os Filtros da Consulta SQL
					String whereSQL = "where id > " + ultimoID + " and account_logged = " + usuarioSkype;
					
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
								JOptionPane.showMessageDialog(null, "Não foi possível persistir a Mensagem Id_Geral: " + objTempMensagensMysQL.getId_geral());							
							
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
			
			}
			
		}
		catch (Exception ex) {

			JOptionPane.showMessageDialog(null, "Exceção ao Enviar dados Servidor. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		
		}
		finally {

			if (objMensagensMysQL != null)
				objMensagensMysQL = null;
			
			if (objMensagensPostgreSQL != null)
				objMensagensPostgreSQL = null;

		}
		
	}

}