package etl;

public class IniciaSalvaMensagens extends Thread {
	
	//Entre uma carga e outra aguarda 10 minutos
	private final long SLEEP_TIME = 600000;
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		criaObjetoMensagens();
		
	}	
	
	/*
	 * Método recursivo que gerencia o E.T.L das mensagens
	 */
	private void criaObjetoMensagens() {
		
		SalvaMensagens objMensagens = new SalvaMensagens();
		try {
			
			objMensagens.executaCargaMensagens();	
			
		}		
		finally {
		
			if (objMensagens != null)
				objMensagens = null;			
		
		}
		
		System.gc();
		
		//Pausa para a nova carga de Mensagens
		try {

			IniciaSalvaMensagens.sleep(SLEEP_TIME);
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Método recursivo para carga de Mensagens
		criaObjetoMensagens();

	}
	
}