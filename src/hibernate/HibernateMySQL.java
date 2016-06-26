package hibernate;

import javax.swing.JOptionPane;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import modal.Configuracao_Skype;

public class HibernateMySQL {
	
	private SessionFactory factory;
	private Configuracao_Skype objConfiguracao;
	
	public SessionFactory getFactory() { return factory; }
	public void setFactory(SessionFactory pfactory) { factory = pfactory; }	public Configuracao_Skype getObjConfiguracao() { return objConfiguracao; }
	public void setObjConfiguracao(Configuracao_Skype objConfiguracao) { this.objConfiguracao = objConfiguracao; }
	
	public boolean createFactory() {
		
		try {			
		        
			Configuration config = new Configuration();

			config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
			config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
			config.setProperty("hibernate.connection.url", "jdbc:mysql:" + objConfiguracao.getMySQLHost()); 
			config.setProperty("hibernate.connection.username", objConfiguracao.getMySqlRoot());
			config.setProperty("hibernate.connection.password", objConfiguracao.getMySqlPassWord());
			config.setProperty("hibernate.show_sql", "false");           
			config.setProperty("hibernate.format_sql","false");
			config.setProperty("hibernate.useSSL", "false");

			for (int index = 0; index < ClassMap.getMySQLClasses().length; index++)        
				config.addClass(ClassMap.getMySQLClasses()[index]);

		    factory = config.buildSessionFactory();
		    
		}
		catch (Exception ex) {
			setFactory(null);
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Exceção ao criar o MySQL SessionFactory: " + ex.getMessage());
			return false;
		}
		
		return (factory != null);
		
	}
		
	public void closeFactory() {
		if (factory != null) {
			if (! factory.isClosed())
				factory.close();
			factory = null;
		}
			
	}

}