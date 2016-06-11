package hibernate;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Hibernate {
	
	private static SessionFactory factory;	
	
	public static SessionFactory getFactory() { return factory; }
	public static void setFactory(SessionFactory pfactory) { factory = pfactory; }
	
	public static boolean createFactory() {
		
		try {
			
			factory = new Configuration().configure().buildSessionFactory();
			
		}
		catch (HibernateException ex) {
			System.out.println("Exceção ao criar a Sessionfactory: " + ex.getMessage());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Exceção na criação da SessionFactory: " + ex.getMessage());
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