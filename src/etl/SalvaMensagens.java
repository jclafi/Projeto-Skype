package etl;

import javax.swing.JOptionPane;

import modal.UsuarioLogado;

public class SalvaMensagens {
	
	private UsuarioLogado objUsuarioRegras; 
		
	public UsuarioLogado getObjUsuario() { return objUsuarioRegras; }
	public void setObjUsuario(UsuarioLogado objUsuario) { this.objUsuarioRegras = objUsuario; }

	public void executaCargaMensagens() {
		
		//Identifica o Usuario logado no Sistema
		objUsuarioRegras = carregaUsuario();
		
		if (objUsuarioRegras != null) 
			executaETL(objUsuarioRegras);
		else
			JOptionPane.showMessageDialog(null, "Atenção falha ao validar o Usuário Logado no Skype !");
		
	}
	
	private void executaETL(UsuarioLogado varUsuarioRegras) {
	
		
		
	}
	
	private UsuarioLogado carregaUsuario() {
		
		UsuarioLogado objUser = new UsuarioLogado();
		
		objUser.getUsuarioLogado();
		
		return objUser;
		
	}
	
	public void finalize() {
		
		if (objUsuarioRegras != null)
			objUsuarioRegras = null;
		
	}


}