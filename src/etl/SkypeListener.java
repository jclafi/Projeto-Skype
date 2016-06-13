package etl;

import javax.swing.JOptionPane;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

import modal.Mensagens_Skype;

public class SkypeListener implements ChatMessageListener {
	
	public static final String VERSION = "Versão Skype Homologada: 7.24.0.104";
	private Mensagens_Skype objMensagem;
	
	public SkypeListener() {
		
		objMensagem = new Mensagens_Skype();
		
	}
	
	@Override
	public void chatMessageReceived(ChatMessage recMessage) throws SkypeException {
		try {						
			
			objMensagem.setId(Integer.parseInt(recMessage.getId().toString()));						
			objMensagem.setId_sender(recMessage.getSenderId().toString());
			objMensagem.setSender_display_name(recMessage.getSenderDisplayName().toString());			
			objMensagem.setContent(recMessage.getContent().toString());
			objMensagem.setChat(recMessage.getChat().toString());
			objMensagem.setMessage_type("R");
			
			objMensagem.salvaMensagem();
		}
		catch (final SkypeException ex) {
			JOptionPane.showMessageDialog(null, VERSION +  "\n  Exceção no Skype Listener. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		
	}

	@Override
	public void chatMessageSent(ChatMessage sentMessage) throws SkypeException {
		try {
			
			objMensagem.setId(Integer.parseInt(sentMessage.getId().toString()));						
			objMensagem.setId_sender(sentMessage.getSenderId().toString());
			objMensagem.setSender_display_name(sentMessage.getSenderDisplayName().toString());			
			objMensagem.setContent(sentMessage.getContent().toString());
			objMensagem.setChat(sentMessage.getChat().toString());
			objMensagem.setMessage_type("E");
			
			objMensagem.salvaMensagem();
		}
		catch (final SkypeException ex) {
			JOptionPane.showMessageDialog(null, VERSION +  "\n  Exceção no Skype Listener. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public void finalize() {
		
		if (objMensagem != null)
			objMensagem =null;
		
	}

}