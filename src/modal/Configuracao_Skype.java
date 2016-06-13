package modal;

import javax.swing.JOptionPane;

public class Configuracao_Skype {
	
	private int id_geral;
	private String skypeDatabase;
	private String skypeListener;
	
	public int getId_geral() {
		return id_geral;
	}
	public void setId_geral(int id_geral) {
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
	
	public static final int CODIGO_CONFIGURACAO = 1;

	public Configuracao_Skype() {
	
	}	
	
	public Configuracao_Skype(int varIdGeral) {
		
		setConfiguracao(varIdGeral);
		
	}

	public void setConfiguracao(int id_geral) {
		
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