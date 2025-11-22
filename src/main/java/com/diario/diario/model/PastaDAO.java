package com.diario.diario.model;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class PastaDAO {
    
    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    public void inserirPasta(Pasta pasta) {
        String sql = "INSERT INTO pasta (nome, usuario_id) VALUES (?, ?)";
        jdbc.update(sql, pasta.getNome(), pasta.getUsuarioId());
    }

    public List<Pasta> listarPastasPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM pasta WHERE usuario_id = ? ORDER BY nome";
        return jdbc.query(sql, 
            (rs, rowNum) -> new Pasta(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getInt("usuario_id"),
                rs.getTimestamp("data_criacao").toLocalDateTime()
            ), 
            usuarioId);
    }

    public Pasta buscarPastaPorId(int id) {
        String sql = "SELECT * FROM pasta WHERE id = ?";
        try {
            return jdbc.queryForObject(sql,
                (rs, rowNum) -> new Pasta(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("usuario_id"),
                    rs.getTimestamp("data_criacao").toLocalDateTime()
                ),
                id);
        } catch (Exception e) {
            return null;
        }
    }

    public void deletarPasta(int id) {
        String sql = "DELETE FROM pasta WHERE id = ?";
        jdbc.update(sql, id);
    }
}
