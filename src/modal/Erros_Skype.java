package modal;

import java.net.InetAddress;
import java.util.Date;
import org.hibernate.SessionFactory;

public class Erros_Skype {

	private static int id_geral;
	private static String content;
	private static String ip_adress;
	private static String host_name;
	private static Date error_date;
	private static SessionFactory objPostgreSQLFactory;
	
	public static int getId_geral() { return id_geral; }
	public static void setId_geral(int pid_geral) { id_geral = pid_geral; }
	public static String getContent() { return content; }
	public static void setContent(String pcontent) { content = pcontent; }
	public static String getIp_adress() { return ip_adress; }
	public static void setIp_adress(String pip_adress) { ip_adress = pip_adress; }
	public static String getHost_name() { return host_name; }
	public static void setHost_name(String phost_name) { host_name = phost_name; }
	public static Date getError_date() { return error_date; }
	public static void setError_date(Date perror_date) { error_date = perror_date; }
	public static SessionFactory getObjPostgreSQLFactory() { return objPostgreSQLFactory; }
	public static void setObjPostgreSQLFactory(SessionFactory varSessionFactory) { objPostgreSQLFactory = varSessionFactory; };	

	@SuppressWarnings("static-access")
	public static void salvaErroSkype(String errorContent) {
		
		Erros_Skype_Dao objPersistente = new Erros_Skype_Dao();
		Erros_Skype objErrosTemp = new Erros_Skype();
		try {
						
			objErrosTemp.setContent(getContent());
			objErrosTemp.setError_date(new Date());
			objErrosTemp.setHost_name(InetAddress.getLocalHost().getHostName());
			objErrosTemp.setIp_adress(InetAddress.getLocalHost().getHostAddress());
		
			objPersistente.setObjSessionFactory(objPostgreSQLFactory);
			objPersistente.setObjErros_Skype(objErrosTemp);
			
			objPersistente.salvaConta();
				
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