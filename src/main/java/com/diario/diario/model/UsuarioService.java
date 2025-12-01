package com.diario.diario.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioDAO udao;

    public void inserirUsuario(Usuario usuario) {
        udao.inserirUsuario(usuario);
    }

    public Usuario autenticar(String email, String senha) {
    return udao.buscarPorEmailESenha(email, senha);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return udao.findByEmail(email);
    }
    
    public boolean validarFormatoEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String regexEmail = "^[A-Za-z0-9+_.-]+@(gmail|hotmail|outlook|yahoo|icloud)\\.com$";
        return email.matches(regexEmail);
    }
    
    public boolean validarForcaSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            return false;
        }
        
        boolean temMaiuscula = senha.matches(".*[A-Z].*");
        boolean temMinuscula = senha.matches(".*[a-z].*");
        boolean temNumero = senha.matches(".*\\d.*");
        
        return temMaiuscula && temMinuscula && temNumero;
    }
    
    public Usuario salvar(Usuario usuario) {
        udao.inserirUsuario(usuario);
        return usuario;
    }

}
