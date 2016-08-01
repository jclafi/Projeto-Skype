package controller;

import modal.Erros_Skype_Static;

public class GerenciaSessao extends Thread {
	
	//Entre uma carga e outra aguarda 1 minuto
	private final long SLEEP_TIME = 60000;
	private DefineEstruturaProjeto objEstruturaSkype;
	
	public DefineEstruturaProjeto getObjEstruturaSkype() { return objEstruturaSkype; }
	public void setObjEstruturaSkype(DefineEstruturaProjeto objEstruturaSkype) { this.objEstruturaSkype = objEstruturaSkype; }

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		verificaFlagExecucao();
		
	}

	private void verificaFlagExecucao() {
		
		do {
			
			try{
				
				GerenciaSessao.sleep(SLEEP_TIME);
				
			} catch (InterruptedException e) {
				Erros_Skype_Static.salvaErroSkype("Interrupted Exception no Timer Gerencia Sessão Servidor");
				e.printStackTrace();
			}
			
			objEstruturaSkype.getObjConfiguracao().carregaConfiguracao(objEstruturaSkype.getObjConfiguracao().getCODIGO_CONFIGURACAO());
			
		} while (objEstruturaSkype.getObjConfiguracao().getStatusListener().equals("E"));

		objEstruturaSkype.finalizaAplicacao(true);
		
	}

}