package source;

import javax.swing.JOptionPane;
import com.skype.*;
import java.util.concurrent.TimeUnit;

public class SkypeListenerClass  {
	
	private final int TEMPO_MINUTOS = 1;
		
	public boolean connectSkype() {
		
		boolean connected = false;
		
		if (! Skype.isInstalled()) {
			JOptionPane.showMessageDialog(null, "Aten��o instala��o do Skype n�o Identificada !");
			return false;
		}
		
		do {
			try {
				
				if (Skype.isRunning())
					connected = true;
				else
					Skype.addApplication("javaw.exe");
				
			} catch (SkypeException ex) {
				System.out.println("Permiss�o de Acesso Skype solicitada !");
				ex.printStackTrace();
			}
			
			//Se n�o conectou aguarda para a pr�xima tentativa
			if (! connected) {
				try {

					TimeUnit.MINUTES.sleep(TEMPO_MINUTOS);
					
				 }
				 catch (InterruptedException ex) {
					 System.out.println("Exce��o ao rodar sleep: " + ex.getMessage());
				 }
			}
			
		}
		while ( ! connected);
		
		return true;
	}

	@SuppressWarnings("deprecation")
	public void startChatListener() {

		SkypeListener skypeListener = new SkypeListener();;
		try {
			
			//Adiciona o Listener para Mensagens On The Fly
			Skype.addChatMessageListener(skypeListener);
			
			// Para n�o permitir desabilitar o programa
			Skype.setDeamon(false);
			
		} catch (SkypeException ex) {
			System.out.println("Mensagem exce��o Listener Skype: " + ex.getMessage());
			ex.printStackTrace();
		}
		
	}

	public void startChatLogBackup() {
		
		Chat[] objChat = null;
		ChatMessage[] objMessage = null;
		try {

			objChat = (Chat []) Skype.getAllChats();
			
			for (Chat indexChat : objChat) {
				
				try {
					
					objMessage = indexChat.getRecentChatMessages();
					objMessage.toString();
					
				}
				catch (SkypeException ex) {
					System.out.println("Mensagem exce��o Listener Skype: " + ex.getMessage());
					ex.printStackTrace();
				}
				
			}
			
		}
		catch (SkypeException ex) {
			System.out.println("Mensagem exce��o Listener Skype: " + ex.getMessage());
			ex.printStackTrace();			
		}
		
	}


}