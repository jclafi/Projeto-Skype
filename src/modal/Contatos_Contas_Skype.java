package modal;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Contatos_Contas_Skype {
	
	private int id_geral;
	private int id_conta_skype;
	private String account_name;
	private String display_name;
	private String contact_verified;
	private SessionFactory objSessionFactory;
	private Set<Contatos_Contas_Skype> objListaContatosContaSkype = new HashSet<Contatos_Contas_Skype>();

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
	public Set<Contatos_Contas_Skype> getObjListaContatosContaSkype() { return objListaContatosContaSkype; }
	public void setObjListaContatosContaSkype(Set<Contatos_Contas_Skype> objContatosContaSkype) { this.objListaContatosContaSkype = objContatosContaSkype; }
	
	public boolean carregaContatosConta(int id_conta_skype) {
		
		boolean ok = false;

		final String CUSTOM_SQL = " select id_geral from contatos_contas_skype where id_conta_skype = :id_conta_skype ";
				
		Contatos_Contas_Skype_Dao objPersistente;
			
		Session session = objSessionFactory.openSession();

		SQLQuery qryTeste = null;
		try {			
		
			qryTeste = session.createSQLQuery(CUSTOM_SQL);
			qryTeste.setParameter("id_conta_skype", id_conta_skype);
			
			for (int index = 0; index < qryTeste.list().size(); index++) {
				
				Integer objTemp = (Integer) qryTeste.list().get(index);
				
				objPersistente = new Contatos_Contas_Skype_Dao(this);
				objPersistente.setObjSessionFactory(objSessionFactory);
				
				if (objPersistente.carregaContatosConta(objTemp.intValue())) 
					ok = objListaContatosContaSkype.add(objPersistente.getContatos_Contas_Skype());
				else 
					ok = false;
				
				if (! ok) break;
			}
		
		}
		catch (Exception ex) {
			Erros_Skype_Static.salvaErroSkype("Exceção ao Carregar Contatos da Conta Skype: Mensagem: " + ex.getMessage());
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
	
	public boolean equals(Object o) {
		
		//O parâmetro não pode ser nulo
		if (o == null) return false;
		
		//Se não for um objeto da classe Contatos Conta retorna nulo
		if (! (this.getClass().equals(o.getClass()))) return false;
		
		Contatos_Contas_Skype outra = (Contatos_Contas_Skype) o;
		
		return ( (this.account_name.equals(outra.getAccount_name())) &&
				 (this.display_name.equals(outra.getDisplay_name())) &&
				 (this.id_conta_skype == outra.getId_conta_skype()) &&
				 (this.contact_verified.equals(outra.getContact_verified())) );
				
	}	

	public int hashCode() {

		String atributos = (this.account_name + 
							this.display_name + 
							this.contact_verified + 
							this.id_conta_skype);
		
		return atributos.hashCode();
	
	}
	
	public void finalize() {		
		
		if (objListaContatosContaSkype != null) {
	
			if (! objListaContatosContaSkype.isEmpty())
				objListaContatosContaSkype.clear();
			objListaContatosContaSkype = null;
	
		}
		
	}
	
}