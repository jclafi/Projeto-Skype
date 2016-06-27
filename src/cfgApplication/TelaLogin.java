package cfgApplication;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TelaLogin extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean LoginOk;
	private JTextField editNmUser;
	private JPasswordField editSenha;

	public boolean isLoginOk() {
		return LoginOk;
	}

	public void setLoginOk(boolean loginOk) {
		LoginOk = loginOk;
	}

	public TelaLogin() {

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JPanel(), BorderLayout.NORTH);
		getContentPane().add(montaDados(), BorderLayout.CENTER);
		getContentPane().add(montaBotoes(), BorderLayout.SOUTH);

		setTitle("Login no Sistema");
		setAlwaysOnTop(true);
		setResizable(false);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setBounds(100, 100, 300, 140);
		setLoginOk(false);
		toFront();
	}

	private JPanel montaBotoes() {
		// Monta o painel de botões e define o seu Layout
		contentPane = new JPanel();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		// Adiciona os componentes no painel
		JButton button = new JButton("Entrar");
		button.setFont(new Font("Tahoma", Font.PLAIN, 18));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				realizaLogin();

			}
		});
		contentPane.add(button);

		JButton button_2 = new JButton("Sair");
		button_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLoginOk(false);
				setVisible(false);
			}
		});
		contentPane.add(button_2);

		return contentPane;
	}

	@SuppressWarnings("deprecation")
	private void realizaLogin() {
		if ((editNmUser.getText().isEmpty()) || (editSenha.getText().isEmpty()))
			return;

		if (validaLogin(editNmUser.getText(), editSenha.getText())) {
			setLoginOk(true);
			setVisible(false);
		} else {
			setLoginOk(false);
			JOptionPane.showMessageDialog(null, "Atenção Usuário/Senha inválidos !");
			editNmUser.grabFocus();
			editNmUser.selectAll();
		}

	}

	private JPanel montaDados() {

		contentPane = new JPanel();

		// Define o Layout
		contentPane.setLayout(new GridLayout(2, 2));

		// Cria os Edits para adicionar ao painel
		// Adiciona os Edit´s ao painel criando os respectivos labels
		JLabel label_1 = new JLabel("Usuário:");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(label_1);
		editNmUser = new JTextField();
		editNmUser.setToolTipText("Somente Usu\u00E1rios Ativos");
		editNmUser.setFont(new Font("Tahoma", Font.PLAIN, 18));
		editNmUser.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
			}
		});
		editNmUser.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (editNmUser.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Atenção Usuário Inválido !");
					editNmUser.grabFocus();
				}
			}
		});
		contentPane.add(editNmUser);

		JLabel label = new JLabel("Senha:");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(label);
		editSenha = new JPasswordField();
		editSenha.setFont(new Font("Tahoma", Font.PLAIN, 18));
		editSenha.setEchoChar('*');
		editSenha.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				arg0.setKeyChar(Character.toUpperCase(arg0.getKeyChar()));
			}
		});
		editSenha.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent arg0) {
				if ((!editNmUser.getText().isEmpty()) && (editSenha.getText().isEmpty())) {
					JOptionPane.showMessageDialog(null, "Atenção Senha Inválida !");
					editSenha.grabFocus();
				}
			}
		});
		contentPane.add(editSenha);

		return contentPane;
	}

	private boolean validaLogin(String user, String passWord) {

		if (user.equals("ADMIN") && passWord.equals("SKYPEAPP") ) {
			return true;
		} else
			return false;

	}

}