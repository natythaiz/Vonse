package web.controlevacinacao.controller;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping(value = {"/", "/index.html"} )
    public String index(Model model) {
        String mesAtual = LocalDate.now()
            .getMonth()
            .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"))
            .toUpperCase();

        model.addAttribute("mesAtual", mesAtual);
        return "index";
    }
	
}
