package curso.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.ProfissaoRepository;
import curso.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private ReportUtil reportUtil;
	
	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView modelView = new ModelAndView("cadastro/cadastropessoa");
		modelView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		modelView.addObject("Pessoa", new Pessoa());
		modelView.addObject("profissoes", profissaoRepository.findAll());
		
		return modelView;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa", consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult, final MultipartFile file) throws IOException {
		
		if(bindingResult.hasErrors()) {
			ModelAndView modelView = new ModelAndView("cadastro/cadastropessoa");
			modelView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			modelView.addObject("Pessoa", pessoa);
			
			List<String> lstErros = new ArrayList<String>();
			
			for (ObjectError error : bindingResult.getAllErrors()) {
				lstErros.add(error.getDefaultMessage());	
			}
			
			modelView.addObject("msgErros", lstErros);
						
			return modelView;
		}
		//Seta os telefones no objeto de pessoa (para evitar erros no momento de persistir no BD teste)
		pessoa.setTelefones(telefoneRepository.TelefonesPessoa(pessoa.getId()));
		
		//Caso venha um arquivo(currículo) do formulário, adiciona na pessoa
		if(file.getSize() > 0) {
			pessoa.setCurriculo(file.getBytes());
			pessoa.setTipoArquivo(file.getContentType());
			pessoa.setNomeArquivo(file.getOriginalFilename());
		}else if (pessoa.getId() != null && pessoa.getId() > 0) {
			//Caso não venha o curriculo e pessoa já existir - carrega a pessoa e passa nos objeto - para não perder o arquivo em edições
			Pessoa pessoaTemporaria = pessoaRepository.findById(pessoa.getId()).get();
			pessoa.setCurriculo(pessoaTemporaria.getCurriculo());
			pessoa.setTipoArquivo(pessoaTemporaria.getTipoArquivo());
			pessoa.setNomeArquivo(pessoaTemporaria.getNomeArquivo());
		}
		
		//Salva a pessoa
		pessoaRepository.save(pessoa);
		//Lista todas as pessoas
		ModelAndView modelView = new ModelAndView("cadastro/cadastropessoa");
		modelView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		modelView.addObject("Pessoa", new Pessoa());
		//retorna
		return modelView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	public ModelAndView lista() {
		ModelAndView modelView = new ModelAndView("cadastro/cadastropessoa");
		modelView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		modelView.addObject("Pessoa", new Pessoa());
		
		return modelView;
	}
	
	@GetMapping("**/listapaginacao")
	public ModelAndView ListaPessoaPaginacao(@PageableDefault(size = 5) Pageable pageable, ModelAndView andView,
			                                 @RequestParam("pesquisaNome") String pesquisaNome) {
		
		Page<Pessoa> pessoas = pessoaRepository.BuscaPorNomeSexoPage(pesquisaNome, "", pageable);
		andView.addObject("pessoas", pessoas);
		andView.addObject("Pessoa", new Pessoa());
		andView.addObject("nomepesquisa", pesquisaNome);
		andView.addObject("profissoes", profissaoRepository.findAll());
		andView.setViewName("cadastro/cadastropessoa");
		
		return andView;		
	}
	
	@GetMapping("**/editarpessoa/{idPessoa}")
	public ModelAndView editar(@PathVariable("idPessoa") Long idPessoa) {		
		Optional<Pessoa> oPessoa = pessoaRepository.findById(idPessoa);
		
		ModelAndView modelView = new ModelAndView("cadastro/cadastropessoa");
		modelView.addObject("profissoes", profissaoRepository.findAll());
		modelView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		modelView.addObject("Pessoa", oPessoa.get());
		
		return modelView;
	}
	
	@GetMapping("/excluirpessoa/{idPessoa}")
	public ModelAndView excluir(@PathVariable("idPessoa") Long idPessoa) {
		pessoaRepository.deleteById(idPessoa);
		
		ModelAndView modelView = new ModelAndView("cadastro/cadastropessoa");
		modelView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		modelView.addObject("Pessoa", new Pessoa());
		
		return modelView;
	}
	
	@PostMapping("/filtrarpessoa")
	public ModelAndView filtro(@RequestParam("pesquisaNome") String sPesquisaNome, @RequestParam("pesquisaSexo") String sPesquisaSexo,
			 				   @PageableDefault(size = 5, sort = {"nome"}) Pageable pageable){
		
		Page<Pessoa> pessoas = null;
		
		if(sPesquisaSexo != "" && sPesquisaSexo != null) {
			pessoas = pessoaRepository.BuscaPorNomeSexoPage(sPesquisaNome, sPesquisaSexo, pageable);
		}else {
			pessoas = pessoaRepository.BuscaPorNomePage(sPesquisaNome, pageable);
		}
	
		ModelAndView modelView = new ModelAndView("cadastro/cadastropessoa");
		modelView.addObject("Pessoa", new Pessoa());
		modelView.addObject("pessoas", pessoas);
		modelView.addObject("pesquisaNome", sPesquisaNome);
		
		return modelView;
	}
	
	@GetMapping("/filtrarpessoa")
	public void ImprimeRelatorio(@RequestParam("pesquisaNome") String sPesquisaNome,
			           			 @RequestParam("pesquisaSexo") String sPesquisaSexo,
			           			 HttpServletRequest request,
			           			 HttpServletResponse response) throws Exception{
		
		/* Carrega a lista de dados de acordo com os filtros informados em tela */
		 List<Pessoa> lstPessoas = pessoaRepository.BuscarPorNomeSexo(sPesquisaNome, sPesquisaSexo);
		
		 /* Gera o PDF com os valores da lista */
		 byte[] pdf = reportUtil.gerarRelatorio(lstPessoas, "Pessoa", request.getServletContext());
		
		 /* DEFINIÇÕES PARA O NAVEGADOR */
		 
		 /* Tamanho da Resposta */
		 response.setContentLength(pdf.length);
		 
		 /* Tipo do arquivo a ser devolvido (genérico)*/
		 response.setContentType("application/octet-stream");
		 
		 /* Cabeçalho da resposta */
		 String headerKey = "Content-Disposition";
		 String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		 response.setHeader(headerKey, headerValue);
		 
		 /* Finaliza a resposta */
		 response.getOutputStream().write(pdf);	 
		
	}
	
	@GetMapping("/telefones/{idPessoa}")
	public ModelAndView telefones(@PathVariable("idPessoa") Long idPessoa) {		
		Optional<Pessoa> oPessoa = pessoaRepository.findById(idPessoa);
		
		ModelAndView modelView = new ModelAndView("cadastro/cadastrotelefone");
		modelView.addObject("Pessoa", oPessoa.get());
		modelView.addObject("Telefone", new Telefone());
		modelView.addObject("telefones", telefoneRepository.TelefonesPessoa(idPessoa));
		
		return modelView;
	}
	
	@PostMapping("/salvarTelefone/{pessoaId}")
	public ModelAndView salvarTelefone(@Valid Telefone telefone, BindingResult bindingResult, @PathVariable("pessoaId") Long pessoaId) {
		Pessoa oPessoa = pessoaRepository.findById(pessoaId).get();
		telefone.setPessoa(oPessoa);
		
		if (bindingResult.hasErrors()) {
			ModelAndView modelView = new ModelAndView("cadastro/cadastrotelefone");
			modelView.addObject("Pessoa", oPessoa);
			modelView.addObject("Telefone", telefone);
			modelView.addObject("telefones", telefoneRepository.TelefonesPessoa(pessoaId));
			
			List<String> lstErros = new ArrayList<String>();
			
			for (ObjectError error : bindingResult.getAllErrors()) {
				lstErros.add(error.getDefaultMessage());
			}
			
			modelView.addObject("msgErros", lstErros);
			
			return modelView;
		}
		
		telefoneRepository.save(telefone);
		
		ModelAndView modelView = new ModelAndView("cadastro/cadastrotelefone");
		modelView.addObject("Pessoa", oPessoa);
		modelView.addObject("Telefone", new Telefone());
		modelView.addObject("telefones", telefoneRepository.TelefonesPessoa(pessoaId));
		
		return modelView;
	}
	
	@GetMapping("/excluirTelefone/{idTelefone}")
	public ModelAndView excluirtelefone(@PathVariable("idTelefone") Long idTelefone) {
		
		Pessoa oPessoa = telefoneRepository.findById(idTelefone).get().getPessoa();
		
		telefoneRepository.deleteById(idTelefone);	
		
		ModelAndView modelView = new ModelAndView("cadastro/cadastrotelefone");
		
		modelView.addObject("Pessoa", oPessoa);
		modelView.addObject("Telefone", new Telefone());
		modelView.addObject("telefones", telefoneRepository.TelefonesPessoa(oPessoa.getId()));
		
		return modelView;
	}
	
	@GetMapping("**/baixarcurriculo/{idPessoa}")
	public void BaixarCurriculo(@PathVariable("idPessoa") Long idPessoa, HttpServletResponse response) throws IOException {
		
		Pessoa oPessoa = pessoaRepository.findById(idPessoa).get();
		
		if(oPessoa.getCurriculo() != null) {
			
			/* Seta o tamanho da resposta */
			response.setContentLength(oPessoa.getCurriculo().length);
			
			/* Seta o tipo do retorno (PDF, DOC, JPG...) ou seta o tipo genérico (application/octet-stream)*/
			response.setContentType(oPessoa.getTipoArquivo());
			
			/* Seta o cabeçalho do retorno */
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", oPessoa.getNomeArquivo());
			response.setHeader(headerKey, headerValue);
			
			/* Adicionando os bytes do arquivo no retorno para o navegador */
			response.getOutputStream().write(oPessoa.getCurriculo());
			
		}
		
	}

}
