package com.project.Blackbelt.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.Blackbelt.Model.Users;
import com.project.Blackbelt.Service.RecuperacaosenhaService;


@Controller
public class RecuperacaoSenhaController {
	
	  @Autowired
	    private RecuperacaosenhaService recuperacaoSenhaService;

    @RequestMapping("/esquecersenha")
    public String RecuperarSenha(Model model) {
    	
        return "/paginas/recuperar-senha";
    }

    @PostMapping("/recuperar-senha")
    public ModelAndView processarRecuperacao(@RequestParam("email") String email) {
        recuperacaoSenhaService.processarSolicitacaoRecuperacao(email);
        ModelAndView mv = new ModelAndView("/paginas/recuperar-senha");
        mv.addObject("mensagem", "E-mail de recuperação enviado.");
        return mv;
    }

    @GetMapping("/redefinirsenha")
    public ModelAndView mostrarFormularioRedefinirSenha(@RequestParam("token") String token) {
        Users usuario = recuperacaoSenhaService.validarToken(token);
        if (usuario == null) {
            return new ModelAndView("redirect:/esquecersenha?tokenInvalido");
        }
        ModelAndView mv = new ModelAndView("/paginas/redefinir-senha");
        mv.addObject("token", token);
        return mv;
    }

    @PostMapping("/redefinirsenha")
    public ModelAndView processarRedefinicaoSenha(@RequestParam("token") String token,
                                                  @RequestParam("senha") String senha) {
        Users usuario = recuperacaoSenhaService.validarToken(token);
        if (usuario == null) {
            return new ModelAndView("redirect:/esquecersenha?tokenInvalido");
        }
        recuperacaoSenhaService.redefinirSenha(usuario, senha);
        return new ModelAndView("redirect:/?senhaAtualizada");
    }

 
}
