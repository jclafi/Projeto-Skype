package etl;

import modal.UsuarioLogado;
import modal.Mensagens_Skype;
import jdbc.SqlLiteConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class SalvaMensagens {
	
	private UsuarioLogado objUsuarioRegras; 
		
	public UsuarioLogado getObjUsuario() { return objUsuarioRegras; }
	public void setObjUsuario(UsuarioLogado objUsuario) { this.objUsuarioRegras = objUsuario; }

	public void executaCargaMensagens() {
		
		//Identifica o Usuario logado no Sistema
		objUsuarioRegras = carregaUsuario();
				
		if (objUsuarioRegras != null)
			executaETL();
		else
			JOptionPane.showMessageDialog(null, "Atenção falha ao validar o Usuário Logado no Skype !");
		
	}
	
	private void executaETL() {
		
		int ultimoID = 0;
	
		Mensagens_Skype objMensagensRegra = new Mensagens_Skype();
		try {
			
			//Identifica o último ID salvo na base de backup
			ultimoID = objMensagensRegra.retornaUltimoID();
			
			//Consulta na base do Skype as Mensagens lançadas 
			//com ID maior que i retornado acima
			String SQL = null;		
			ResultSet resultSet = null;
			PreparedStatement statement = null;			
			try {				
				
				SQL = " select id, chatname, author, from_dispname, body_xml " + 
						" from messages where body_xml is not null and id > ? order by timestamp ";
				
				statement = SqlLiteConnection.getConnection().prepareStatement(SQL);
				statement.setInt(1, ultimoID);
				resultSet = statement.executeQuery();
				
				while (resultSet.next()) {
					
					objMensagensRegra.setId(resultSet.getInt("id"));
					objMensagensRegra.setChat(resultSet.getString("chatname"));
					objMensagensRegra.setSender_display_name(resultSet.getString("from_dispname"));
					objMensagensRegra.setContent(resultSet.getString("body_xml"));
					objMensagensRegra.setId_sender(resultSet.getString("author"));
					
					//Identifica a origem das mensagens de acordo com a estação Cliente
					if (resultSet.getString("author").equals(objUsuarioRegras.getSigninName()))
						objMensagensRegra.setMessage_type("E");
					else
						objMensagensRegra.setMessage_type("R");
					
					objMensagensRegra.salvaMensagem();
				}
				
				if (! resultSet.isClosed())
					resultSet.close();
				
			}
			finally {				
				if (statement != null)
					statement = null;
				if (resultSet != null)
					resultSet = null;
			}			
			
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Exceção ao Importar Dados Skype. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (objMensagensRegra != null)
				objMensagensRegra = null;
		}
			
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