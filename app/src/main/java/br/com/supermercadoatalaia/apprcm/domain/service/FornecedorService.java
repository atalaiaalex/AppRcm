package br.com.supermercadoatalaia.apprcm.domain.service;

import java.util.List;

import br.com.supermercadoatalaia.apprcm.domain.model.Fornecedor;
import br.com.supermercadoatalaia.apprcm.domain.model.OcorrenciaFornecedor;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface FornecedorService {

    String END_POINT = "v1/fornecedores";

    @GET(END_POINT+"/{id}")
    Call<Fornecedor> buscarPorId(@Header("Authorization") String token,
                                 @Path("id") Long id);

    @GET(END_POINT+"/cnpj/{cnpjCpf}")
    Call<Fornecedor> buscarPorCnpj(@Header("Authorization") String token,
                                   @Path("cnpjCpf") String cnpjCpf);

    @GET(END_POINT+"/vinculo/{vinculoCodigo}")
    Call<List<Fornecedor>> listarVinculados(@Header("Authorization") String token,
                                            @Path("vinculoCodigo") Long vinculoCodigo);

    @GET(END_POINT+"/{id}/ocorrencias_pendente")
    Call<List<OcorrenciaFornecedor>> listarOcorrencias(@Header("Authorization") String token,
                                                       @Path("id") Long id);

}
