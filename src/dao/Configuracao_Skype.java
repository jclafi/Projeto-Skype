package dao;

import javax.swing.JOptionPane;

public class Configuracao_Skype {
	
	private long id_geral;
	private String skypeDatabase;
	private String skypeListener;
	
	public long getId_geral() {
		return id_geral;
	}
	public void setId_geral(long id_geral) {
		this.id_geral = id_geral;
	}
	public String getSkypeDatabase() {
		return skypeDatabase;
	}
	public void setSkypeDatabase(String skypeDatabase) {
		this.skypeDatabase = skypeDatabase;
	}
	public String getSkypeListener() {
		return skypeListener;
	}
	public void setSkypeListener(String skypeListener) {
		this.skypeListener = skypeListener;
	}
	
	public static final long CODIGO_CONFIGURACAO = 1;

	public Configuracao_Skype() {
	
	}	
	
	public Configuracao_Skype(long varIdGeral) {
		
		setConfiguracao(varIdGeral);
		
	}

	private void setConfiguracao(long id_geral) {
		
		Configuracao_Skype_Dao objPersistente = new Configuracao_Skype_Dao();
		try {
			
			if (objPersistente.carregaConfiguracao(id_geral)) {

				setId_geral(objPersistente.getObjRegraConfiguracao().getId_geral());
				setSkypeDatabase(objPersistente.getObjRegraConfiguracao().getSkypeDatabase());
				setSkypeListener(objPersistente.getObjRegraConfiguracao().getSkypeListener());

			}				
			else
				JOptionPane.showMessageDialog(null, "Atenção não foi possível carregar a configuração !");
		}
		finally {
			if (objPersistente != null)
				objPersistente = null;
		}
		
	}
	
}