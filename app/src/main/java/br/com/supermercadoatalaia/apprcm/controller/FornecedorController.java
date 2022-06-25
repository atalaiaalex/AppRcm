package br.com.supermercadoatalaia.apprcm.controller;

import android.content.Context;

import java.util.List;

import br.com.supermercadoatalaia.apprcm.domain.model.Fornecedor;
import br.com.supermercadoatalaia.apprcm.domain.model.OcorrenciaFornecedor;
import br.com.supermercadoatalaia.apprcm.domain.repository.FornecedorRepository;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;

public class FornecedorController {
    private final FornecedorRepository repository;

    public FornecedorController(Context context) {
        repository = new FornecedorRepository(context);
    }

    public Fornecedor buscarPorId(Long id) throws RegistroNotFoundException {
        return repository.buscar(id);
    }

    public Fornecedor buscarPorCnpjCpf(String cnpjCpf) throws RegistroNotFoundException {
        return repository.buscar(cnpjCpf);
    }

    public List<Fornecedor> listarPorVinculo(Long vinculo) throws RegistroNotFoundException {
        return repository.listar(vinculo);
    }

    public List<OcorrenciaFornecedor> listarOcorrencias(Long fornecedorId)
            throws RegistroNotFoundException {

        return repository.listarOcorrencias(fornecedorId);
    }
}
