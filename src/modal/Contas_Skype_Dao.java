package modal;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Contas_Skype_Dao {

	private Contas_Skype conta;
	private SessionFactory objSessionFactory;	
	
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public Contas_Skype getObjContas_Skype() { return conta; }
	public void setObjContas_Skype(Contas_Skype conta) { this.conta = conta; }

	public Contas_Skype_Dao(Contas_Skype conta) {
		
		setObjContas_Skype(conta);
				
	}
	
	public int getPk() {
		
		int pk = 0;
		
		final String CUSTOM_SQL = " select coalesce(max(id_geral), 0) as id_geral from contas_skype ";
				
		//Cria a sessão
		Session session = objSessionFactory.openSession();

		SQLQuery qryTeste = null;
		try {			
		
			qryTeste = session.createSQLQuery(CUSTOM_SQL);

			for (int index = 0; index < qryTeste.list().size();) {
				Integer objTemp = (Integer) qryTeste.list().get(index);
				pk = objTemp.intValue();
				break;
			}
		
		}
		catch (Exception ex) {
			Erros_Skype_Static.salvaErroSkype("Exceção ao Identificar PK DAO Contas Skype. Mensagem: " + ex.getMessage());
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
	
	public boolean salvaConta() {

		//Cria a Session
		Session session = objSessionFactory.openSession();
		
		//Cria a Transacation
		Transaction tx = null;		
		
		try {
	
			tx = session.beginTransaction();
						
			if (conta != null) {
				conta.setId_geral(getPk());
				session.save(conta);
				tx.commit();			
			}
		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			Erros_Skype_Static.salvaErroSkype("Exceção ao Salvar DAO Conta Skype: " + ex.getMessage());
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

	public boolean removeConta() {		
		
		//Objeto Session
		Session session = objSessionFactory.openSession();
		
		//Objeto Transação
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
						
			if (conta != null) {
				session.delete(conta);			
				tx.commit();
			}

		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			Erros_Skype_Static.salvaErroSkype("Exceção ao Remover DAO a Conta Skype: " + ex.getMessage());
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
	
	public boolean atualizaConta() {
		
		//Objeto Session
		Session session = objSessionFactory.openSession();
		
		//Objeto Transação
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
						
			if (conta != null) {					
				session.update(conta);			
				tx.commit();
			}
		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			Erros_Skype_Static.salvaErroSkype("Exceção ao Atualizar DAO a Conta Skype: " + ex.getMessage());
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
	
	public boolean carregaConta(int id_geral) {

		// Objeto Session
		Session session = objSessionFactory.openSession();
		try {
			setObjContas_Skype(session.get(Contas_Skype.class, id_geral));
		} catch (Exception ex) {
			ex.printStackTrace();
			Erros_Skype_Static.salvaErroSkype("Exceção ao Carregar DAO a Conta Skype: " + ex.getMessage());
			return false;
		} finally {
			if (session != null) {
				if (session.isOpen())
					session.close();
				session = null;				
			}			
		}

		return ((getObjContas_Skype() != null) && (getObjContas_Skype().getId_geral() > 0));
	}	
	
}