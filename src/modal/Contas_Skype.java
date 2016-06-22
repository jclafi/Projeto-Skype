package modal;

import org.hibernate.SessionFactory;

public class Contas_Skype {

	private int id_geral;
	private String account_name;
	private String display_name;
	private String ip_adress;
	private String host_name;
	private String account_verified;
	private SessionFactory objSessionFactory;
	
	public int getId_geral() { return id_geral; }
	public void setId_geral(int id_geral) { this.id_geral = id_geral; }
	public String getAccount_name() { return account_name; }
	public void setAccount_name(String account_name) { this.account_name = account_name; }
	public String getDisplay_name() { return display_name; }
	public void setDisplay_name(String display_name) { this.display_name = display_name; }
	public String getIp_adress() { return ip_adress; }
	public void setIp_adress(String ip_adress) { this.ip_adress = ip_adress; }
	public String getHost_name() { return host_name; }
	public void setHost_name(String host_name) { this.host_name = host_name; }
	public String getAccount_verified() { return account_verified; }
	public void setAccount_verified(String account_verified) { this.account_verified = account_verified; }
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	
	public boolean salvaConta() {
		
		boolean bolOk = false;
		
		Contas_Skype_Dao objPersistente = new Contas_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);
			bolOk = objPersistente.salvaConta();
			
		}
		finally {
	    	if (objPersistente != null)
	    		objPersistente = null;			
		}
		
		return bolOk;
	}
	
	public boolean atualizaConta() {
		
		boolean bolOk = false;
		
		Contas_Skype_Dao objPersistente = new Contas_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);
			bolOk = objPersistente.atualizaConta();
			
		}
		finally {
	    	if (objPersistente != null)
	    		objPersistente = null;			
		}
		
		return bolOk;		
		
	}
	
	public boolean removeConta() {
		
		boolean bolOk = false;
		
		Contas_Skype_Dao objPersistente = new Contas_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);	
			bolOk = objPersistente.removeConta();
			
		}
		finally {
	    	if (objPersistente != null)
	    		objPersistente = null;			
		}
		
		return bolOk;
	}
	
}