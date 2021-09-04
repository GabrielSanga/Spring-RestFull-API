package curso.springboot.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import curso.springboot.enums.Cargo;
import lombok.Data;

@Valid
@Data
@Entity
public class Pessoa implements Serializable{
	
	public Pessoa(){}
	
	public Pessoa(String sNome, String sSexo){
		this.nome = sNome;
		this.sexo = sSexo;
	}
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull(message = "Nome não pode ser Nulo!")
	@NotEmpty(message = "Nome não pode ser Vazio!")
	private String nome;
	
	@NotNull(message = "Sobrenome não pode ser Nulo!")
	@NotEmpty(message = "Sobrenome não pode ser Vazio!")
	private String sobrenome;
	
	private String sexo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@NotNull(message = "Idade não pode ser Nulo!")
	@Min(value = 18, message = "Idade Inválida!")
	private int idade;
	
	@OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Telefone> telefones;
	
	private String cep;
	
	private String rua;
	
	private String bairro;
	
	private String cidade;
	
	private String estado;
	
	private String ibge;
	
	@ManyToOne()
	private Profissao profissao;
	
	@Enumerated(EnumType.STRING)
	private Cargo cargo;
	
	@Lob
	private byte[] curriculo;	
	private String nomeArquivo;	
	private String tipoArquivo;

}
