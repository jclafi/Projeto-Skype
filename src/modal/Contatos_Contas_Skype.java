package modal;

import org.hibernate.SessionFactory;

public class Contatos_Contas_Skype {
	
	private int id_geral;
	private int id_conta_skype;
	private String account_name;
	private String display_name;
	private String contact_verified;
	private SessionFactory objSessionFactory;

	public int getId_geral() { return id_geral; }
	public void setId_geral(int id_geral) { this.id_geral = id_geral; }
	public int getId_conta_skype() { return id_conta_skype; }
	public void setId_conta_skype(int id_conta_skype) { this.id_conta_skype = id_conta_skype; }
	public String getAccount_name() { return account_name; }
	public void setAccount_name(String account_name) { this.account_name = account_name; }
	public String getDisplay_name() { return display_name; }
	public void setDisplay_name(String display_name) { this.display_name = display_name; }
	public String getContact_verified() { return contact_verified; }
	public void setContact_verified(String contact_verified) { this.contact_verified = contact_verified; }	
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	

	public boolean salvaContatosConta() {
		
		boolean bolOk = false;
		
		Contatos_Contas_Skype_Dao objPersistente = new Contatos_Contas_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);
			bolOk = objPersistente.salvaContatosConta();
			
		}
		finally {
	    	if (objPersistente != null)
	    		objPersistente = null;			
		}
		
		return bolOk;
	}
	
	public boolean atualizaContatosConta() {
		
		boolean bolOk = false;
		
		Contatos_Contas_Skype_Dao objPersistente = new Contatos_Contas_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);
			bolOk = objPersistente.atualizaContatosConta();
			
		}
		finally {
	    	if (objPersistente != null)
	    		objPersistente = null;			
		}
		
		return bolOk;		
		
	}
	
	public boolean removeContatosConta() {
		
		boolean bolOk = false;
		
		Contatos_Contas_Skype_Dao objPersistente = new Contatos_Contas_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);	
			bolOk = objPersistente.removeContatosConta();
			
		}
		finally {
	    	if (objPersistente != null)
	    		objPersistente = null;			
		}
		
		return bolOk;
	}
	
}