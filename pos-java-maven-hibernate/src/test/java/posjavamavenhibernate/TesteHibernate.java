package posjavamavenhibernate;

import java.util.List;

import org.junit.Test;

import dao.DaoGeneric;
import model.TelefoneUser;
import model.UsuarioPessoa;

public class TesteHibernate {
	
	//TESTE GRAVA
	@Test
	public void testeHibernateUtil() {
		
	DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric <UsuarioPessoa>();
	
	UsuarioPessoa pessoa = new UsuarioPessoa();
	
	pessoa.setNome("D");
	pessoa.setSobrenome("Vargas");
	pessoa.setEmail("yanvargas@gmail.com");
	pessoa.setLogin("yan.vargas");
	pessoa.setSenha("123");
	
	daoGeneric.salvar(pessoa);
	
	}
	
	//TESTE BUSCA
	@Test
	public void testeBuscar() {
		
	DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric <UsuarioPessoa>();
	
	UsuarioPessoa pessoa = new UsuarioPessoa();
	
	pessoa.setId(1L);
	
	pessoa = daoGeneric.pesquisar(pessoa);
	
	System.out.println(pessoa);
		
		
	}
	
	//TESTE BUSCA 2
	@Test
	public void testeBuscar2() {
		
	DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric <UsuarioPessoa>();
	
	UsuarioPessoa pessoa = daoGeneric.pesquisar(1L, UsuarioPessoa.class);
	
	System.out.println(pessoa);
		
		
	}
	
	//TESTE UPDATE 
	@Test
	public void testeUpdate() {
		
	DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric <UsuarioPessoa>();
	
	UsuarioPessoa pessoa = daoGeneric.pesquisar(1L, UsuarioPessoa.class);
	
	pessoa.setEmail("yan@hotmail.com");
	
	pessoa = daoGeneric.updateMerge(pessoa);
	
	System.out.println(pessoa);
		
		
	}
	
	//TESTE DELETE 
	@Test
	public void testeDelete() {
		
	DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric <UsuarioPessoa>();
	
	UsuarioPessoa pessoa = daoGeneric.pesquisar(4L, UsuarioPessoa.class);
	
	daoGeneric.deletarPorId(pessoa);
		
	}
	
	//TESTE CONSULTA LISTA 
	@Test
	public void testeConsultaLista() {
		
	DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric <UsuarioPessoa>();
	
	List<UsuarioPessoa>	lista = daoGeneric.listar(UsuarioPessoa.class);
	
	for (UsuarioPessoa listar : lista) {
		System.out.println(listar);
		System.out.println("------------");
	}
					
	}
	
	//TESTE ESPECÍFICO COM FITRO BANCO
	@Test
	public void testeQueryList() {
		
	DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
	List<UsuarioPessoa> list = daoGeneric.getEntityManager().createQuery(" from UsuarioPessoa where nome='Baptista'").getResultList();
	
	for (UsuarioPessoa usuarioPessoa : list) {
		System.out.println(usuarioPessoa);
	}
	
	}
	
	//TESTE LIMITANDO RESULTADOS
	@Test
	public void testeQueryListMaxResult() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createQuery(" from UsuarioPessoa order by nome").setMaxResults(3).getResultList();
		
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
		
		}
	
	//TESTE BUSCANDO USUÁRIO POR PARAMETRO
	@Test
	public void testeQueryListParameter() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		
		List<UsuarioPessoa> list = daoGeneric.
				getEntityManager().createQuery("from UsuarioPessoa where nome = :nome or sobrenome = :sobrenome")
				.setParameter("nome", "AS").setParameter("sobrenome", "Vargas").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
		
	}
	
	//TESTE BUSCA POR @NAMEDQUERIE FORMATADO
	@Test
	public void testeNameQuery() {
	
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.todos").getResultList(); //usando @NamedQueries
		
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			
		}
		
	}
	
	//TESTE BUSCA POR @NAMEDQUERIE FORMATADO + PARAMETRO
	@Test
	public void testeNameQuery2() {
	
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.buscaPorNome").setParameter("nome","B").getResultList(); //usando @NamedQueries
		
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			
		}
		
	}
		
	//OPERAÇÃO MATEMATICAS NO BANCO
	@Test
	public void testeQuerySomaIdade() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		
		Long somaIdade = (Long) daoGeneric.getEntityManager().createQuery("select sum (u.idade) from UsuarioPessoa u").getSingleResult();
		
	}
	
	//GRAVA TELEFONE
	@Test
	public void testeGravaTelefone() {
		
		DaoGeneric daoGeneric  = new DaoGeneric();
		
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(8L, UsuarioPessoa.class);
		
		TelefoneUser telefoneUser = new TelefoneUser();
		telefoneUser.setTipo("Casa");
		telefoneUser.setNumero("5133234534");
		telefoneUser.setUsuarioPessoa(pessoa);
		
		daoGeneric.salvar(telefoneUser);
		
		
	}
	
	//LISTA TELEFONE
	@Test
	public void testeConsultaTelefone() {
		
		DaoGeneric daoGeneric  = new DaoGeneric();
		
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(8L, UsuarioPessoa.class);
		
		for (TelefoneUser fone : pessoa.getTelefoneUsers()) {
			System.out.println(fone.getNumero());
			System.out.println(fone.getTipo());
			System.out.println(fone.getUsuarioPessoa().getNome());
			System.out.println("-------------------");
		}
		
		
	}
	
	
	
}
