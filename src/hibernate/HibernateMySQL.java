package hibernate;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateMySQL {
	
	private static SessionFactory factory;	
	
	public static SessionFactory getFactory() { return factory; }
	public static void setFactory(SessionFactory pfactory) { factory = pfactory; }
	
	public static boolean createFactory() {
		
		try {			
		        
			Configuration config = new Configuration();

			config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
			config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
			config.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/skypedb"); 
			config.setProperty("hibernate.connection.username", "root");
			config.setProperty("hibernate.connection.password", "mysql");
			config.setProperty("hibernate.show_sql", "false");           
			config.setProperty("hibernate.format_sql","false");

			for (int index = 0; index < ClassMap.getMySQLClasses().length; index++)        
				config.addClass(ClassMap.getMySQLClasses()[index]);

		    factory = config.buildSessionFactory();
		    
		}
		catch (HibernateException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Exceção ao criar o PostgreSQL SessionFactory: " + ex.getMessage());
			return false;
		}
		
		return true;
		
	}
		
	public static void closeFactory() {
		if (factory != null) {
			if (! factory.isClosed())
				factory.close();
			factory = null;
		}
			
	}

}