package br.com.supermercadoatalaia.apprcm.controller;

import android.content.Context;

import java.util.List;

import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;
import br.com.supermercadoatalaia.apprcm.domain.repository.ColetaRepository;
import br.com.supermercadoatalaia.apprcm.dto.help.MessageResponse;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;
import br.com.supermercadoatalaia.apprcm.exception.RequestFailureException;

public class ColetaController {

    private final ColetaRepository coletaRepository;

    public ColetaController(Context context) {
        coletaRepository = new ColetaRepository(context);
    }

    public Coleta buscarPorId(Long id) throws RegistroNotFoundException {
        return coletaRepository.buscar(id);
    }

    public Coleta buscarPorFornecedorNotaFiscal(Long fornecedorId, Long numeroNotaFiscal)
            throws RegistroNotFoundException {

        return coletaRepository.buscar(fornecedorId, numeroNotaFiscal);
    }

    public List<Coleta> listarPorFornecedor(Long fornecedorId) throws RegistroNotFoundException {
        return coletaRepository.listarPorFornecedor(fornecedorId);
    }

    public List<Coleta> listarPorNotaFiscal(Long numeroNotaFiscal) throws RegistroNotFoundException {
        return coletaRepository.listarPorNf(numeroNotaFiscal);
    }

    public MessageResponse salvarColeta(Coleta coleta) throws RequestFailureException {
        return coletaRepository.salvar(coleta);
    }

    public MessageResponse salvarColetaFlex(Coleta coleta, LancamentoColeta item)
            throws RequestFailureException {

        coletaRepository.salvarRCMFlex(coleta, item);
        return coletaRepository.atualizar(coleta);
    }

    public MessageResponse salvarItemColeta(Coleta coleta, LancamentoColeta item)
            throws RequestFailureException {

        return coletaRepository.salvarItem(coleta, item);
    }

    public MessageResponse atualizarColeta(Coleta coleta) throws RequestFailureException {
        return coletaRepository.atualizar(coleta);
    }

    public MessageResponse atualizarItemColeta(Coleta coleta, LancamentoColeta item)
            throws RequestFailureException {

        return coletaRepository.atualizarItem(coleta, item);
    }

    public void deletarColeta(Coleta coleta) throws RequestFailureException {
        coletaRepository.deletar(coleta);
    }

    public void deletarItemColeta(Coleta coleta, LancamentoColeta item) throws RequestFailureException {
        coletaRepository.deletarItem(coleta, item);
    }
}
