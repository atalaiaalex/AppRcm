package br.com.supermercadoatalaia.apprcm.controller;

import android.content.Context;

import br.com.supermercadoatalaia.apprcm.domain.model.ProdUnidade;
import br.com.supermercadoatalaia.apprcm.domain.repository.ProdUnidadeRepository;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;

public class ProdutoController {
    private final ProdUnidadeRepository prodUnidadeRepository;

    public ProdutoController(Context context) {
        prodUnidadeRepository = new ProdUnidadeRepository(context);
    }

    public ProdUnidade buscarPorIdUnidade(Long id, String unidade)
            throws RegistroNotFoundException {

        return prodUnidadeRepository.buscar(id, unidade);
    }

    public ProdUnidade buscarPorEanUnidade(String ean, String unidade)
            throws RegistroNotFoundException {

        return prodUnidadeRepository.buscar(ean, unidade);
    }
}
