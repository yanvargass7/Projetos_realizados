package managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import dao.DaoGeneric;
import model.UsuarioPessoa;

@ManagedBean (name = "usuarioPessoaManagedBean")
@ViewScoped
public class UsuarioPessoaManagedBean {
	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();
	private List<UsuarioPessoa> lista = new ArrayList<UsuarioPessoa>();
	
	public String salvar() {
		daoGeneric.salvar(usuarioPessoa);
		return "";
	}
	
	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}
	
	public String excluir() {
		daoGeneric.deletarPorId(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}
	
	
	

	
	
	
//------------------

	
	
	public List<UsuarioPessoa> getLista() {
		lista = daoGeneric.listar(UsuarioPessoa.class);
		return lista;
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	public DaoGeneric<UsuarioPessoa> getDaoGeneric() {
		return daoGeneric;
	}
	public void setDaoGeneric(DaoGeneric<UsuarioPessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	
//------------------
	

}
