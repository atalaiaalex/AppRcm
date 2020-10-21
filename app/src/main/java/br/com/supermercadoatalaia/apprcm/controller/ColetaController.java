package br.com.supermercadoatalaia.apprcm.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.core.HttpResposta;
import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;
import br.com.supermercadoatalaia.apprcm.domain.repository.ColetaRepository;

public class ColetaController {

    private final ColetaRepository coletaRepository;

    public ColetaController(ConfigApp configApp) throws IOException {
        coletaRepository = new ColetaRepository(configApp);
    }

    public Coleta buscarPorId(Long id) throws IOException, ParseException {
        return coletaRepository.buscar(id);
    }

    public Coleta buscarPorFornecedorNotaFiscal(Long fornecedorId, Long numeroNotaFiscal)
            throws IOException, ParseException {
        return coletaRepository.buscar(fornecedorId, numeroNotaFiscal);
    }

    public List<Coleta> listarPorFornecedor(Long fornecedorId) throws IOException, ParseException {
        return coletaRepository.listarPorFornecedor(fornecedorId);
    }

    public List<Coleta> listarPorNotaFiscal(Long numeroNotaFiscal) throws IOException, ParseException {
        return coletaRepository.listarPorNf(numeroNotaFiscal);
    }

    public Coleta salvarColeta(Coleta coleta) throws IOException, ParseException {
        return coletaRepository.salvar(coleta);
    }

    public LancamentoColeta salvarItemColeta(Coleta coleta, LancamentoColeta item)
            throws IOException, ParseException {
        return coletaRepository.salvarItem(coleta, item);
    }

    public Coleta atualizarColeta(Coleta coleta) throws IOException, ParseException {
        return coletaRepository.atualizar(coleta);
    }

    public LancamentoColeta atualizarItemColeta(Coleta coleta, LancamentoColeta item)
            throws IOException, ParseException {
        return coletaRepository.atualizarItem(coleta, item);
    }

    public HttpResposta deletarColeta(Coleta coleta) throws IOException {
        return coletaRepository.deletar(coleta);
    }

    public HttpResposta deletarItemColeta(Coleta coleta, LancamentoColeta item) throws IOException {
        return coletaRepository.deletarItem(coleta, item);
    }
}
