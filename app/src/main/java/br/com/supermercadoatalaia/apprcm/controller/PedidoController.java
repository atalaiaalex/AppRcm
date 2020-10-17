package br.com.supermercadoatalaia.apprcm.controller;

import java.io.IOException;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.domain.model.Pedido;
import br.com.supermercadoatalaia.apprcm.domain.repository.PedidoRepository;

public class PedidoController {
    private final PedidoRepository pedidoRepository;

    public PedidoController(ConfigApp configApp) throws IOException {
        pedidoRepository = new PedidoRepository(configApp);
    }

    public Pedido buscarPorId(Long id) throws IOException {
        return pedidoRepository.buscar(id);
    }

    public Pedido buscarPorFornecedorNotaFiscal(Long fornecedorId, Long numeroNotaFiscal)
            throws IOException {
        return pedidoRepository.buscar(fornecedorId, numeroNotaFiscal);
    }

    public List<Pedido> listarPorFornecedor(Long fornecedorId) throws IOException {
        return pedidoRepository.listar(fornecedorId);
    }
}
