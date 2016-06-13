package etl;

public class DatabaseExtractLoad extends Thread {
	
	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 * 
	 * 
	 */
	public void run() {
		
		criaObjetoMensagens();
		
	}	
	
	/*
	 * 
	 * Método recursivo que gerencia o E.T.L das mensagens
	 * 
	 */
	private void criaObjetoMensagens() {
		
		SalvaMensagens objMensagens = new SalvaMensagens();
		try {
			
			objMensagens.executaCargaMensagens();			
		
		}		
		finally {
		
			if (objMensagens != null)
				objMensagens = null;
			System.gc();
			
			this.criaObjetoMensagens();
			
		}
		
	}
	
}