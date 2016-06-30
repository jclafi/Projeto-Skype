package modal;

import java.math.BigInteger;
import java.sql.Timestamp;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Mensagens_Skype {
	
	private long id_geral;
	private long id;
	private String id_sender;
	private String sender_display_name;
	private String content;
	private String chat;
	private String message_type;
	private Timestamp message_date;
	private String ip_adress;
	private String host_name;
	private String account_logged;
	private String contact_verified;
	private String account_verified;	
	private SessionFactory objSessionFactory;
	
	public long getId_geral() { return id_geral; }
	public void setId_geral(long id_geral) { this.id_geral = id_geral; }
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public String getId_sender() { return id_sender; }
	public void setId_sender(String id_sender) { this.id_sender = id_sender; }
	public String getSender_display_name() { return sender_display_name; } 
	public void setSender_display_name(String sender_display_name) { this.sender_display_name = sender_display_name; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public String getChat() { return chat; }
	public void setChat(String chat) { this.chat = chat; }
	public String getMessage_type() {	return message_type; }
	public void setMessage_type(String message_type) { this.message_type = message_type; }
	public Timestamp getMessage_date() { return message_date; }
	public void setMessage_date(Timestamp pmessage_date) { this.message_date = pmessage_date; }	
	public String getIp_adress() { return ip_adress; }
	public void setIp_adress(String ip_adress) { this.ip_adress = ip_adress; }
	public String getHost_name() { return host_name; }
	public void setHost_name(String host_name) { this.host_name = host_name; }	
	public String getAccount_logged() { return account_logged; }
	public void setAccount_logged(String account_logged) { this.account_logged = account_logged; }
	public String getContact_verified() { return contact_verified; }
	public void setContact_verified(String contact_verified) { this.contact_verified = contact_verified; }
	public String getAccount_verified() { return account_verified; }
	public void setAccount_verified(String account_verified) { this.account_verified = account_verified; }
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	
	public long retornaUltimoID(String accountLogged) {
		
		long varId = 0;
		
		final String CUSTOM_SQL = " select coalesce(max(id), 0) as id from mensagens_skype where account_logged = :account ";
				
		//Cria a sessão
		Session session = objSessionFactory.openSession();		
		
		SQLQuery qryTeste = null;
		try {			
		
			qryTeste = session.createSQLQuery(CUSTOM_SQL);
			qryTeste.setParameter("account", accountLogged);	
			
			for (int index = 0; index < qryTeste.list().size();) {
				BigInteger objTemp = (BigInteger) qryTeste.list().get(index);
				varId = objTemp.longValue();				
				break;
			}
		
		}
		catch (Exception ex) {
			Erros_Skype_Static.salvaErroSkype("Exceção ao Retornar último ID Tabela. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (!qryTeste.list().isEmpty()) {
				qryTeste.list().clear();
				qryTeste = null;
			}
		
			if (session != null) {
				if (session.isOpen())
					session.close();
				session = null;				
			}
		}
				
		return varId;

	}
	
	public boolean salvaMensagem() {
		
		boolean bolOk = false;
		
		Mensagens_Skype_Dao objPersistente = new Mensagens_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);
			bolOk = objPersistente.salvaMensagem();
			
		}
		finally {
	    	if (objPersistente != null)
	    		objPersistente = null;			
		}
		
		return bolOk;
	}
	
	public boolean atualizaMensagem() {
		
		boolean bolOk = false;
		
		Mensagens_Skype_Dao objPersistente = new Mensagens_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);
			bolOk = objPersistente.atualizaMensagem();
			
		}
		finally {
	    	if (objPersistente != null)
	    		objPersistente = null;			
		}
		
		return bolOk;		
		
	}
	
	public boolean removeMensagem() {
		
		boolean bolOk = false;
		
		Mensagens_Skype_Dao objPersistente = new Mensagens_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);	
			bolOk = objPersistente.removeMensagem();
			
		}
		finally {
	    	if (objPersistente != null)
	    		objPersistente = null;			
		}
		
		return bolOk;
	}
	
	public boolean equals(Object o) {
		
		//O parâmetro não pode ser nulo
		if (o == null) return false;
		
		//Se não for um objeto da classe Pessoa retorna nulo
		if (! (this.getClass().equals(o.getClass()))) return false;
		
		Mensagens_Skype outra = (Mensagens_Skype) o;
		
		return ( (this.id == outra.getId())) &&
				 (this.id_sender.equals(outra.getId_sender())) &&
				 (this.sender_display_name.equals(outra.getSender_display_name())) &&
				 (this.content.equals(outra.getContent())) &&
				 (this.chat.equals(outra.getChat())) &&
				 (this.message_type.equals(outra.getMessage_type())) &&
				 (this.message_date.equals(outra.getMessage_date())) &&
				 (this.host_name.equals(outra.getHost_name())) &&
				 (this.account_logged.equals(outra.getAccount_logged()) &&
				 (this.ip_adress.equals(outra.getIp_adress())) &&
				 (this.host_name.equals(outra.getHost_name())) &&
				 (this.account_verified.equals(outra.getAccount_verified())) );					
	}	

	public int hashCode() {

		String atributos = (this.id + 
							this.id_sender + 
							this.sender_display_name + 
							this.content + 
							this.chat + 
							this.message_type + 
							this.message_date + 
							this.host_name + 
							this.account_logged + 
							this.ip_adress + 
							this.host_name + 
							this.account_verified);
		
		return atributos.hashCode();
	
	}	
        
}