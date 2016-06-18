package hibernate;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernatePostgreSQL {
	
	private SessionFactory factory;	
	
	public SessionFactory getFactory() { return factory; }
	public void setFactory(SessionFactory pfactory) { factory = pfactory; }
	
	public boolean createFactory() {
		
		try {			
		        
			Configuration config = new Configuration();

			config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
			config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
			config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/skypedb"); 
			config.setProperty("hibernate.connection.username", "postgres");
			config.setProperty("hibernate.connection.password", "postgres");
			config.setProperty("hibernate.show_sql", "false");           

			for (int index = 0; index < ClassMap.getPostgreSQLClasses().length; index++)        
				config.addClass(ClassMap.getPostgreSQLClasses()[index]);

		    factory = config.buildSessionFactory();
		    
		}
		catch (HibernateException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Exceção ao criar o PostgreSQL SessionFactory: " + ex.getMessage());
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