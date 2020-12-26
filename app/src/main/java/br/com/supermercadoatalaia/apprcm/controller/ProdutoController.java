package br.com.supermercadoatalaia.apprcm.controller;

import java.io.IOException;

import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
import br.com.supermercadoatalaia.apprcm.domain.model.ProdUnidade;
import br.com.supermercadoatalaia.apprcm.domain.repository.ProdUnidadeRepository;

public class ProdutoController {
    private final ProdUnidadeRepository prodUnidadeRepository;

    public ProdutoController() {
        prodUnidadeRepository = new ProdUnidadeRepository();
    }

    public ProdUnidade buscarPorId(Long id, String unidade) throws ApiException, IOException {
        return prodUnidadeRepository.buscar(id, unidade);
    }

    public ProdUnidade buscarPorEan(String ean, String unidade) throws ApiException, IOException {
        return prodUnidadeRepository.buscar(ean, unidade);
    }
}
