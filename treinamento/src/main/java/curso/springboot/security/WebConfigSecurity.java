package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private ImplUserDetailsService implUserDetailsService;
	
	/* Configura como será tratado as solicitações de acesso por HTTP */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable()  //Desativa as configurações padrões de mémória, para podermos definir conforme nossas regras.
		.authorizeRequests() //Habilita a restrição de acessos.
		.antMatchers(HttpMethod.GET, "/").permitAll() //Concede acesso liberado a todos os usuários na pagina inicial do sistema.
		.antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN") //Bloqueando o acesso a URL caso o role do usuário não seja ADMIN.
		.anyRequest().authenticated() // Força o usuário autenticar-se para utilizar qualquer outra URL
		.and().formLogin().permitAll().loginPage("/login") //Concede acesso liberado a tela de formulário a todos os usuários.
		.defaultSuccessUrl("/cadastropessoa").failureUrl("/login?error=true")
		.and().logout().logoutSuccessUrl("/login").logoutRequestMatcher(new AntPathRequestMatcher("/logout")); //Mapeia a URL de logout e invalida o usuário .
	}
	
	/* Realiza as autenticações do usuário com o banco de dados ou com os dados em memória */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		auth.userDetailsService(implUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());	
	}
	
	/* Ignora URLs específicas */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**"); //Ignora URL
	}

}
