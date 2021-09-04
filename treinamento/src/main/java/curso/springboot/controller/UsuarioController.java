package curso.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {
	
	
	@GetMapping("/cadastrousuario")
	public String inicio() {
		
		return "cadastro/cadastrousuario";
	}

}
