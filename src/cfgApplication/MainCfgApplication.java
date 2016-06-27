package cfgApplication;

import javax.swing.JOptionPane;
import controller.DefineEstruturaProjeto;

public class MainCfgApplication {

	public static void main(String[] args) {

		DefineEstruturaProjeto objEstruturaSkype = new DefineEstruturaProjeto();
		
		//Verifica se o Skype está instalado na máquina Cliente
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
						"Exceção ao Executar o Login no Sistema. Mensagem: " + ex.getMessage());
				ex.printStackTrace();
			} finally {
				if (frmTelaLogin != null) 
					frmTelaLogin.dispose();
			}
			
			//Se o login falhou aborta a aplicação, caso contrário inicia a tela de Config			
			if (! LoginOk)
				
				objEstruturaSkype.finalizaAplicacao();
	
			else {
				
				//Abre a tela de Configuração do Aplicativo
				
			}

		} else {

			JOptionPane.showMessageDialog(null, "Atenção não foi possível Conectar na Base de dados Local !");
			objEstruturaSkype.finalizaAplicacao();

		}

	}

}