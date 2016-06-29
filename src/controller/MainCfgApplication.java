package controller;

import javax.swing.JOptionPane;

import config.TelaCadastro;
import config.TelaLogin;
import modal.Erros_Skype_Static;

public class MainCfgApplication {

	public static void main(String[] args) {

		DefineEstruturaProjeto objEstruturaSkype = new DefineEstruturaProjeto();
		try {
						
			//Verifica se o Skype est� instalado na m�quina Cliente
			if (!objEstruturaSkype.verificaInstalacaoSkype())
				objEstruturaSkype.finalizaAplicacao();
	
			// Cria a Factory de acesso a Dados
			if (objEstruturaSkype.connectPostgreSQLHibernate()) {
				
				//Define a conex�o local para o objeto de Log de Erros
				Erros_Skype_Static.setObjSessionFactory(objEstruturaSkype.getObjPostgreSQLFactory().getFactory());
				Erros_Skype_Static.setObjConfiguracao(objEstruturaSkype.getObjConfiguracao());
				
				//Cria o objeto de configura��o Base de Dados
				if (! objEstruturaSkype.criaObjetoConfiguracao())
					JOptionPane.showMessageDialog(null, "Aten��o n�o foi poss�vel criar o objeto de Configura��o !");
				else {									
					//Se o login falhou aborta a aplica��o, caso contr�rio inicia a tela de Config			
					//Abre a tela de Configura��o do Aplicativo
					if (validaLogin()) 
						cadastraConfiguracao(objEstruturaSkype);
				}
			} else 
				JOptionPane.showMessageDialog(null, "Aten��o n�o foi poss�vel Conectar na Base de dados Local !");
		}
		finally {

			if (objEstruturaSkype != null) {
				objEstruturaSkype.finalizaAplicacao();

			}		

		}

	}
	
	public static boolean validaLogin() {
		
		boolean LoginOk = false;

		TelaLogin frmTelaLogin = new TelaLogin();
		try {

			frmTelaLogin.setVisible(true);
			LoginOk = frmTelaLogin.isLoginOk();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,	"Exce��o ao Executar o Login no Sistema. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (frmTelaLogin != null) 
				frmTelaLogin.dispose();
		}
		
		return LoginOk;
		
	}
	
	public static void cadastraConfiguracao(DefineEstruturaProjeto objTemp) {
		
		TelaCadastro frmCadastro = new TelaCadastro();
		try {

			frmCadastro.setObjEstruturaRegras(objTemp);
			frmCadastro.carregaConfiguracoes();
			frmCadastro.setVisible(true);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,	"Exce��o ao Executar o Cadastro de Configura��o. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (frmCadastro != null) {
				frmCadastro.dispose();
			}
		}
		
	}

}