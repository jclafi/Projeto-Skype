package modal;

import java.math.BigInteger;
import java.sql.Timestamp;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Erros_Skype {

	private long id_geral;
	private long id;
	private String content;
	private String ip_adress;
	private String host_name;
	private Timestamp error_date;
	private String account_name;
	private SessionFactory objSessionFactory;
	
	public long getId_geral() { return id_geral; }
	public void setId_geral(long pid_geral) { this.id_geral = pid_geral; }
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public String getContent() { return content; }
	public void setContent(String pcontent) { this.content = pcontent; }
	public String getIp_adress() { return ip_adress; }
	public void setIp_adress(String pip_adress) { this.ip_adress = pip_adress; }
	public String getHost_name() { return host_name; }
	public void setHost_name(String phost_name) { this.host_name = phost_name; }
	public Timestamp getError_date() { return error_date; }
	public void setError_date(Timestamp perror_date) { this.error_date = perror_date; }
	public String getAccount_name() { return account_name; }
	public void setAccount_name(String account_name) { this.account_name = account_name; }
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	
	public long retornaUltimoID(String accountLogged) {
		
		long varId = 0;
		
		final String CUSTOM_SQL = " select coalesce(max(id), 0) as id from erros_skype where account_name = :account ";
				
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

	public boolean salvaErroSkype() {
		
		boolean ok = false;
		
		Erros_Skype_Dao objPersistente = new Erros_Skype_Dao();
		try {
						
			objPersistente.setObjSessionFactory(objSessionFactory);
			objPersistente.setObjErros_Skype(this);
			
			ok = objPersistente.salvaErroSkype();
				
		}
		catch (Exception ex) {

			System.out.println("Exceção ao Salvar Dados PC estação cliente. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
			return false;
			
		}
		finally {
			
			if (objPersistente != null)
				objPersistente = null;

		}
		
		return ok;
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean equals(Object o) {
		
		//O parâmetro não pode ser nulo
		if (o == null) return false;
		
		//Se não for um objeto da classe Pessoa retorna nulo
		if (! (this.getClass().equals(o.getClass()))) return false;
		
		Erros_Skype outra = (Erros_Skype) o;
		
		return ( (this.id_geral == outra.getId_geral())) &&
				 (this.id == outra.getId()) &&
				 (this.account_name.equals(outra.getAccount_name())) &&
				 (this.content.equals(outra.getContent())) &&
				 (this.host_name.equals(outra.getHost_name())) &&
				 (this.ip_adress.equals(outra.getIp_adress()) && 
				 (this.error_date.getDate() == outra.getError_date().getDate()) );					
	}	

	public int hashCode() {

		@SuppressWarnings("deprecation")
		String atributos = (this.id_geral +
							this.content + 
							this.host_name + 
							this.ip_adress + 
							this.host_name + 
							this.error_date.getDate());
		
		return atributos.hashCode();
	
	}
	
}