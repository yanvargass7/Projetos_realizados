package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import posjavamavenhibernate.HibernateUtil;

public class DaoGeneric <E> { // declara entidade
	
	private EntityManager entityManager = HibernateUtil.getEntityManager();
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	//SALVAR
	public void salvar (E entidade) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
		
	}
	
	//UPDATE - SALVA OU ATUALIZA
	public E updateMerge (E entidade) {
			
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			E entidadeSalva = entityManager.merge(entidade);
			transaction.commit();
			
			return entidadeSalva;
		}
	
	//PESQUISAR
	public E pesquisar(E entidade) {
		
		Object id = HibernateUtil.getPrimaryKey(entidade);
		E e = (E) entityManager.find(entidade.getClass(), id);
		
		return e;
	}
	
	//PESQUISAR
	public E pesquisar(Long id, Class<E> entidade) {
			
			E e = (E) entityManager.find(entidade, id);
			
			return e;
		}
		
	//DELETAR	
	public void deletarPorId(E entidade)	{
		
		Object id = HibernateUtil.getPrimaryKey(entidade);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.createNativeQuery("delete from "+entidade.getClass().getSimpleName().toLowerCase()+" where id = "+ id).executeUpdate();
		
		transaction.commit();
	}
		
	//EXIBIR LISTA
	public List<E> listar(Class<E> entidade){
	EntityTransaction transaction = entityManager.getTransaction(); //start transação
	transaction.begin(); // inicia processo
		
	List<E> lista = entityManager.createQuery("from "+ entidade.getName()).getResultList();
	transaction.commit();
	return lista;
	
	}
		
}
