package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Usuario;
import curso.springboot.repository.UsuarioRepository;

@Service
@Transactional
public class ImplUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario oUsuario = usuarioRepository.findUserByLogin(username);
		
		if(oUsuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado!");
		}
		
		return new User(oUsuario.getLogin(), oUsuario.getSenha(), true, true, true, true, oUsuario.getAuthorities());
	}

}
