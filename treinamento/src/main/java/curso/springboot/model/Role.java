package curso.springboot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
@Entity
public class Role implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nomeRole;

	@Override
	public String getAuthority() {   //Irá Retornar ROLE_ADMIN, ROLE_GERENTE, ROLE_SECRETARIO.
		return this.nomeRole;
	}
	
}