package skypeMain;

import com.skype.Skype;
import com.skype.SkypeException;
import sourceClasses.SkypeListener;
import dataBase.Hibernate;
import javax.swing.JOptionPane;

public class MainSkypeClass {

	public static boolean connectHibernate() {

		return Hibernate.createFactory();

	}

	@SuppressWarnings("deprecation")
	public static void startListener() {

		// Tenta conectar no skype a cada 1 minuto
		SkypeListener skypeListener = new SkypeListener();

		try {
			if (!Skype.isInstalled()) {
				JOptionPane.showMessageDialog(null, "Atenção instalação do Skype não Identificada !");
				return;
			}
			if (Skype.isRunning()) {
				return;
			}

		} catch (SkypeException ex) {
			System.out.println("Permissão de Acesso solicitada !");
		}

		try {
			Skype.addChatMessageListener(skypeListener);
		} catch (SkypeException ex) {
			System.out.println("Listener do Skype criado !");
		}
		// To prevent exiting from this program
		Skype.setDeamon(false);

	}

	public static void main(String[] args) {

		// Cria a Factory de acesso a Dados
		// Cria o Listener que grava Mensagens enviadas/recebidas
		if (MainSkypeClass.connectHibernate()) {
			// Cria o listener que captura mensagens do Skype
			MainSkypeClass.startListener();

			// Valida os contatos cadastrados no skype
		}

	}

}