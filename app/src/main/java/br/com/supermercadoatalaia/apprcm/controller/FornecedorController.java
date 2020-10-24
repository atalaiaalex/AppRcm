package br.com.supermercadoatalaia.apprcm.controller;

import java.io.IOException;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
import br.com.supermercadoatalaia.apprcm.domain.model.Fornecedor;
import br.com.supermercadoatalaia.apprcm.domain.repository.FornecedorRepository;

public class FornecedorController {
    private final FornecedorRepository fornecedorRepository;

    public FornecedorController(ConfigApp configApp) throws IOException {
        fornecedorRepository = new FornecedorRepository(configApp);
    }

    public Fornecedor buscarPorId(Long id) throws ApiException, IOException {
        return fornecedorRepository.buscar(id);
    }

    public Fornecedor buscarPorCnpjCpf(String cnpjCpf) throws ApiException, IOException {
        return fornecedorRepository.buscar(cnpjCpf);
    }

    public List<Fornecedor> listarPorVinculo(Long vinculo) throws ApiException, IOException {
        return fornecedorRepository.listar(vinculo);
    }
}
