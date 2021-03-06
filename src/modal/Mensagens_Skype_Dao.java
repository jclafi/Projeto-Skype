package modal;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.math.BigInteger;

public class Mensagens_Skype_Dao {
	
	private Mensagens_Skype mensagem;
	private SessionFactory objSessionFactory;	
	
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public Mensagens_Skype getMensagem() { return mensagem; }
	public void setMensagem(Mensagens_Skype mensagem) { this.mensagem = mensagem; }

	public Mensagens_Skype_Dao(Mensagens_Skype pMensagem) {
		
		setMensagem(pMensagem);
				
	}
	
	public long getPk() {
		
		long pk = 0;
		
		final String CUSTOM_SQL = " select coalesce(max(id_geral), 0) as id_geral from mensagens_skype ";
				
		//Cria a sess�o
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
			Erros_Skype_Static.salvaErroSkype("Exception ao Identificar PK DAO Mensagem Skype. Erro: " + ex.getMessage());
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
	
	public boolean salvaMensagem() {

		//Cria a Session
		Session session = objSessionFactory.openSession();
		
		//Cria a Transacation
		Transaction tx = null;		
		
		try {
	
			tx = session.beginTransaction();
						
			if (mensagem != null) {
				mensagem.setId_geral(getPk());
				session.save(mensagem);
				tx.commit();			
			}
			else
				tx.rollback();
		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			Erros_Skype_Static.salvaErroSkype("Exception ao Salvar DAO Mensagem: " + ex.getMessage());
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
			
	public boolean removeMensagem() {		
				
		//Objeto Session
		Session session = objSessionFactory.openSession();
		
		//Objeto Transa��o
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
						
			if (mensagem != null) {
				session.delete(mensagem);			
				tx.commit();
			}
			else
				tx.rollback();

		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			Erros_Skype_Static.salvaErroSkype("Exception ao Remover DAO Mensagem: " + ex.getMessage());
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
	
	public boolean atualizaMensagem() {
		
		//Objeto Session
		Session session = objSessionFactory.openSession();
		
		//Objeto Transa��o
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
						
			if (mensagem != null) {					
				session.update(mensagem);			
				tx.commit();
			}
			else
				tx.rollback();
		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			Erros_Skype_Static.salvaErroSkype("Exception ao Atualizar DAO Mensagem: " + ex.getMessage());
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
	
	public boolean carregaMensagem(long id_geral) {

		// Objeto Session
		Session session = objSessionFactory.openSession();

		try {
			setMensagem(session.get(Mensagens_Skype.class, id_geral));
		} catch (Exception ex) {
			ex.printStackTrace();
			Erros_Skype_Static.salvaErroSkype("Exception ao Carregar DAO a Mensagem: " + ex.getMessage());
			return false;
		} finally {
			if (session != null) {
				if (session.isOpen())
					session.close();
				session = null;				
			}			
		}

		return ((getMensagem() != null) && (getMensagem().getId_geral() > 0));
	}

}