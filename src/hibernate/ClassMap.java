package hibernate;

import modal.Configuracao_Skype;
import modal.Mensagens_Skype;

public class ClassMap {

	@SuppressWarnings("rawtypes")
	public static Class[] getPostgreSQLClasses() {

		Class[] lista = { Configuracao_Skype.class, Mensagens_Skype.class };

		return lista;
	}
	
	@SuppressWarnings("rawtypes")
	public static Class[] getMySQLClasses() {

		Class[] lista = { Mensagens_Skype.class };

		return lista;
	}

}