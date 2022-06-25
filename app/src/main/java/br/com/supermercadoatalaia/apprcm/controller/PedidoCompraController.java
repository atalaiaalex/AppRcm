package br.com.supermercadoatalaia.apprcm.controller;

import android.content.Context;

import java.util.List;

import br.com.supermercadoatalaia.apprcm.domain.model.PedidoCompra;
import br.com.supermercadoatalaia.apprcm.domain.repository.PedidoCompraRepository;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;

public class PedidoCompraController {
    private final PedidoCompraRepository repository;

    public PedidoCompraController(Context context) {
        repository = new PedidoCompraRepository(context);
    }

    public PedidoCompra buscarPorId(Long id) throws RegistroNotFoundException {
        return repository.buscar(id);
    }

    public List<PedidoCompra> buscarPorFornecedorNotaFiscal(Long fornecedorId, Long numeroNotaFiscal)
            throws RegistroNotFoundException {

        return repository.buscar(fornecedorId, numeroNotaFiscal);
    }

    public List<PedidoCompra> listarPorFornecedor(Long fornecedorId)
            throws RegistroNotFoundException {

        return repository.listar(fornecedorId);
    }
}
