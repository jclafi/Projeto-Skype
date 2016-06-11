package dao;

//import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hibernate.Hibernate;

public class Mensagens_Skype_Dao {
	private Mensagens_Skype mensagem;
	
	public Mensagens_Skype getMensagem() { return mensagem; }
	public void setMensagem(Mensagens_Skype mensagem) { this.mensagem = mensagem; }

	public Mensagens_Skype_Dao(Mensagens_Skype pMensagem) {
		
		setMensagem(pMensagem);
				
	}
	
	public long getPk() {
		long pk = 1;
		
		final String CUSTOM_SQL = " select * from mensagens_skype order by id_geral desc limit 1 ";
				
		//Cria a sessão
		Session session = Hibernate.getFactory().openSession();

		SQLQuery qryTeste = null;
		try {			
		
			qryTeste = session.createSQLQuery(CUSTOM_SQL);

			@SuppressWarnings("unchecked")
			List<Object[]> rows = qryTeste.list();
		 
			if ((rows != null) && (! rows.isEmpty())) {
				for (Object[] index : rows) {
					Long varLong = new Long(index[0].toString());
					pk = ++varLong;
					break;
				}
			}
			else
				return pk;
		
		}
		catch (HibernateException ex) {
			System.out.println("Exceção ao Executar SQL Customizado:" + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (!qryTeste.list().isEmpty()) {
				qryTeste.list().clear();
				qryTeste = null;
			}
		
			if (session != null) {
				session.close();
				session = null;				
			}
		}
		
		return pk;

	}
	
	public boolean salvaMensagem() {

		//Cria a Session
		Session session = Hibernate.getFactory().openSession();
		
		//Cria a Transacation
		Transaction tx = null;		
		
		try {
	
			tx = session.beginTransaction();
						
			if (mensagem != null) {
				session.save(mensagem);
				tx.commit();			
			}
		}
		catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			System.out.println("Exceção ao Salvar Pessoa:" + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		finally {
			if (session != null) {
				session.close();
				session = null;
			}			
			if (tx != null)
				tx = null;
		}
		
		return true;
	}
			
	public boolean removeMensagem() {		
				
		//Objeto Session
		Session session = Hibernate.getFactory().openSession();
		
		//Objeto Transação
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
						
			if (mensagem != null) {
				session.delete(mensagem);			
				tx.commit();
			}

		}
		catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			return false;
		}
		finally {
			if (session != null) {
				session.close();				
			}			
			if (tx != null) 
				tx = null;
		}		
		
		
		return true;	
	}
	
	public boolean updatePessoa() {
		
		//Objeto Session
		Session session = Hibernate.getFactory().openSession();
		
		//Objeto Transação
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
						
			if (mensagem != null) {					
				session.update(mensagem);			
				tx.commit();
			}
		}
		catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			return false;
		}
		finally {
			if (session != null) {
				session.close();				
			}			
			if (tx != null) 
				tx = null;
		}		
				
		return true;
	}
	
	public void finalize() {

	}
	
}