package modal;

import org.hibernate.SessionFactory;

public class Configuracao_Skype {
	
	private int id_geral;
	private String skypeDatabase;
	private String skypeListener;
	private String mySqlRoot;
	private String mySqlPassWord;
	private String mySQLHost;
	private String skypeAccount;
	
	public int getId_geral() { return id_geral; }
	public void setId_geral(int id_geral) { this.id_geral = id_geral; }
	public String getSkypeDatabase() { return skypeDatabase; }
	public void setSkypeDatabase(String skypeDatabase) { this.skypeDatabase = skypeDatabase; }
	public String getSkypeListener() { return skypeListener; }
	public void setSkypeListener(String skypeListener) { this.skypeListener = skypeListener; }
	public String getMySqlRoot() { return mySqlRoot; }
	public void setMySqlRoot(String mySqlRoot) { this.mySqlRoot = mySqlRoot; }
	public String getMySqlPassWord() { return mySqlPassWord; }
	public void setMySqlPassWord(String mySqlPassWord) { this.mySqlPassWord = mySqlPassWord; }
	public String getMySQLHost() { return mySQLHost; }
	public void setMySQLHost(String mySQLHost) { this.mySQLHost = mySQLHost; }
	public String getSkypeAccount() { return skypeAccount; }
	public void setSkypeAccount(String skypeAccount) { this.skypeAccount = skypeAccount; }

	private final int CODIGO_CONFIGURACAO = 1;
	private SessionFactory objSessionFactory;		

	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };			
	public int getCODIGO_CONFIGURACAO() { return CODIGO_CONFIGURACAO; }
	
	public boolean carregaConfiguracao(int id_geral) {
		
		Configuracao_Skype_Dao objPersistente = new Configuracao_Skype_Dao();
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);
			
			if (objPersistente.carregaConfiguracao(id_geral)) {

				setId_geral(objPersistente.getObjRegraConfiguracao().getId_geral());
				setSkypeDatabase(objPersistente.getObjRegraConfiguracao().getSkypeDatabase());
				setSkypeListener(objPersistente.getObjRegraConfiguracao().getSkypeListener());
				setMySQLHost(objPersistente.getObjRegraConfiguracao().getMySQLHost());
				setMySqlPassWord(objPersistente.getObjRegraConfiguracao().getMySqlPassWord());
				setMySqlRoot(objPersistente.getObjRegraConfiguracao().getMySqlRoot());
				setSkypeAccount(objPersistente.getObjRegraConfiguracao().getSkypeAccount());
			}				
			else {
				
				Erros_Skype.salvaErroSkype("Atenção erro ao carregar a configuração !");
				return false;
				
			}
		}
		finally {
			if (objPersistente != null)
				objPersistente = null;
		}
		
		return true;
		
	}
	
	public boolean salvaConfiguracao() {
		
		boolean ok = false;
		
		Configuracao_Skype_Dao objPersistente = new Configuracao_Skype_Dao();
		try {
			
			objPersistente.setObjSessionFactory(objSessionFactory);
			objPersistente.setObjRegraConfiguracao(this);
			
			ok = objPersistente.salvaConfiguracao();
			
		}
		finally {
			if (objPersistente != null)
				objPersistente = null;			
		}
		
		return ok;
		
	}
	
}