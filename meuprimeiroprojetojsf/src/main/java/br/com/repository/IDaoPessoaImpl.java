package br.com.repository;

import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;


public class IDaoPessoaImpl implements IDaoPessoa {

	@Override
	public Pessoa consultaUsuario(String login, String senha) {
		Pessoa pessoa = null;
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		try {
		pessoa = (Pessoa) entityManager.createQuery("select p from Pessoa p where p.login = '" + login + "' and p.senha = '" + senha + "'").getSingleResult();
		}catch (javax.persistence.NoResultException e) { 
		}
		entityTransaction.commit();
		entityManager.close();
		
		return pessoa;
	}

	@Override
	public List<SelectItem> listaEstados() {
		// TODO Auto-generated method stub
		return null;
	}

}