package modal;

import java.math.BigInteger;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Erros_Skype_Dao {

	private Erros_Skype objErros_Skype;
	private SessionFactory objSessionFactory;	
	
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };
	public Erros_Skype getObjErros_Skype() { return objErros_Skype; }
	public void setObjErros_Skype(Erros_Skype objErros_Skype) { this.objErros_Skype = objErros_Skype; }
	
	public long getPk() {
		
		long pk = 0;
		
		final String CUSTOM_SQL = " select coalesce(max(id_geral), 0) as id_geral from erros_skype ";
				
		//Cria a sessão
		Session session = objSessionFactory.openSession();

		SQLQuery qryTeste = null;
		try {			
		
			qryTeste = session.createSQLQuery(CUSTOM_SQL);

			for (int index = 0; index < qryTeste.list().size();) {
				BigInteger objTemp = (BigInteger) qryTeste.list().get(index);
				pk = objTemp.longValue();
				break;
			}
		
		}
		catch (Exception ex) {
			System.out.println("Exceção ao Executar SQL Conta: " + ex.getMessage());
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
		
		return ++pk;

	}
	
	public boolean salvaErroSkype() {

		//Cria a Session
		Session session = objSessionFactory.openSession();
		
		//Cria a Transacation
		Transaction tx = null;		
		
		try {
	
			tx = session.beginTransaction();
						
			if (objErros_Skype != null) {
				objErros_Skype.setId_geral(getPk());
				session.save(objErros_Skype);
				tx.commit();			
			}
		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			System.out.println("Exceção ao Salvar Conta: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		finally {
			if (session != null) {
				if (session.isOpen())
					session.close();
				session = null;
			}			
			if (tx != null)
				tx = null;
		}
		
		return true;
	}

	public void finalize() {
		
		if (objErros_Skype != null)
			objErros_Skype = null;
		
	}
	
	
}