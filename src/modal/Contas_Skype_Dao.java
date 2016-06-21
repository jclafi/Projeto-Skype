package modal;

import org.hibernate.SessionFactory;

public class Contas_Skype_Dao {

	private Contas_Skype conta;
	private SessionFactory objSessionFactory;	
	
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public Contas_Skype getContas_Skype() { return conta; }
	public void setContas_Skype(Contas_Skype mensagem) { this.conta = mensagem; }

	public Contas_Skype_Dao(Contas_Skype pMensagem) {
		
		setContas_Skype(pMensagem);
				
	}
	
}