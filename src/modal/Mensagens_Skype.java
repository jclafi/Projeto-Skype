package modal;

import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Mensagens_Skype {
	
	private long id_geral;
	private int id;
	private String id_sender;
	private String sender_display_name;
	private String content;
	private String chat;
	private String message_type;
	private Date message_date;
	private String ip_adress;
	private String host_name;
	private String account_logged;
	private SessionFactory objSessionFactory;
	
	public long getId_geral() { return id_geral; }
	public void setId_geral(long id_geral) { this.id_geral = id_geral; }
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
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
	public Date getMessage_date() { return message_date; }
	public void setMessage_date(Date message_date) { this.message_date = message_date; }	
	public String getIp_adress() { return ip_adress; }
	public void setIp_adress(String ip_adress) { this.ip_adress = ip_adress; }
	public String getHost_name() { return host_name; }
	public void setHost_name(String host_name) { this.host_name = host_name; }	
	public String getAccount_logged() { return account_logged; }
	public void setAccount_logged(String account_logged) { this.account_logged = account_logged; }
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	

	public int retornaUltimoID() {
		
		int id = 0;
		
		final String CUSTOM_SQL = " select * from mensagens_skype order by id desc limit 1 ";
		
		//Cria a sessão
		Session session = objSessionFactory.openSession();

		SQLQuery qryTeste = null;
		try {			
		
			qryTeste = session.createSQLQuery(CUSTOM_SQL);

			@SuppressWarnings("unchecked")
			List<Object[]> rows = qryTeste.list();
		 
			if ((rows != null) && (! rows.isEmpty())) {
				for (Object[] index : rows) {
					id = Integer.parseInt(index[1].toString());
					break;
				}
			}
		
		}
		catch (HibernateException ex) {
			JOptionPane.showMessageDialog(null, "Exceção ao Executar SQL Mensagem: " + ex.getMessage());
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
		
		return id;

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
        
}