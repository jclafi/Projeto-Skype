package modal;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Contatos_Contas_Skype_Dao {
	
	private Contatos_Contas_Skype contatos;
	private SessionFactory objSessionFactory;	
	
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public Contatos_Contas_Skype getContatos_Contas_Skype() { return contatos; }
	public void setContatos_Contas_Skype(Contatos_Contas_Skype contatos) { this.contatos = contatos; }

	public Contatos_Contas_Skype_Dao(Contatos_Contas_Skype contatos) {
		
		setContatos_Contas_Skype(contatos);
				
	}
	
	public int getPk() {
		
		int pk = 0;
		
		final String CUSTOM_SQL = " select coalesce(max(id_geral), 0) as id_geral from contatos_contas_skype ";
				
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
			Erros_Skype.salvaErroSkype("Exceção ao Identificar a PK DAO Contatos Contas Skype. Mensagem: " + ex.getMessage());
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

	public boolean salvaContatosConta() {

		//Cria a Session
		Session session = objSessionFactory.openSession();
		
		//Cria a Transacation
		Transaction tx = null;		
		
		try {
	
			tx = session.beginTransaction();
						
			if (contatos != null) {
				contatos.setId_geral(getPk());
				session.save(contatos);
				tx.commit();			
			}
		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			Erros_Skype.salvaErroSkype("Exceção ao Salvar DAO Contatos Contas Skype: " + ex.getMessage());
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
			
	public boolean removeContatosConta() {		
				
		//Objeto Session
		Session session = objSessionFactory.openSession();
		
		//Objeto Transação
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
						
			if (contatos != null) {
				session.delete(contatos);			
				tx.commit();
			}

		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			Erros_Skype.salvaErroSkype("Exceção ao Remover DAO Contatos Conta Skype: " + ex.getMessage());
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
	
	public boolean atualizaContatosConta() {
		
		//Objeto Session
		Session session = objSessionFactory.openSession();
		
		//Objeto Transação
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
						
			if (contatos != null) {					
				session.update(contatos);			
				tx.commit();
			}
		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			Erros_Skype.salvaErroSkype("Exceção ao Atualizar DAO Contatos Conta Skype: " + ex.getMessage());
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
	
	public boolean carregaContatosConta(int id_geral) {

		// Objeto Session
		Session session = objSessionFactory.openSession();

		try {
			setContatos_Contas_Skype(session.get(Contatos_Contas_Skype.class, id_geral));
		} catch (Exception ex) {
			ex.printStackTrace();
			Erros_Skype.salvaErroSkype("Exceção ao Carregar DAO os Contatos da Conta Skype: " + ex.getMessage());
			return false;
		} finally {
			if (session != null) {
				if (session.isOpen())
					session.close();
				session = null;				
			}			
		}

		return ((getContatos_Contas_Skype() != null) && (getContatos_Contas_Skype().getId_geral() > 0));
	}
	
}