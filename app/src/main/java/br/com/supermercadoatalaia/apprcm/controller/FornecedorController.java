package br.com.supermercadoatalaia.apprcm.controller;

import android.content.Context;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
import br.com.supermercadoatalaia.apprcm.domain.model.Fornecedor;
import br.com.supermercadoatalaia.apprcm.domain.model.OcorrenciaFornecedor;
import br.com.supermercadoatalaia.apprcm.domain.repository.FornecedorRepository;
import br.com.supermercadoatalaia.apprcm.domain.repository.OcorrenciaRepository;

public class FornecedorController {
    private final FornecedorRepository fornecedorRepository;
    private final OcorrenciaRepository ocorrenciaRepository;

    public FornecedorController(Context context) {
        fornecedorRepository = new FornecedorRepository(context);
        ocorrenciaRepository = new OcorrenciaRepository(context);
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

    public List<OcorrenciaFornecedor> listarOcorrencias(Long fornecedorId) throws IOException, ParseException {
        return ocorrenciaRepository.listarOcorrencias(fornecedorId);
    }
}
