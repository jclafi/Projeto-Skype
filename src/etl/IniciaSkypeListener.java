package etl;

import javax.swing.JOptionPane;
import org.hibernate.SessionFactory;
import com.skype.*;
import jdbc.SqlLiteConnection;
import modal.Usuario_Logado;

public class IniciaSkypeListener extends Thread {
	
	//Entre uma carga e outra aguarda 5 minutos
	private final long SLEEP_TIME = 300000;
	private SessionFactory objSessionFactory;
	private SqlLiteConnection connectionSQLLite;
	private Usuario_Logado objUsuarioRegras;
	
	public SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public void setObjSessionFactory(SessionFactory varSessionFactory) { this.objSessionFactory = varSessionFactory; };	
	public SqlLiteConnection getConnectionSQLLite() { return connectionSQLLite; }
	public void setConnectionSQLLite(SqlLiteConnection connectionSQLLite) { this.connectionSQLLite = connectionSQLLite; }	
	public Usuario_Logado getUsuarioRegras() { return objUsuarioRegras; }
	public void setUsuarioRegras(Usuario_Logado objSkypeUser) { this.objUsuarioRegras = objSkypeUser; }
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		objUsuarioRegras = carregaUsuario();
		
		//Valida se o Operador conectou no Skype e Cria o Listener que grava Mensagens enviadas/recebidas
		if ((objUsuarioRegras != null) && (connectSkype()))
			startChatListener();
	}	
	
	private Usuario_Logado carregaUsuario() {
		
		Usuario_Logado objUser = new Usuario_Logado();
		
		objUser.setConnectionSQLLite(connectionSQLLite);
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

				//Pausa para a nova carga de Mensagens
				try {

					IniciaSkypeListener.sleep(SLEEP_TIME);
				
				} catch (InterruptedException e) {
					e.printStackTrace();
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
			
			//Objeto com dados do Usuário Skype
			skypeListener.setUsuarioRegras(objUsuarioRegras);
			
			//Objeto Session Factory Hibernate
			skypeListener.setObjSessionFactory(objSessionFactory);
			
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