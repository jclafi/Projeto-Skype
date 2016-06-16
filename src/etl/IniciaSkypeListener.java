package etl;

import javax.swing.JOptionPane;
import com.skype.*;

import modal.UsuarioLogado;

import java.util.concurrent.TimeUnit;

public class IniciaSkypeListener extends Thread {
	
	private final int TEMPO_MINUTOS = 10;
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		//Valida se o Operador conectou no Skype e Cria o Listener que grava Mensagens enviadas/recebidas
		if ((carregaUsuario() != null) && (connectSkype()))
			startChatListener();
		
	}	
	
	private UsuarioLogado carregaUsuario() {
		
		UsuarioLogado objUser = new UsuarioLogado();
		
		objUser.getUsuarioLogado();
		
		return objUser;
		
	}
	
	public boolean verificaInstalação() {

		if (! Skype.isInstalled()) {
		
			JOptionPane.showMessageDialog(null, "Atenção instalação do Skype não Identificada !");
			return false;
		
		}
		
		return true;
		
	}
		
	private boolean connectSkype() {
		
		boolean connected = false;
		
		do {
			try {
				
				if (Skype.isRunning())
					connected = true;
				else
					Skype.addApplication("javaw.exe");
				
			} catch (SkypeException ex) {

				JOptionPane.showMessageDialog(null, "Permissão de Acesso Skype solicitada !");
				ex.printStackTrace();
		
			}
			
			//Se não conectou aguarda para a próxima tentativa
			if (! connected) {
				try {

					TimeUnit.MINUTES.sleep(TEMPO_MINUTOS);
					
				 }
				 catch (InterruptedException ex) {
			
					 JOptionPane.showMessageDialog(null, "Exceção ao rodar sleep: " + ex.getMessage());
				 
				 }
				
			}
			
		}
		while ( ! connected);
		
		return true;
	}

	@SuppressWarnings("deprecation")
	private void startChatListener() {

		SkypeListener skypeListener = new SkypeListener();;
		try {
			
			//Adiciona o Listener para Mensagens On The Fly
			Skype.addChatMessageListener(skypeListener);
			
			// Para não permitir desabilitar o programa
			Skype.setDeamon(false);
			
		} catch (SkypeException ex) {
			JOptionPane.showMessageDialog(null, "Mensagem exceção Listener Skype: " + ex.getMessage());
			ex.printStackTrace();
		}
		
	}

}