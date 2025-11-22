package com.diario.diario.model;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class UsuarioDAO {
    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    public void inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
        Object[] obj = new Object[3];
        obj[0] = usuario.getNome();
        obj[1] = usuario.getEmail();
        obj[2] = usuario.getSenha();
        jdbc.update(sql, obj);
    }

    public Usuario buscarPorEmailESenha(String email, String senha) {
    String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
    try {
        return jdbc.queryForObject(sql, 
            (rs, rowNum) -> new Usuario(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha")
            ), 
            email, senha);
    } catch (Exception e) {
        return null; // usuário não encontrado
    }
}

}
