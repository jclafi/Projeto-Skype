package etl;

import javax.swing.JOptionPane;
import com.skype.*;
import java.util.concurrent.TimeUnit;

public class IniciaSkypeListener  {
	
	private final int TEMPO_MINUTOS = 10;
	
	public boolean verificaInstala��o() {

		if (! Skype.isInstalled()) {
		
			JOptionPane.showMessageDialog(null, "Aten��o instala��o do Skype n�o Identificada !");
			return false;
		
		}
		
		return true;
		
	}
		
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

				JOptionPane.showMessageDialog(null, "Permiss�o de Acesso Skype solicitada !");
				ex.printStackTrace();
		
			}
			
			//Se n�o conectou aguarda para a pr�xima tentativa
			if (! connected) {
				try {

					TimeUnit.MINUTES.sleep(TEMPO_MINUTOS);
					
				 }
				 catch (InterruptedException ex) {
			
					 JOptionPane.showMessageDialog(null, "Exce��o ao rodar sleep: " + ex.getMessage());
				 
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
			JOptionPane.showMessageDialog(null, "Mensagem exce��o Listener Skype: " + ex.getMessage());
			ex.printStackTrace();
		}
		
	}

}