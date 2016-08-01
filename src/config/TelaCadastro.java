package config;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import controller.DefineEstruturaProjeto;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.concurrent.TimeUnit;

import modal.Conta_Login;
import modal.Erros_Skype_Static;

public class TelaCadastro extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane = new JPanel();
	private JTextField edtContaSkype;
	private JTextField edtSkypeDataBase;
	private JTextField edtServerRoot;
	private JTextField edtServerHost;
	private JPasswordField edtServerPassWord;
	private JCheckBox chkListener;
	private JButton btnPararServico;	
	private DefineEstruturaProjeto objEstruturaRegras;
	private final long TEMPO_SEGUNDOS = 10;	
	
	public DefineEstruturaProjeto getObjEstruturaRegras() { return objEstruturaRegras; }
	public void setObjEstruturaRegras(DefineEstruturaProjeto objEstruturaRegras) { this.objEstruturaRegras = objEstruturaRegras; }

	public TelaCadastro() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JPanel(), BorderLayout.NORTH);
		getContentPane().add(montaDados(), BorderLayout.CENTER);
		getContentPane().add(montaBotoes(), BorderLayout.SOUTH);
		
		setTitle("Cadastro de Configura\u00E7\u00F5es ETL");
		setAlwaysOnTop(true);
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 550, 250);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		toFront();
		edtServerHost.grabFocus();
	}

	private JPanel montaDados() {

		contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(6, 2));

		JLabel lblServerHost = new JLabel("Host Servidor MySQL:");
		lblServerHost.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerHost.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(lblServerHost);
		edtServerHost = new JTextField("", 80);
		edtServerHost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		contentPane.add(edtServerHost);
		
		JLabel lblServerUser = new JLabel("Usu\u00E1rio Servidor MySQL:");
		lblServerUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerUser.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(lblServerUser);
		edtServerRoot = new JTextField("", 80);
		edtServerRoot.setFont(new Font("Tahoma", Font.PLAIN, 18));
		contentPane.add(edtServerRoot);

		JLabel lblServerSenha = new JLabel("Senha Servidor MySQL:");
		lblServerSenha.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerSenha.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(lblServerSenha);
		edtServerPassWord = new JPasswordField("", 30);
		edtServerPassWord.setEchoChar('*');
		edtServerPassWord.setFont(new Font("Tahoma", Font.PLAIN, 18));
		contentPane.add(edtServerPassWord);
		
		JLabel lblSkypeDataBase = new JLabel("Banco Skype SQL Lite:");
		lblSkypeDataBase.setHorizontalAlignment(SwingConstants.CENTER);
		lblSkypeDataBase.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(lblSkypeDataBase);
		edtSkypeDataBase = new JTextField("", 20);
		edtSkypeDataBase.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
			
				if ((! edtSkypeDataBase.getText().isEmpty()) && 
					(edtContaSkype.getText().isEmpty()))
					selecionaSkypeUser();
			
			}
			@Override
			public void focusGained(FocusEvent arg0) {
			
				if (edtSkypeDataBase.getText().isEmpty())
					selecionaSkypeBD();
			
			}
		});
		edtSkypeDataBase.setFont(new Font("Tahoma", Font.PLAIN, 18));
		contentPane.add(edtSkypeDataBase);		
		
		JLabel lblContaSkype = new JLabel("Conta Login Skype:");
		lblContaSkype.setHorizontalAlignment(SwingConstants.CENTER);
		lblContaSkype.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(lblContaSkype);
		edtContaSkype = new JTextField("", 20);
		edtContaSkype.setFont(new Font("Tahoma", Font.PLAIN, 18));
		contentPane.add(edtContaSkype);

		JLabel lblSkypeListener = new JLabel("Ativa Skype Listener:");
		lblSkypeListener.setHorizontalAlignment(SwingConstants.CENTER);
		lblSkypeListener.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(lblSkypeListener);
		chkListener = new JCheckBox("", false);
		chkListener.setFont(new Font("Tahoma", Font.PLAIN, 18));
		chkListener.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(chkListener);
		
		return contentPane;
	}

	private JPanel montaBotoes() {

		contentPane = new JPanel();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton button = new JButton("Salvar");
		button.setToolTipText("Salva Configura\u00E7\u00F5es");
		button.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!validaConfiguracao()) {
					JOptionPane.showMessageDialog(null, "Atenção complete os dados da Configuração!");
					edtServerHost.grabFocus();
				} else
					salvaConfiguracoes();
			}
		});
		contentPane.add(button);

		JButton btnBdSkype = new JButton("Skype");
		btnBdSkype.setToolTipText("Testa Conex\u00E3o Local");
		btnBdSkype.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnBdSkype.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				conectaBaseSkype();

			}
		});
		contentPane.add(btnBdSkype);

		JButton btnBdServidor = new JButton("Servidor");
		btnBdServidor.setToolTipText("Testa Conex\u00E3o Servidor");
		btnBdServidor.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnBdServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				conectaBaseServidor();
				
			}
		});
		contentPane.add(btnBdServidor);
		
		btnPararServico = new JButton("Parar ETL");
		btnPararServico.setToolTipText("Para o Serviço do ETL");
		btnPararServico.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPararServico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				finalizaServicoETL();
			
			}
		});
		contentPane.add(btnPararServico);
		
		return contentPane;

	}

	@SuppressWarnings("deprecation")
	private boolean validaConfiguracao() {
		
		boolean ok = false; 

		ok = ((!edtContaSkype.getText().isEmpty()) && 
			  (!edtSkypeDataBase.getText().isEmpty()) && 
			  (!edtServerHost.getText().isEmpty()) && 
			  (!edtServerRoot.getText().isEmpty()) && 
			  (!edtServerPassWord.getText().isEmpty()));

		if (! ok) 
			JOptionPane.showMessageDialog(this, "Atenção verifique os dados da Configuração Skype!");
					
		return ok;
	}

	@SuppressWarnings("deprecation")
	private void salvaConfiguracoes() {
		
		objEstruturaRegras.getObjConfiguracao().setId_geral(objEstruturaRegras.getObjConfiguracao().getCODIGO_CONFIGURACAO());
		objEstruturaRegras.getObjConfiguracao().setMySQLHost(edtServerHost.getText());
		objEstruturaRegras.getObjConfiguracao().setMySqlPassWord(edtServerPassWord.getText());
		objEstruturaRegras.getObjConfiguracao().setMySqlRoot(edtServerRoot.getText());
		objEstruturaRegras.getObjConfiguracao().setSkypeAccount(edtContaSkype.getText());
		objEstruturaRegras.getObjConfiguracao().setSkypeDatabase(edtSkypeDataBase.getText());
		
		if (chkListener.isSelected())
			objEstruturaRegras.getObjConfiguracao().setSkypeListener("S");
		else
			objEstruturaRegras.getObjConfiguracao().setSkypeListener("N");
	
		if (! objEstruturaRegras.getObjConfiguracao().salvaConfiguracao())
			JOptionPane.showMessageDialog(this, "Atenção não foi possível Salvar os Dados do Skype !");			
		else
			JOptionPane.showMessageDialog(this, "Dados do Skype Salvos com Sucesso !");
			
	}

	public void carregaConfiguracoes() {
		
		edtContaSkype.setText(objEstruturaRegras.getObjConfiguracao().getSkypeAccount().toString());
		edtSkypeDataBase.setText(objEstruturaRegras.getObjConfiguracao().getSkypeDatabase().toString());
		edtSkypeDataBase.setToolTipText(edtSkypeDataBase.getText().toString());
		edtServerHost.setText(objEstruturaRegras.getObjConfiguracao().getMySQLHost());
		edtServerRoot.setText(objEstruturaRegras.getObjConfiguracao().getMySqlRoot());
		edtServerPassWord.setText(objEstruturaRegras.getObjConfiguracao().getMySqlPassWord());
		chkListener.setSelected(objEstruturaRegras.getObjConfiguracao().getSkypeListener().equals("S"));
					
	}
	
	private void selecionaSkypeBD() {
		
		final JFileChooser file = new JFileChooser();
		int opcao = file.showOpenDialog(this); 
		
		if (opcao == JFileChooser.APPROVE_OPTION) { 
					
			edtSkypeDataBase.setText(file.getSelectedFile().getPath());
			edtSkypeDataBase.setToolTipText(edtSkypeDataBase.getText().toString());
			
		}
		else
			JOptionPane.showMessageDialog(this, "Atenção não foi definido o Banco de Dados do Skype");
		
	}
	
	private void selecionaSkypeUser() {
		
		objEstruturaRegras.getObjConfiguracao().setSkypeDatabase(edtSkypeDataBase.getText());
		
		if (objEstruturaRegras.connectSQLLiteJDBC()) {
			
			Conta_Login objPersistente = new Conta_Login();
			try {
				
				objPersistente.setConnectionSQLLite(objEstruturaRegras.getConnectionSQLLite());
				objPersistente.carregaContaLogin();
				
				edtContaSkype.setText(objPersistente.getSigninName());
				
			}
			finally {
				
				if (objPersistente != null)
					objPersistente = null;
				
			}
			
		}
		else {
			edtContaSkype.setText("");
			edtSkypeDataBase.setText("");
			JOptionPane.showMessageDialog(this, "Atenção não foi possível conectar na Base SQL Lite do Skype!");
			edtServerHost.grabFocus();
			objEstruturaRegras.closeSQLLiteJDBC();
		}

	}
		
	private void conectaBaseSkype() {
		
		if (objEstruturaRegras.connectSQLLiteJDBC()) {
			
			objEstruturaRegras.closeSQLLiteJDBC();
			JOptionPane.showMessageDialog(this, "Conexão realizada com Sucesso !");
			
		}
		else
		{

			objEstruturaRegras.closeSQLLiteJDBC();
			JOptionPane.showMessageDialog(this, "Falha ao realizar a Conexão !");
			
		}
		
	}
	
	private void conectaBaseServidor() {
		
		if (objEstruturaRegras.connectMySQLHibernate()) {
			
			objEstruturaRegras.closeMySQLHibernate();
			JOptionPane.showMessageDialog(this, "Conexão realizada com Sucesso !");			
			
		}
		else
		{

			objEstruturaRegras.closeMySQLHibernate();
			JOptionPane.showMessageDialog(this, "Falha a realizar a Conexão !");			
			
		}
		
	}
	 
	private void finalizaServicoETL() {
		
		btnPararServico.setEnabled(false);
		try {
			//Verifica se o sistema está sendo executado
			if (objEstruturaRegras.getObjConfiguracao().carregaConfiguracao(objEstruturaRegras.getObjConfiguracao().getCODIGO_CONFIGURACAO())) {
				
				if (objEstruturaRegras.getObjConfiguracao().getStatusListener().equals("E")) {
					
					objEstruturaRegras.getObjConfiguracao().defineFlagExecucao('F');
					while (! objEstruturaRegras.getObjConfiguracao().getStatusListener().toString().equals("P")) {
						
						try {
							TimeUnit.SECONDS.sleep(TEMPO_SEGUNDOS);
						} catch (InterruptedException ex) {
							Erros_Skype_Static.salvaErroSkype("Interrupted Exception no sleep Login Skype: " + ex.getMessage());					
						}
						
						objEstruturaRegras.getObjConfiguracao().carregaConfiguracao(objEstruturaRegras.getObjConfiguracao().getCODIGO_CONFIGURACAO());

					}
					
					JOptionPane.showMessageDialog(this, "Serviço Finalizado !");				
				}
				else
					JOptionPane.showMessageDialog(this, 
							"O Serviço não está em execução. Status atual: " + (
									objEstruturaRegras.getObjConfiguracao().getStatusListener().toString().equals("P") ? "Parado" : "Finalizando"));
				
			}
			else {
				
				JOptionPane.showMessageDialog(this, "Não foi possível carregar a Configuração do Sistema !");
				
			}
		}
		finally {
			
			btnPararServico.setEnabled(true);
			
		}
	}
	
}