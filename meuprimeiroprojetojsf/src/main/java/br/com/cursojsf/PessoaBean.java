//CLASSE PESSOA.BEAN
package br.com.cursojsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {
	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	
	private List<SelectItem> estados;
	private List<SelectItem> cidades;
	private Part arquivoFoto;
	
	public String salvar() throws IOException {
		//processa imagem
		byte[] imagemByte = getByte(arquivoFoto.getInputStream());
		pessoa.setFotoIconBase64Original(imagemByte);
		
		//transforma em bufferimage
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
		
		//pega o time da imagem
		int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		
		int largura = 200;
		int altura = 200;
		
		//cria a miniatura
		BufferedImage resizedImage = new BufferedImage(largura, altura, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(bufferedImage, 0, 0, largura, altura, null);
		g.dispose();
		
		//escrever novamente a imagem em tamanho menor
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = arquivoFoto.getContentType().split("\\/")[1];//image/png
		ImageIO.write(resizedImage, extensao, baos);
		
		String miniImage = "data:"+ arquivoFoto.getContentType()+";base64,"+
						DatatypeConverter.printBase64Binary(baos.toByteArray());
		//processar imagem
		pessoa.setFotoIconBase64(miniImage);
		pessoa.setExtensao(extensao);
		
		
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		mostrarMsg("Cadastrado com sucesso");
		return "";
	}
	
	public void registraLog() {
		System.out.println("metodo registrado");
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
	
	public void carregaCidades(AjaxBehaviorEvent event) {
		
	Estados estado = (Estados)((HtmlSelectOneMenu)event.getSource()).getValue();
		
		  
		 if (estado != null) { 
			 
		 
		  List<Cidades> cidades = JPAUtil.getEntityManager()
				  .createQuery("from Cidades where estados.id = "
			      + estado.getId()).getResultList();
		  
		  List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();
		  
		  for (Cidades cidade : cidades) { selectItemsCidade.add(new
		  SelectItem(cidade,cidade.getNome())); }
		  
		  setCidades(selectItemsCidade);
		 
				
			}
		}
	
	public void editar() {
		
		if(pessoa.getCidades()!=null) {
			Estados estado = pessoa.getCidades().getEstados();
			pessoa.setEstados(estado);
			
			 List<Cidades> cidades = JPAUtil.getEntityManager()
					  .createQuery("from Cidades where estados.id = "
				      + estado.getId()).getResultList();
			  
			  List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();
			  
			  for (Cidades cidade : cidades) { selectItemsCidade.add(new
			  SelectItem(cidade,cidade.getNome())); }
			  
			  setCidades(selectItemsCidade);
		}
		
	}
	
	private byte[] getByte (InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte [] buf = null;
		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte [size];
			len = is.read(buf,0,size);
			
		}else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while((len = is.read(buf,0,size)) != -1) {
				bos.write(buf, 0, len);
			}
			buf = bos.toByteArray();
		}
		return buf;
	}
	
	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId = params.get("fileDownloadId");
		
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDownloadId);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-Disposition", "attachment; filename=download."+pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBase64Original().length);
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush();
		
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	
	//------------------------------------
	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}
	public Part getArquivoFoto() {
		return arquivoFoto;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}
	public List<SelectItem> getCidades() {
		return cidades;
	}

	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
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
