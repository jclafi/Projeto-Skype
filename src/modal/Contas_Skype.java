package modal;

import java.math.BigInteger;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Contas_Skype {

	private long id_geral;
	private String account_name;
	private String display_name;
	private String ip_adress;
	private String host_name;
	private String account_verified;
	private SessionFactory objSessionFactory;
	
	public long getId_geral() { return id_geral; }
	public void setId_geral(long id_geral) { this.id_geral = id_geral; }
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
	
	public boolean carregaConta(long id_geral) {
		
		Contas_Skype_Dao objPersistente = new Contas_Skype_Dao(this);
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);
			
			if (objPersistente.carregaConta(id_geral)) {

				setId_geral(objPersistente.getObjContas_Skype().getId_geral());
				setAccount_name(objPersistente.getObjContas_Skype().getAccount_name());
				setAccount_verified(objPersistente.getObjContas_Skype().getAccount_verified());
				setDisplay_name(objPersistente.getObjContas_Skype().getDisplay_name());
				setHost_name(objPersistente.getObjContas_Skype().getHost_name());
				setIp_adress(objPersistente.getObjContas_Skype().getIp_adress());
				
			}				
			else {
				
				Erros_Skype_Static.salvaErroSkype("Atenção não foi possível carregar a Conta do Usuário !");
				return false;
				
			}
		}
		finally {
			if (objPersistente != null)
				objPersistente = null;
		}
		
		return true;
		
	}
		
	public boolean carregaConta(String accountName) {

		boolean ok = false;
		
		final String CUSTOM_SQL = " select id_geral from contas_skype where account_name = :accountname limit 1 ";
		
		//Cria a sessão
		Session session = objSessionFactory.openSession();

		SQLQuery qryTeste = null;
		try {			
		
			qryTeste = session.createSQLQuery(CUSTOM_SQL);
			qryTeste.setParameter("accountname", accountName);
			
			for (int index = 0; index < qryTeste.list().size();) {
				
				BigInteger objTemp = (BigInteger) qryTeste.list().get(index);
				ok =  carregaConta(objTemp.longValue());
				break;
				
			}
		
		}
		catch (Exception ex) {
			Erros_Skype_Static.salvaErroSkype("Exception ao Carregar a Conta Skype: " + ex.getMessage());
			ex.printStackTrace();
			return false;
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
		
		return ok;
		
	}
	
	public boolean equals(Object o) {
		
		//O parâmetro não pode ser nulo
		if (o == null) return false;
		
		//Se não for um objeto da classe Contas retorna nulo
		if (! (this.getClass().equals(o.getClass()))) return false;
		
		Contas_Skype outra = (Contas_Skype) o;
		
		return ( (this.account_name.equals(outra.getAccount_name())) &&
				 (this.display_name.equals(outra.getDisplay_name())) &&
				 (this.ip_adress.equals(outra.getIp_adress())) &&
				 (this.host_name.equals(outra.getHost_name())) &&
				 (this.account_verified.equals(outra.getAccount_verified())) );
	}	

	public int hashCode() {

		String atributos = (this.account_name + 
							this.display_name + 
							this.ip_adress + 
							this.host_name + 
							this.account_verified);
		
		return atributos.hashCode();
	
	}		
	
}