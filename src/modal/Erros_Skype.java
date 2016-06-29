package modal;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;
import org.hibernate.SessionFactory;

public class Erros_Skype {

	private static int id_geral;
	private static String content;
	private static String ip_adress;
	private static String host_name;
	private static Timestamp error_date;
	private static String account_name;
	private static SessionFactory objPostgreSQLFactory;
	private static Configuracao_Skype objConfiguracao;
	
	public static int getId_geral() { return id_geral; }
	public static void setId_geral(int pid_geral) { id_geral = pid_geral; }
	public static String getContent() { return content; }
	public static void setContent(String pcontent) { content = pcontent; }
	public static String getIp_adress() { return ip_adress; }
	public static void setIp_adress(String pip_adress) { ip_adress = pip_adress; }
	public static String getHost_name() { return host_name; }
	public static void setHost_name(String phost_name) { host_name = phost_name; }
	public static Timestamp getError_date() { return error_date; }
	public static void setError_date(Timestamp perror_date) { error_date = perror_date; }
	public static String getAccount_name() { return account_name; }
	public static void setAccount_name(String account_name) { Erros_Skype.account_name = account_name; }
	public static SessionFactory getObjPostgreSQLFactory() { return objPostgreSQLFactory; }
	public static void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { objPostgreSQLFactory = varSessionFactory; };	
	public static Configuracao_Skype getObjConfiguracao() { return objConfiguracao; }
	public static void setObjConfiguracao(Configuracao_Skype pobjConfiguracao) { objConfiguracao = pobjConfiguracao; }
	
	@SuppressWarnings("static-access")
	public static void salvaErroSkype(String errorContent) {
		
		Erros_Skype_Dao objPersistente = new Erros_Skype_Dao();
		Erros_Skype objErrosTemp = new Erros_Skype();
		try {
						
			objErrosTemp.setAccount_name(objConfiguracao.getSkypeAccount());
			objErrosTemp.setContent(errorContent);
			Date date = new Date() ;
	        Timestamp timestamp = new Timestamp(date.getTime());
			objErrosTemp.setError_date(timestamp);
			objErrosTemp.setHost_name(InetAddress.getLocalHost().getHostName());
			objErrosTemp.setIp_adress(InetAddress.getLocalHost().getHostAddress());
		
			objPersistente.setObjSessionFactory(objPostgreSQLFactory);
			objPersistente.setObjErros_Skype(objErrosTemp);
			
			objPersistente.salvaErroSkype();
				
		}
		catch (Exception ex) {

			System.out.println("Exceção ao Salvar Dados PC estação cliente. Mensagem: " + ex.getMessage());
			ex.printStackTrace();
			
		}
		finally {
			
			if (objPersistente != null)
				objPersistente = null;

			if (objErrosTemp != null)
				objErrosTemp = null;

		}
		
	}
	
}