package dataBase;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Hibernate {
	
	private static SessionFactory factory;	
	
	public static SessionFactory getFactory() { return factory; }
	public static void setFactory(SessionFactory pfactory) { factory = pfactory; }
	
	public Hibernate() {
		
				
	}
	
	public static boolean createFactory() {
		
		try {
			
			factory = new Configuration().configure().buildSessionFactory();
			
		}
		catch (HibernateException ex) {
			System.out.println("Exce��o ao criar a Sessionfactory: " + ex.getMessage());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Exce��o na cria��o da SessionFactory: " + ex.getMessage());
			return false;
		}
		
		return true;
		
	}
		
	public void finalize() {
		System.out.println("M�todo Finalize!");
		if (factory != null) {
			if (! factory.isClosed())
				factory.close();
			factory = null;
		}
			
	}

}