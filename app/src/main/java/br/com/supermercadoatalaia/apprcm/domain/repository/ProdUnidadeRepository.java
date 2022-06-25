package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.content.Context;

import java.io.IOException;

import br.com.supermercadoatalaia.apprcm.config.RetrofitAtalaiaConfig;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.domain.model.ProdUnidade;
import br.com.supermercadoatalaia.apprcm.domain.service.ProdutoService;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;
import retrofit2.Call;
import retrofit2.Response;

public class ProdUnidadeRepository {

    private final Context context;
    private final ProdutoService service;

    public ProdUnidadeRepository(Context context) {
        this.service = new RetrofitAtalaiaConfig().getProdutoService();
        this.context = context;
    }

    public ProdUnidade buscar(Long id, String unidade) throws RegistroNotFoundException {

        Call<ProdUnidade> call = service.buscarPorIdUnidade(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                id,
                unidade
        );

        Response<ProdUnidade> produto;
        try {
            produto = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar produto", e);
        }

        if(produto.isSuccessful()) {
            return produto.body();
        }

        throw new RegistroNotFoundException("Produto não encontrado");
    }

    public ProdUnidade buscar(String ean, String unidade) throws RegistroNotFoundException {

        Call<ProdUnidade> call = service.buscarPorEanUnidade(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                ean,
                unidade
        );

        Response<ProdUnidade> produto;
        try {
            produto = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar produto", e);
        }

        if(produto.isSuccessful()) {
            return produto.body();
        }

        throw new RegistroNotFoundException("Produto não encontrado");
    }
}
