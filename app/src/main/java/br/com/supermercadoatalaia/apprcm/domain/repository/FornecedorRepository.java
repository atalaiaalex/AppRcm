package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.content.Context;
import android.os.StrictMode;

import java.io.IOException;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.config.RetrofitAtalaiaConfig;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.domain.model.Fornecedor;
import br.com.supermercadoatalaia.apprcm.domain.model.OcorrenciaFornecedor;
import br.com.supermercadoatalaia.apprcm.domain.service.FornecedorService;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;
import retrofit2.Call;
import retrofit2.Response;

public class FornecedorRepository {

    private final Context context;
    private final FornecedorService service;

    public FornecedorRepository(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        service = new RetrofitAtalaiaConfig().getFornecedorService();
        this.context = context;
    }

    public Fornecedor buscar(Long id) throws RegistroNotFoundException {
        Call<Fornecedor> call = service.buscarPorId(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                id
        );

        Response<Fornecedor> fornecedor;
        try {
            fornecedor = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar fornecedor", e);
        }

        if(fornecedor.isSuccessful()) {
            return fornecedor.body();
        }

        throw new RegistroNotFoundException("Fornecedor não encontrado");
    }

    public Fornecedor buscar(String cnpjCpf) throws RegistroNotFoundException {
        Call<Fornecedor> call = service.buscarPorCnpj(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                cnpjCpf
        );

        Response<Fornecedor> fornecedor;
        try {
            fornecedor = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar fornecedor", e);
        }

        if(fornecedor.isSuccessful()) {
            return fornecedor.body();
        }

        throw new RegistroNotFoundException("Fornecedor não encontrado");
    }

    public List<Fornecedor> listar(Long vinculoCodigo) throws RegistroNotFoundException {
        Call<List<Fornecedor>> call = service.listarVinculados(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                vinculoCodigo
        );

        Response<List<Fornecedor>> fornecedores;
        try {
            fornecedores = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar fornecedor", e);
        }

        if(fornecedores.isSuccessful()) {
            return fornecedores.body();
        }

        throw new RegistroNotFoundException("Fornecedor não encontrado");
    }

    public List<OcorrenciaFornecedor> listarOcorrencias(Long fornecedorId)
            throws RegistroNotFoundException {

        Call<List<OcorrenciaFornecedor>> call = service.listarOcorrencias(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                fornecedorId
        );

        Response<List<OcorrenciaFornecedor>> ocorrencias;
        try {
            ocorrencias = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar ocorrencias", e);
        }

        if(ocorrencias.isSuccessful()) {
            return ocorrencias.body();
        }

        throw new RegistroNotFoundException("Ocorrencia não encontrado");
    }
}
