package com.project.Blackbelt.Security;





import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebSecurityConfig implements WebMvcConfigurer {

	private final SecurityDatabaseService securityDatabaseService;

	public WebSecurityConfig(SecurityDatabaseService securityDatabaseService) {
		this.securityDatabaseService = securityDatabaseService;
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> authz
				// Rotas acessíveis apenas pelo admin
				.requestMatchers("/login.css","/img/**","/js/**","/esquecersenha","/redefinirsenha","/recuperacao.css"
						, "/recuperar-senha").permitAll()
			
				.requestMatchers("/salvarusuarios","/listarusuarios","/editarusuarios/{idusuario}",
						"/filial","/upload","/usuarios","/empresa","/update-sheets","/importar-csv").hasRole("MANAGER")
				
				// Qualquer outra rota requer autenticação
				.anyRequest().authenticated())
				// Usa o form da pagina tela.html do Spring Security
				.formLogin((form) -> form
						.loginPage("/login")
						.failureUrl("/login?error=true")
						.defaultSuccessUrl("/login?success", true) 
						.permitAll())
				 .logout(logout -> logout.logoutUrl("/logout")
			             .permitAll());  
		       
		
		http.csrf(csrf -> csrf.disable());

		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
