package br.com.supermercadoatalaia.apprcm.controller;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
import br.com.supermercadoatalaia.apprcm.domain.model.Pedido;
import br.com.supermercadoatalaia.apprcm.domain.repository.PedidoRepository;

public class PedidoController {
    private final PedidoRepository pedidoRepository;

    public PedidoController(Context context) {
        pedidoRepository = new PedidoRepository(context);
    }

    public Pedido buscarPorId(Long id) throws ApiException, IOException {
        return pedidoRepository.buscar(id);
    }

    public List<Pedido> buscarPorFornecedorNotaFiscal(Long fornecedorId, Long numeroNotaFiscal)
            throws ApiException, IOException {
        return pedidoRepository.buscar(fornecedorId, numeroNotaFiscal);
    }

    public List<Pedido> listarPorFornecedor(Long fornecedorId) throws ApiException, IOException {
        return pedidoRepository.listar(fornecedorId);
    }
}
