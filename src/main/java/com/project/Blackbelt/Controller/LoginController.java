package com.project.Blackbelt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@RequestMapping("/login")
    public String login() {
        return "paginas/login";
    }
}
