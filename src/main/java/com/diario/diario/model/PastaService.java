package com.diario.diario.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PastaService {

    @Autowired
    PastaDAO pastaDAO;

    public void inserirPasta(Pasta pasta) {
        pastaDAO.inserirPasta(pasta);
    }

    public List<Pasta> listarPastasPorUsuario(int usuarioId) {
        return pastaDAO.listarPastasPorUsuario(usuarioId);
    }

    public Pasta buscarPastaPorId(int id) {
        return pastaDAO.buscarPastaPorId(id);
    }

    public void deletarPasta(int id) {
        pastaDAO.deletarPasta(id);
    }
}