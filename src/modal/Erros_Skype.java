package modal;

import java.sql.Timestamp;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Erros_Skype {

	private int id_geral;
	private String content;
	private String ip_adress;
	private String host_name;
	private Timestamp error_date;
	private String account_name;
	private SessionFactory objSessionFactory;
	
	public int getId_geral() { return id_geral; }
	public void setId_geral(int pid_geral) { this.id_geral = pid_geral; }
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
	
	public int retornaUltimoID(String accountLogged) {
		
		int varId = 0;
		
		final String CUSTOM_SQL = " select coalesce(max(id_geral), 0) as id_geral from erros_skype where account_name = :account ";
				
		//Cria a sess�o
		Session session = objSessionFactory.openSession();		
		
		SQLQuery qryTeste = null;
		try {			
		
			qryTeste = session.createSQLQuery(CUSTOM_SQL);
			qryTeste.setParameter("account", accountLogged);	
			
			for (int index = 0; index < qryTeste.list().size();) {
				varId = (Integer) qryTeste.list().get(index);
				break;
			}
		
		}
		catch (Exception ex) {
			Erros_Skype_Static.salvaErroSkype("Exce��o ao Retornar �ltimo ID Tabela (NEW). Mensagem: " + ex.getMessage());
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

			System.out.println("Exce��o ao Salvar Dados PC esta��o cliente. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
			return false;
			
		}
		finally {
			
			if (objPersistente != null)
				objPersistente = null;

		}
		
		return ok;
		
	}
	
}