package com.diario.diario.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    PostDAO pdao;

    public void inserirPost(Post post) {
        pdao.inserirPost(post);
    }

    public List<Post> listarPostsPorUsuario(int usuarioId) {
        return pdao.listarPostsPorUsuario(usuarioId);
    }

    public void atualizarPost(Post post) {
        pdao.atualizarPost(post);
    }

    public void deletarPost(int id) {
        pdao.deletarPost(id);
    }

    public Post buscarPostPorId(int id) {
        return pdao.buscarPostPorId(id);
    }

    public List<Post> listarPostsPorPasta(int pastaId) {
        return pdao.listarPostsPorPasta(pastaId);
    }

    public List<Post> listarPostsPorData(int usuarioId, String data) {
        return pdao.listarPostsPorData(usuarioId, data);
    }

}