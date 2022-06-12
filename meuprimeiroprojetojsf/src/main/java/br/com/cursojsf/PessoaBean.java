package br.com.cursojsf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {
	
	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	
	public String salvar() {
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		mostrarMsg("Cadastrado com sucesso");
		return "";
	}
	
	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
		
	}

	public String novo() {
		pessoa= new Pessoa();
		return "";
	}
	
	public String limpar() {
		pessoa= new Pessoa();
		return "";
	}
	
	public String remove() {
		daoGeneric.deletePorId(pessoa);
		pessoa= new Pessoa();
		carregarPessoas();
		mostrarMsg("Removido com sucesso");
		return "";
	}
	
	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(Pessoa.class);
		
	}
	
	public String logar() {
		
		Pessoa pessoaUser = iDaoPessoa.consultaUsuario(pessoa.getLogin(), pessoa.getSenha());
		
		if (pessoaUser != null) {
			
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaUser);
			
			
			return "primeirapagina.jsf";
		}
			
		return "index.jsf";
	}
	
	public String deslogar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
	    externalContext.getSessionMap().remove("usuarioLogado");
		
	   HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
	    httpServletRequest.getSession().invalidate();
	    
		return "index.jsf";
	}
	
	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
	    Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
	    return pessoaUser.getPerfil().equals(acesso);
	}
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
	try {
		
		URL url = new URL("http://viacep.com.br/ws/"+pessoa.getCep()+"/json/");
		URLConnection connection = url.openConnection();
		
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		
		String cep = "";
		StringBuilder jsonCep = new StringBuilder();
		
		while ((cep = br.readLine()) !=null) {
			jsonCep.append(cep);
		}
		
		Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(),Pessoa.class);
		
		
		pessoa.setLogradouro(gsonAux.getLogradouro());
		pessoa.setComplemento(gsonAux.getComplemento());
		pessoa.setBairro(gsonAux.getBairro());
		pessoa.setLocalidade(gsonAux.getLocalidade());
		pessoa.setUf(gsonAux.getUf());
		pessoa.setDdd(gsonAux.getDdd());
		
	
		
	}catch (Exception e) {
		e.printStackTrace();
		mostrarMsg("Erro ao consultar o cep");
	}
		
		
	}
	
	
	//------------------------------------
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}



	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}



	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	
}
