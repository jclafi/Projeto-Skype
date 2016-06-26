package etl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import modal.Erros_Skype;
import org.hibernate.SessionFactory;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;
import modal.Mensagens_Skype;
import modal.Conta_Login;
import modal.Contas_Skype;
import modal.Contatos_Contas_Skype;

public class EtlListener implements ChatMessageListener {
	
	private final String VERSION = "Versão Skype Homologada: 7.24.0.104";
	private SessionFactory objSessionFactory;	
	private Conta_Login objUsuarioRegras;
	private Contas_Skype objContasSkype;
	private Set<Contatos_Contas_Skype> objListaContatosContaSkype = new HashSet<Contatos_Contas_Skype>();

	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public Conta_Login getUsuarioRegras() { return objUsuarioRegras; }
	public void setUsuarioRegras(Conta_Login objSkypeUser) { this.objUsuarioRegras = objSkypeUser; }
	public Contas_Skype getObjContasSkype() { return objContasSkype; }
	public void setObjContasSkype(Contas_Skype objContasSkype) { this.objContasSkype = objContasSkype; }
	public Set<Contatos_Contas_Skype> getObjListaContatosContaSkype() { return objListaContatosContaSkype; }
	public void setObjListaContatosContaSkype(Set<Contatos_Contas_Skype> objListaContatosContaSkype) { this.objListaContatosContaSkype = objListaContatosContaSkype; }
	
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
					
			//Verifica se o Contato da mensagem recebida está autorizado
			objMensagem.setContact_verified("N");
			for (Contatos_Contas_Skype index : objListaContatosContaSkype) {
				
				if (index.getAccount_name().equals(recMessage.getSenderId().toString())) {
					objMensagem.setContact_verified(index.getContact_verified());
					break;
				}
				
			}
			objMensagem.setAccount_verified("*");
			
			if (! objMensagem.salvaMensagem())
				Erros_Skype.salvaErroSkype(VERSION +  "\n  Falha ao inserir a Mensagem via Listener!");
				
		}
		catch (final SkypeException ex) {
			Erros_Skype.salvaErroSkype(VERSION +  "\n  Exceção no Skype Listener. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			Erros_Skype.salvaErroSkype(VERSION +  "\n  Exceção no Skype Listener. Mensagem: " + ex.getMessage());
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

			//Verifica se Conta padrão do Sistema está autorizada			
			if (objUsuarioRegras.getSigninName().equals(sentMessage.getSenderId().toString()))			
				objMensagem.setAccount_verified(objContasSkype.getAccount_verified());
			else
				objMensagem.setAccount_verified("N");
			objMensagem.setContact_verified("*");			
			
			if (! objMensagem.salvaMensagem())
				Erros_Skype.salvaErroSkype(VERSION +  "\n  Falha ao inserir a Mensagem via Listener!");

		}
		catch (final SkypeException ex) {
			Erros_Skype.salvaErroSkype(VERSION +  "\n  Exceção no Skype Listener. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			Erros_Skype.salvaErroSkype(VERSION +  "\n  Exceção no Listener. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (objMensagem != null)
				objMensagem = null;
		}

	}
	
}