package modal;

import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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