package etl;

import javax.swing.JOptionPane;
import com.skype.*;
import java.util.concurrent.TimeUnit;

public class IniciaSkypeListener  {
	
	private final int TEMPO_MINUTOS = 10;
	
	public boolean verificaInstalação() {

		if (! Skype.isInstalled()) {
		
			JOptionPane.showMessageDialog(null, "Atenção instalação do Skype não Identificada !");
			return false;
		
		}
		
		return true;
		
	}
		
	public boolean connectSkype() {
		
		boolean connected = false;
		
		if (! Skype.isInstalled()) {

			JOptionPane.showMessageDialog(null, "Atenção instalação do Skype não Identificada !");
			return false;
	
		}
		
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
	public void startChatListener() {

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