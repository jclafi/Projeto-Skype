package sourceClasses;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

import dao.Mensagens_Skype;

public class SkypeListener implements ChatMessageListener {
	
	private Mensagens_Skype objMensagem;
	private Long objLong;
	
	public SkypeListener() {
		
		objMensagem = new Mensagens_Skype();
		
	}
	
	@Override
	public void chatMessageReceived(ChatMessage recMessage) throws SkypeException {
		try {			
			objLong = new Long(recMessage.getId().toString());			
			
			objMensagem.setId_geral(objMensagem.getObjPersistente().getPk());
			objMensagem.setId(objLong.longValue());						
			objMensagem.setId_sender(recMessage.getSenderId().toString());
			objMensagem.setSender_display_name(recMessage.getSenderDisplayName().toString());			
			objMensagem.setContent(recMessage.getContent().toString());
			objMensagem.setChat(recMessage.getChat().toString());
			objMensagem.setMessage_type("R");
			
			objMensagem.getObjPersistente().salvaMensagem();
		}
		catch (final SkypeException ex) {
			ex.printStackTrace();
		}
		
	}

	@Override
	public void chatMessageSent(ChatMessage sentMessage) throws SkypeException {
		try {
			objLong = new Long(sentMessage.getId().toString());			
			
			objMensagem.setId_geral(objMensagem.getObjPersistente().getPk());			
			objMensagem.setId(objLong.longValue());						
			objMensagem.setId_sender(sentMessage.getSenderId().toString());
			objMensagem.setSender_display_name(sentMessage.getSenderDisplayName().toString());			
			objMensagem.setContent(sentMessage.getContent().toString());
			objMensagem.setChat(sentMessage.getChat().toString());
			objMensagem.setMessage_type("E");
			
			objMensagem.getObjPersistente().salvaMensagem();
		}
		catch (final SkypeException ex) {
			ex.printStackTrace();
		}
	}
	
	public void finalize() {
		
		if (objMensagem != null)
			objMensagem =null;
		
	}

}
