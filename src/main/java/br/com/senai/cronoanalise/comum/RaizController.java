package br.com.senai.cronoanalise.comum;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Hidden
public class RaizController {

    @GetMapping("/")
    public String raiz() {
        return "redirect:/swagger-ui/index.html";
    }
}
