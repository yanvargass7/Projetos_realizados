package br.com.framework.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framework.implementacao.crud.VariavelConexaoUtil;
//test
/**
 * Responsavel por estabelecer conexao com hibernate
 * @author Yan Vargas
 */

@ApplicationScoped
public class HibernateUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE="java:/comp/env/jdbc/datasource";
	
	private static SessionFactory sessionFactory = buildSessionFactory();
	
	/**
	 * Responsavel por ler o arquivo de configura��o hibernate.cfg.xml
	 * @return
	 */
	private static SessionFactory buildSessionFactory() {
		try {
			if (sessionFactory == null) {
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}
			
			return sessionFactory;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro ao criar conex�o SessionFactory ");
		}
	}
	
	/**
	 * Retorna SessionFactory corrente
	 * @return SessionFactory
	 */

	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
	/**
	 * Retorna sess�o do SessionFactory
	 * @return Session
	 */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}
	
	/**
	 * Abre uma nova sess�o no SessionFactory
	 * @return Session
	 */
	public static Session openSession() {
		if (sessionFactory == null) {
			buildSessionFactory();
		}
		return sessionFactory.openSession();
	}
	
	/**
	 * Obtem a connection do provedor de conex�o configurado
	 * @return Connection SQL
	 * @throws SQLException
	 */
	public static Connection getConnectionProvider() throws SQLException{
		
		return ((SessionFactoryImplementor) sessionFactory).getConnectionProvider().getConnection();
	}
	
	/**
	 * Connection no InitialContext JAVA_COMP_ENV_JDBC_DATA_SOURCE
	 * @return Connetion
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception{
		InitialContext context = new InitialContext();
			DataSource ds = (DataSource) context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
			return ds.getConnection();
	}
	
	/**
	 * Return DataSource JNDI Tomcat
	 * @return
	 * @throws NamingException
	 */
	public DataSource getDataSourceJndi() throws NamingException {
		InitialContext context = new InitialContext();
		return (DataSource)context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_DATA_SOURCE);
	}
	
	
}
