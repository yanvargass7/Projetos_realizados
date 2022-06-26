package br.com.framework.implementacao.crud;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.impl.SessionFactoryObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.interfac.crud.InterfaceCrud;

@Component
@Transactional
public class ImplementacaoCrud<T> implements InterfaceCrud<T> {
	
	private static final long serialVersionUID = 1L;
	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SimpleJdbcTemplateImpl simpleJdbcTemplate;
	
	@Autowired
	private SimpleJdbcInsertImplents simpleJdbcinsert;
	
	@Autowired
	private SimpleJdbcTemplateImpl simpleJdbcTemplateImpl;
	
	
	public SimpleJdbcTemplate getSimpleJdbcTemplateImpl() {
		return simpleJdbcTemplateImpl;
	}
	
	@Override
	public void save(T obj) throws Exception {
		
		validaSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlushSession();
	}

	@Override
	public void persist(T obj) throws Exception {
		
		validaSessionFactory();
		sessionFactory.getCurrentSession().persist(obj);
		executeFlushSession();
		
	}

	@Override
	public void saveOurUpdate(T obj) throws Exception {
		
		validaSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdateCopy(obj);
		executeFlushSession();
	}

	@Override
	public void update(T obj) throws Exception {
		
		validaSessionFactory();
		sessionFactory.getCurrentSession().update(obj);
		executeFlushSession();
	}

	@Override
	public void delete(T obj) throws Exception {
		
		validaSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlushSession();
		
	}

	@Override
	public T merge(T obj) throws Exception {
		
		validaSessionFactory();
		obj = (T) sessionFactory.getCurrentSession().merge(obj);
		executeFlushSession();
		return obj;
	}

	@Override
	public List<T> findList(Class<T> entidade) throws Exception {
		
		validaSessionFactory();
		StringBuilder query = new StringBuilder();
		query.append(" select distinct(entity) from ").append(entidade.getSimpleName()).append(" entity ");
		
		List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		
		return lista;
	}

	@Override
	public Object findById(Class<T> entidade, Long id) throws Exception {
		
		validaSessionFactory();
		Object obj = sessionFactory.getCurrentSession().load(getClass(), id);
		return obj;
	}

	@Override
	public T findPorId(Class<T> entidade, Long id) throws Exception {
		
		validaSessionFactory();
		T obj = (T)sessionFactory.getCurrentSession().load(getClass(), id);
		return obj;
	}

	@Override
	public List<T> findListByQueryDinamica(String s) throws Exception {
		
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(s).list();
		return lista;
	}

	@Override
	public void executaUpdateQueryDinamica(String s) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executaUpdateSQLDinamica(String s) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearSession() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evict(Object objs) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Session getSession() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> getListSQLDinamica(String sql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		return simpleJdbcinsert;
	}

	@Override
	public Long totalRegistro(String table) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query obterQuery(String query) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultaado) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void validaSessionFactory() {
		if (sessionFactory==null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}
		validaTransaction();
	}
	
	private void validaTransaction() {
		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}
	
	private void commitProcessoAjax(){
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}
	
	private void rollBackProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().rollback();
	}
	
	/*
	 * Roda instantaneamente o SQL no banco de dados
	 */
	private void executeFlushSession() {
		sessionFactory.getCurrentSession().flush();
		
	}

}
