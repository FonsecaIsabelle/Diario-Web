package com.diario.diario.model;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private String titulo;
    private String conteudo;
    private String imagemUrl;
    private LocalDateTime dataCriacao;
    private int usuarioId;
    private Integer pastaId; // NOVO CAMPO

    // Construtor para Select (COM pasta_id) - 7 PARÂMETROS
    public Post(int id, String titulo, String conteudo, String imagemUrl, LocalDateTime dataCriacao, int usuarioId, Integer pastaId) {
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.imagemUrl = imagemUrl;
        this.dataCriacao = dataCriacao;
        this.usuarioId = usuarioId;
        this.pastaId = pastaId;
    }

    // Construtor para Insert (COM pasta_id) - 5 PARÂMETROS
    public Post(String titulo, String conteudo, String imagemUrl, int usuarioId, Integer pastaId) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.imagemUrl = imagemUrl;
        this.usuarioId = usuarioId;
        this.pastaId = pastaId;
    }

    // Construtor vazio para formulários
    public Post() {
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    // GETTER E SETTER DO NOVO CAMPO
    public Integer getPastaId() {
        return pastaId;
    }

    public void setPastaId(Integer pastaId) {
        this.pastaId = pastaId;
    }
}