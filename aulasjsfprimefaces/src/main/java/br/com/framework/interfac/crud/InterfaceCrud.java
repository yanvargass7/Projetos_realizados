package br.com.framework.interfac.crud;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface para CRUD
 * @author Yan Vargas
 * @param Interface
 */

@Component
@Transactional
public interface InterfaceCrud<T> extends Serializable {

	//salva dados
	void save(T obj) throws Exception;
	
	void persist(T obj) throws Exception;
	
	//salva ou atualiza
	void saveOurUpdate(T obj) throws Exception;
	
	//realiza o update
	void update(T obj) throws Exception;
	
	//realiza o delete de dados
	void delete(T obj) throws Exception;
	
	//salva ou atualiza e retorna objeto
	T merge (T obj) throws Exception;
	
	//carrega lista de dados de determinada classe
    List<T> findList(Class<T> objs) throws Exception;
    
    Object findById(Class<T> entidade,Long id) throws Exception;
    
    T findPorId(Class<T> entidade,Long id) throws Exception;
	
	List<T> findListByQueryDinamica(String s) throws Exception;
	
	//executar update com HQL
	void executaUpdateQueryDinamica (String s) throws Exception;
	//executar update com SQL
	void executaUpdateSQLDinamica (String s) throws Exception;
	
	void clearSession ()throws Exception;
	
	void evict (Object objs) throws Exception;
	
	Session getSession() throws Exception;
	
	List<?> getListSQLDinamica(String sql) throws Exception;
	
	//JDBC Spring
	JdbcTemplate getJdbcTemplate();
	
	SimpleJdbcTemplate getSimpleJdbcTemplate();
	
	SimpleJdbcInsert getSimpleJdbcInsert();
	
	Long totalRegistro(String table) throws Exception;
	
	Query obterQuery(String query) throws Exception;
	
	//carregamento dinamico
	List<T> findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultaado);
	
	
	
	
}
