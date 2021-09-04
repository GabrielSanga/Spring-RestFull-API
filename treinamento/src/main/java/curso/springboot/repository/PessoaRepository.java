package curso.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	
	@Query("Select p From Pessoa p Where p.nome like %?1% and (p.sexo = ?2 or ?2 = '')")
	public List<Pessoa> BuscarPorNomeSexo(String sNome, String sSexo);
	
	default Page<Pessoa> BuscaPorNomePage(String sNome, Pageable pageable){
			
		Pessoa oPessoa = new Pessoa();
		oPessoa.setNome(sNome);
		
		/* Configuração da consulta para filtrar o nome e sexo no banco de dados */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		/* Une o objeto com os valores com a configuração a ser consultada */
		Example<Pessoa> example = Example.of(oPessoa, exampleMatcher);
		
		/* Busca as pessoas de acordo com a configuração */
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		return pessoas;
	}
	
	default Page<Pessoa> BuscaPorNomeSexoPage(String sNome, String sSexo, Pageable pageable){
		
		Pessoa oPessoa = new Pessoa();
		oPessoa.setNome(sNome);
		oPessoa.setSexo(sSexo);
		
		/* Configuração da consulta para filtrar o nome e sexo no banco de dados */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		/* Une o objeto com os valores com a configuração a ser consultada */
		Example<Pessoa> example = Example.of(oPessoa, exampleMatcher);
		
		/* Busca as pessoas de acordo com a configuração */
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		return pessoas;
	}
	

}
