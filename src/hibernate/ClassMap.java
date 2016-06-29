package hibernate;

import modal.Configuracao_Skype;
import modal.Mensagens_Skype;
import modal.Contas_Skype;
import modal.Contatos_Contas_Skype;
import modal.Erros_Skype_Static;

public class ClassMap {

	@SuppressWarnings("rawtypes")
	public static Class[] getPostgreSQLClasses() {

		Class[] lista = {Configuracao_Skype.class, 
						 Mensagens_Skype.class,
						 Contas_Skype.class,
						 Contatos_Contas_Skype.class,
						 Erros_Skype_Static.class};

		return lista;
	}
	
	@SuppressWarnings("rawtypes")
	public static Class[] getMySQLClasses() {

		Class[] lista = {Mensagens_Skype.class,
						 Contas_Skype.class,
						 Contatos_Contas_Skype.class,
						 Erros_Skype_Static.class};

		return lista;
	}

}