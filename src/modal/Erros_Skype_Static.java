package modal;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;
import org.hibernate.SessionFactory;

public class Erros_Skype_Static {

	private static SessionFactory objSessionFactory;
	private static Configuracao_Skype objConfiguracao;
	
	public static SessionFactory getObjSessionFactory() { return objSessionFactory; }
	public static void setObjSessionFactory(SessionFactory varSessionFactory) { objSessionFactory = varSessionFactory; };	
	public static Configuracao_Skype getObjConfiguracao() { return objConfiguracao; }
	public static void setObjConfiguracao(Configuracao_Skype pobjConfiguracao) { objConfiguracao = pobjConfiguracao; }
	
	public static boolean salvaErroSkype(String errorContent) {
		
		boolean ok = false;
		
		Erros_Skype objErrosTemp = new Erros_Skype();
		try {

			objErrosTemp.setObjSessionFactory(objSessionFactory);

			long id = objErrosTemp.retornaUltimoID(objConfiguracao.getSkypeAccount());
			objErrosTemp.setId(++id);
			objErrosTemp.setAccount_name(objConfiguracao.getSkypeAccount());
			objErrosTemp.setContent(errorContent);
			objErrosTemp.setHost_name(InetAddress.getLocalHost().getHostName());
			objErrosTemp.setIp_adress(InetAddress.getLocalHost().getHostAddress());

			Date date = new Date() ;
	        Timestamp timestamp = new Timestamp(date.getTime());
			objErrosTemp.setError_date(timestamp);
			
			ok = objErrosTemp.salvaErroSkype();
			
		}
		catch (Exception ex) {

			System.out.println("Exceção ao Salvar o Erro na estação cliente. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
			return false;
			
		}
		finally {
			
			if (objErrosTemp != null)
				objErrosTemp = null;

		}
		
		return ok;
		
	}
	
}