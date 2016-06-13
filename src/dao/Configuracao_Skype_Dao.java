package dao;

import java.util.List;

import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import hibernate.Hibernate;

public class Configuracao_Skype_Dao {
	
	private Configuracao_Skype objRegraConfiguracao;

	public Configuracao_Skype getObjRegraConfiguracao() {
		return objRegraConfiguracao;
	}
	public void setObjRegraConfiguracao(Configuracao_Skype objRegraConfiguracao) {
		this.objRegraConfiguracao = objRegraConfiguracao;
	}

	public long getPk() {
		
		long pk = 1;
		
		final String CUSTOM_SQL = " select * from configuracao_skype order by id_geral desc limit 1 ";
				
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
			JOptionPane.showMessageDialog(null, "Exceção ao Gerar PK Configuração: " + ex.getMessage());
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
	
	public boolean carregaConfiguracao(long id_geral) {

		// Objeto Session
		Session session = Hibernate.getFactory().openSession();

		try {
			setObjRegraConfiguracao(session.get(Configuracao_Skype.class, id_geral));
		} catch (Exception ex) {
			setObjRegraConfiguracao(null);
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Exceção ao Carregar a Mensagem: " + ex.getMessage());
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return ((getObjRegraConfiguracao() != null) && (getObjRegraConfiguracao().getId_geral() > 0));
	}
	
}