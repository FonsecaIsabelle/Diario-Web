package com.diario.diario.model;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class PostDAO {
    
    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    public void inserirPost(Post post) {
        String sql = "INSERT INTO post (titulo, conteudo, imagem_url, usuario_id, pasta_id) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, post.getTitulo(), post.getConteudo(), post.getImagemUrl(), post.getUsuarioId(), post.getPastaId());
    }

    public List<Post> listarPostsPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM post WHERE usuario_id = ? ORDER BY data_criacao DESC";
        return jdbc.query(sql, 
            (rs, rowNum) -> new Post(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("conteudo"),
                rs.getString("imagem_url"),
                rs.getTimestamp("data_criacao").toLocalDateTime(),
                rs.getInt("usuario_id"),
                (Integer) rs.getObject("pasta_id")
            ), 
            usuarioId);
    }

    public void atualizarPost(Post post) {
        String sql = "UPDATE post SET titulo = ?, conteudo = ?, imagem_url = ?, pasta_id = ? WHERE id = ?";
        jdbc.update(sql, post.getTitulo(), post.getConteudo(), post.getImagemUrl(), post.getPastaId(), post.getId());
    }

    public void deletarPost(int id) {
        String sql = "DELETE FROM post WHERE id = ?";
        jdbc.update(sql, id);
    }

    public Post buscarPostPorId(int id) {
        String sql = "SELECT * FROM post WHERE id = ?";
        try {
            return jdbc.queryForObject(sql,
                (rs, rowNum) -> new Post(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("conteudo"),
                    rs.getString("imagem_url"),
                    rs.getTimestamp("data_criacao").toLocalDateTime(),
                    rs.getInt("usuario_id"),
                    (Integer) rs.getObject("pasta_id")
                ),
                id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Post> listarPostsPorPasta(int pastaId) {
        String sql = "SELECT * FROM post WHERE pasta_id = ? ORDER BY data_criacao DESC";
        return jdbc.query(sql, 
            (rs, rowNum) -> new Post(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("conteudo"),
                rs.getString("imagem_url"),
                rs.getTimestamp("data_criacao").toLocalDateTime(),
                rs.getInt("usuario_id"),
                (Integer) rs.getObject("pasta_id")
            ), 
            pastaId);
    }

    public List<Post> listarPostsPorData(int usuarioId, String data) {
        String sql = "SELECT * FROM post WHERE usuario_id = ? AND data_criacao::date = ?::date ORDER BY data_criacao DESC";
        return jdbc.query(sql, 
            (rs, rowNum) -> new Post(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("conteudo"),
                rs.getString("imagem_url"),
                rs.getTimestamp("data_criacao").toLocalDateTime(),
                rs.getInt("usuario_id"),
                (Integer) rs.getObject("pasta_id")
            ), 
            usuarioId, data);
    }
}