package cfgApplication;

import javax.swing.JOptionPane;
import controller.DefineEstruturaProjeto;

public class MainCfgApplication {

	public static void main(String[] args) {

		DefineEstruturaProjeto objEstruturaSkype = new DefineEstruturaProjeto();
		
		//Verifica se o Skype est� instalado na m�quina Cliente
		if (!objEstruturaSkype.verificaInstalacaoSkype())
			objEstruturaSkype.finalizaAplicacao();

		// Cria a Factory de acesso a Dados
		if (objEstruturaSkype.connectPostgreSQLHibernate()) {

			boolean LoginOk = false;

			TelaLogin frmTelaLogin = new TelaLogin();
			try {

				frmTelaLogin.setVisible(true);
				LoginOk = frmTelaLogin.isLoginOk();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Exce��o ao Executar o Login no Sistema. Mensagem: " + ex.getMessage());
				ex.printStackTrace();
			} finally {
				if (frmTelaLogin != null) 
					frmTelaLogin.dispose();
			}
			
			//Se o login falhou aborta a aplica��o, caso contr�rio inicia a tela de Config			
			if (! LoginOk)
				
				objEstruturaSkype.finalizaAplicacao();
	
			else {
				
				//Abre a tela de Configura��o do Aplicativo
				
			}

		} else {

			JOptionPane.showMessageDialog(null, "Aten��o n�o foi poss�vel Conectar na Base de dados Local !");
			objEstruturaSkype.finalizaAplicacao();

		}

	}

}