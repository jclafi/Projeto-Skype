package etl;

import java.net.InetAddress;

import javax.swing.JOptionPane;
import org.hibernate.SessionFactory;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;
import modal.Mensagens_Skype;
import modal.Usuario_Logado;

public class EtlListener implements ChatMessageListener {
	
	private final String VERSION = "Versão Skype Homologada: 7.24.0.104";
	private SessionFactory objSessionFactory;	
	private Usuario_Logado objUsuarioRegras;

	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public Usuario_Logado getUsuarioRegras() { return objUsuarioRegras; }
	public void setUsuarioRegras(Usuario_Logado objSkypeUser) { this.objUsuarioRegras = objSkypeUser; }
	
	@Override
	public void chatMessageReceived(ChatMessage recMessage) throws SkypeException {
		
		Mensagens_Skype objMensagem = new Mensagens_Skype();
		try {					
			objMensagem.setObjSessionFactory(objSessionFactory);
			
			objMensagem.setId(Integer.parseInt(recMessage.getId().toString()));						
			objMensagem.setId_sender(recMessage.getSenderId().toString());
			objMensagem.setSender_display_name(recMessage.getSenderDisplayName().toString());			
			objMensagem.setContent(recMessage.getContent().toString());
			objMensagem.setChat(recMessage.getChat().toString());
			objMensagem.setMessage_date(recMessage.getTime());
			objMensagem.setMessage_type("R");
			objMensagem.setAccount_logged(objUsuarioRegras.getSigninName());			
			objMensagem.setHost_name(InetAddress.getLocalHost().getHostName());
			objMensagem.setIp_adress(InetAddress.getLocalHost().getHostAddress());
//			objMensagem.setAccount_verified("N");
//			objMensagem.setContact_verified("N");			
			
			objMensagem.salvaMensagem();
		}
		catch (final SkypeException ex) {
			JOptionPane.showMessageDialog(null, VERSION +  "\n  Exceção no Skype Listener. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, VERSION +  "\n  Exceção no Skype Listener. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (objMensagem != null)
				objMensagem = null;
		}
		
	}

	@Override
	public void chatMessageSent(ChatMessage sentMessage) throws SkypeException {

		Mensagens_Skype objMensagem = new Mensagens_Skype();
		try {					
			objMensagem.setObjSessionFactory(objSessionFactory);
			
			objMensagem.setId(Integer.parseInt(sentMessage.getId().toString()));						
			objMensagem.setId_sender(sentMessage.getSenderId().toString());
			objMensagem.setSender_display_name(sentMessage.getSenderDisplayName().toString());			
			objMensagem.setContent(sentMessage.getContent().toString());
			objMensagem.setChat(sentMessage.getChat().toString());
			objMensagem.setMessage_date(sentMessage.getTime());
			objMensagem.setMessage_type("E");
			objMensagem.setAccount_logged(objUsuarioRegras.getSigninName());			
			objMensagem.setHost_name(InetAddress.getLocalHost().getHostName());
			objMensagem.setIp_adress(InetAddress.getLocalHost().getHostAddress());
//			objMensagem.setAccount_verified("N");
//			objMensagem.setContact_verified("N");			
			
			objMensagem.salvaMensagem();
		}
		catch (final SkypeException ex) {
			JOptionPane.showMessageDialog(null, VERSION +  "\n  Exceção no Skype Listener. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, VERSION +  "\n  Exceção no Skype Listener. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (objMensagem != null)
				objMensagem = null;
		}

	}
	
}