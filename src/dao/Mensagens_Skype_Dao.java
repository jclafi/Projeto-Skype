package dao;

import java.util.List;

import javax.swing.JOptionPane;

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
					pk = Long.parseLong(index[0].toString());
					++pk;
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
				mensagem.setId_geral(getPk());
				session.save(mensagem);
				tx.commit();			
			}
		}
		catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			JOptionPane.showMessageDialog(null, "Exceção ao Salvar Mensagem: " + ex.getMessage());
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
			JOptionPane.showMessageDialog(null, "Exceção ao Remover Mensagem: " + ex.getMessage());
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
	
	public boolean atualizaMensagem() {
		
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
			JOptionPane.showMessageDialog(null, "Exceção ao Atualizar Mensagem: " + ex.getMessage());
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
	
	public boolean carregaMensagem(long id_geral) {

		// Objeto Session
		Session session = Hibernate.getFactory().openSession();

		try {
			setMensagem(session.get(Mensagens_Skype.class, id_geral));
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Exceção ao Carregar a Mensagem: " + ex.getMessage());
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return ((getMensagem() != null) && (getMensagem().getId_geral() > 0));
	}

		
}