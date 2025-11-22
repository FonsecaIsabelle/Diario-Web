package com.diario.diario.model;

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

}
