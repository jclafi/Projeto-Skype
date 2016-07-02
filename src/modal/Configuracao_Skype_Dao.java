package modal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import modal.Erros_Skype_Static;

public class Configuracao_Skype_Dao {
	
	private Configuracao_Skype objRegraConfiguracao;
	private SessionFactory objSessionFactory;	

	public Configuracao_Skype getObjRegraConfiguracao() { return objRegraConfiguracao; }
	public void setObjRegraConfiguracao(Configuracao_Skype objRegraConfiguracao) { this.objRegraConfiguracao = objRegraConfiguracao; }
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
		
	public boolean carregaConfiguracao(int id_geral) {

		// Objeto Session
		Session session = objSessionFactory.openSession();
		try {
			
			setObjRegraConfiguracao(session.get(Configuracao_Skype.class, id_geral));
	
		}
		catch (HibernateException ex) {
			ex.printStackTrace();
			Erros_Skype_Static.salvaErroSkype("Hibernate Exception ao Carregar Configuração Skype. Mensagem: " + ex.getMessage());
			return false;			
		}		
		catch (Exception ex) {
		
			setObjRegraConfiguracao(null);
			ex.printStackTrace();
			Erros_Skype_Static.salvaErroSkype("Exception ao Carregar Configuração Skype. Mensagem: " + ex.getMessage());
			return false;
		
		} 
		finally {
		
			if (session != null) {
				session.close();
			
			}
		}

		return ((getObjRegraConfiguracao() != null) && (getObjRegraConfiguracao().getId_geral() > 0));
	
	}
	
	public boolean salvaConfiguracao() {

		// Objeto Session
		Session session = objSessionFactory.openSession();
		
		// Objeto Transação
		Transaction tx = null;
		try {
			
			tx = session.beginTransaction();			
			session.saveOrUpdate(getObjRegraConfiguracao());
			tx.commit();

		} 
		catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			Erros_Skype_Static.salvaErroSkype("Hibernate Exception Exceção ao Salvar Configuração Skype. Mensagem: " + ex.getMessage());
			return false;			
		}		
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			Erros_Skype_Static.salvaErroSkype("Exception ao Salvar Configuração Skype. Mensagem: " + ex.getMessage());
			return false;
		} 
		finally {
		
			if (session != null) {
				session.close();
			
			}
	
		}

		return true;
	
	}
	
}