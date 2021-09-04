package curso.springboot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Valid
@Data
@Entity
public class Telefone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "Número é campo de preenchimento obrigatório!")
	private String numero;

	@NotEmpty(message = "Tipo é campo de preenchimento obrigatório!")
	private String tipo;
	
	@org.hibernate.annotations.ForeignKey(name = "pessoa_id")
	@ManyToOne
	private Pessoa pessoa;

}
