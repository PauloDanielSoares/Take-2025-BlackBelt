package com.project.Blackbelt.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Blackbelt.Model.Users;
import com.project.Blackbelt.Repository.UserRepository;

@Service
public class RecuperacaosenhaService {

	@Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    public void processarSolicitacaoRecuperacao(String email) {
        Users usuario = usuarioRepository.findByUsername(email);
        if (usuario != null) {
            String token = UUID.randomUUID().toString();
            usuario.setTokenRecuperacaoSenha(token);
            usuario.setTokenExpiracao(LocalDateTime.now().plusHours(1));
            usuarioRepository.save(usuario);

            String link = "http://localhost:8080/redefinirsenha?token=" + token;
            String corpo = "Clique no link para redefinir sua senha: " + link;

            emailService.enviarEmail(usuario.getLogin(), "Redefinição de Senha", corpo);
        }
    }

    public Users validarToken(String token) {
        Users usuario = usuarioRepository.findByTokenRecuperacaoSenha(token);
        if (usuario != null && usuario.getTokenExpiracao().isAfter(LocalDateTime.now())) {
            return usuario;
        }
        return null;
    }

    public void redefinirSenha(Users usuario, String novaSenha) {
        usuario.setPassword(novaSenha); // você deve codificar a senha
        usuario.setTokenRecuperacaoSenha(null);
        usuario.setTokenExpiracao(null);
        usuarioRepository.save(usuario);
    }
}
