package com.diario.diario.model;

import java.time.LocalDateTime;

public class Pasta {
    private int id;
    private String nome;
    private int usuarioId;
    private LocalDateTime dataCriacao;

    // Construtor para Select
    public Pasta(int id, String nome, int usuarioId, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.usuarioId = usuarioId;
        this.dataCriacao = dataCriacao;
    }

    // Construtor para Insert
    public Pasta(String nome, int usuarioId) {
        this.nome = nome;
        this.usuarioId = usuarioId;
    }

    // Construtor vazio
    public Pasta() {
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}