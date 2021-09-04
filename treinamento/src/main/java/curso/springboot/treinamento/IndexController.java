package curso.springboot.treinamento;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import curso.springboot.model.Usuario;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String index(){
		return "index";
	}
	
}
