package br.com.supermercadoatalaia.apprcm.controller;

import android.content.Context;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.HttpResposta;
import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;
import br.com.supermercadoatalaia.apprcm.domain.repository.ColetaRepository;

public class ColetaController {

    private final ColetaRepository coletaRepository;

    public ColetaController(Context context) {
        coletaRepository = new ColetaRepository(context);
    }

    public Coleta buscarPorId(Long id) throws ApiException, IOException, ParseException {
        return coletaRepository.buscar(id);
    }

    public Coleta buscarPorFornecedorNotaFiscal(Long fornecedorId, Long numeroNotaFiscal)
            throws ApiException, IOException, ParseException {
        return coletaRepository.buscar(fornecedorId, numeroNotaFiscal);
    }

    public List<Coleta> listarPorFornecedor(Long fornecedorId)
            throws ApiException, IOException, ParseException {
        return coletaRepository.listarPorFornecedor(fornecedorId);
    }

    public List<Coleta> listarPorNotaFiscal(Long numeroNotaFiscal)
            throws ApiException, IOException, ParseException {
        return coletaRepository.listarPorNf(numeroNotaFiscal);
    }

    public Coleta salvarColeta(Coleta coleta) throws ApiException, IOException, ParseException {
        return coletaRepository.salvar(coleta);
    }

    public void salvarColetaFlex(Coleta coleta, LancamentoColeta item) throws IOException, ParseException {
        coletaRepository.salvarRCMFlex(coleta, item);
        coletaRepository.atualizar(coleta);
    }

    public LancamentoColeta salvarItemColeta(Coleta coleta, LancamentoColeta item)
            throws ApiException, IOException, ParseException {
        return coletaRepository.salvarItem(coleta, item);
    }

    public Coleta atualizarColeta(Coleta coleta) throws ApiException, IOException, ParseException {
        return coletaRepository.atualizar(coleta);
    }

    public LancamentoColeta atualizarItemColeta(Coleta coleta, LancamentoColeta item)
            throws ApiException, IOException, ParseException {
        return coletaRepository.atualizarItem(coleta, item);
    }

    public HttpResposta deletarColeta(Coleta coleta) throws ApiException, IOException {
        return coletaRepository.deletar(coleta);
    }

    public HttpResposta deletarItemColeta(Coleta coleta, LancamentoColeta item)
            throws ApiException, IOException {
        return coletaRepository.deletarItem(coleta, item);
    }
}
