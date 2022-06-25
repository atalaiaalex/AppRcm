package br.com.supermercadoatalaia.apprcm.domain.repository;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.config.RetrofitAtalaiaConfig;
import br.com.supermercadoatalaia.apprcm.config.RetrofitFlexConfig;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;
import br.com.supermercadoatalaia.apprcm.domain.model.RCMFlex;
import br.com.supermercadoatalaia.apprcm.domain.model.RCMProduto;
import br.com.supermercadoatalaia.apprcm.domain.model.RespostaRCMInserir;
import br.com.supermercadoatalaia.apprcm.domain.service.ColetaService;
import br.com.supermercadoatalaia.apprcm.dto.help.MessageResponse;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;
import br.com.supermercadoatalaia.apprcm.exception.RequestFailureException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ColetaRepository {

    private final ColetaService service;
    private final Context context;

    public ColetaRepository(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.service = new RetrofitAtalaiaConfig().getColetaService();
        this.context = context;
    }

    public Coleta buscar(Long id) throws RegistroNotFoundException {
        Call<Coleta> call = service.buscarColeta(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                id
        );

        Response<Coleta> coleta;
        try {
            coleta = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar coleta", e);
        }

        if(coleta.isSuccessful()) {
            return coleta.body();
        }

        throw new RegistroNotFoundException("Coleta não encontrado");
    }

    public List<Coleta> listarPorFornecedor(Long fornecedorId)
            throws RegistroNotFoundException {

        Call<List<Coleta>> call = service.listarColetasPorFornecedor(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                fornecedorId
        );

        Response<List<Coleta>> coletas;
        try {
            coletas = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar coletas", e);
        }

        if(coletas.isSuccessful()) {
            return coletas.body();
        }

        throw new RegistroNotFoundException("Coletas do fornecedor não encontrado");
    }

    public List<Coleta> listarPorNf(Long numeroNotaFiscal)
            throws RegistroNotFoundException {

        Call<List<Coleta>> call = service.listarColetasPorNotaFiscal(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                numeroNotaFiscal
        );

        Response<List<Coleta>> coletas;
        try {
            coletas = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar coletas", e);
        }

        if(coletas.isSuccessful()) {
            return coletas.body();
        }

        throw new RegistroNotFoundException("Coletas da NF não encontrado");
    }

    public Coleta buscar(Long fornecedorId, Long numeroNotaFiscal)
            throws RegistroNotFoundException {

        Call<Coleta> call = service.listarColetasPorFornecedorENotaFiscal(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                fornecedorId,
                numeroNotaFiscal
        );

        Response<Coleta> coleta;
        try {
            coleta = call.execute();
        } catch (IOException e) {
            throw new RegistroNotFoundException("Erro ao buscar coleta", e);
        }

        if(coleta.isSuccessful()) {
            return coleta.body();
        }

        throw new RegistroNotFoundException("Coleta do fornecedor e NF não encontrado");
    }

    public MessageResponse salvar(Coleta coleta) throws RequestFailureException {

        Call<MessageResponse> call = service.salvarColeta(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                coleta
        );

        Response<MessageResponse> coletaSalva;
        try {
            coletaSalva = call.execute();
        } catch (IOException e) {
            throw new RequestFailureException("Erro ao salvar coleta", e);
        }

        if(coletaSalva.isSuccessful()) {
            return coletaSalva.body();
        }

        throw new RequestFailureException("Não foi possível salvar a coleta");
    }

    public void salvarRCMFlex(Coleta coleta, LancamentoColeta item) {
        Call<RespostaRCMInserir> callRcm = new RetrofitFlexConfig()
                .getRCMFlexService().inserirRCM(
                        SharedPrefManager.getInstance(context).getTokenFlex(),
                        SharedPrefManager.getInstance(context).getCookieFlex(),
                        toRCMFlex(coleta, item)
                );

        Log.i("RCMService", "Iniciando processo de inserção");

        callRcm.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<RespostaRCMInserir> call, Response<RespostaRCMInserir> response) {
                RespostaRCMInserir respostaRCMInserir = response.body();
                Log.i("Transação RCM", respostaRCMInserir.getResponse().getTransacao());
            }

            @Override
            public void onFailure(Call<RespostaRCMInserir> call, Throwable t) {
                Log.e("RCMService", "Erro ao inserir coletagem :: " + t.getMessage());
            }
        });
    }

    private String unidadeToCnpj(String unidade) {
        switch(unidade) {
            case "001": return "07188408000269";
            case "002": return "07188408000188";
            case "003": return "12389990000180";
        }

        return "";
    }

    private Long unidadeToDcto(String unidade) {
        switch(unidade) {
            case "001": return 7523L;
            case "002": return 7524L;
            case "003": return 7525L;
        }

        return 0L;
    }

    private RCMFlex toRCMFlex(Coleta coleta, LancamentoColeta item) {
        List<RCMProduto> produtos = new ArrayList<>();

        produtos.add(new RCMProduto(
                    item.getProdutoId(),
                    (item.getQntEmb() == 0D ? item.getQntTotal() : item.getQntEmb()),
                    item.getQntNaEmb().longValue(),
                    String.format("%1$td/%1$tm/%1$tY", Calendar.getInstance()),
                    String.format("%1$td/%1$tm/%1$tY", item.getVencimento()),
                    0.0,
                    0L,
                    0L,
                    "0"
        ));

        Log.i("RCMProduto", produtos.get(0).toString());

        return new RCMFlex(
                unidadeToCnpj(coleta.getUnidade()),
                unidadeToDcto(coleta.getUnidade()),
                String.valueOf(coleta.getNumeroNotaFiscal()),
                "''",
                String.format("%1$td/%1$tm/%1$tY", coleta.getDataMovimento()),
                "FORNECEDOR",
                String.valueOf(coleta.getFornecedorId()),
                produtos
        );
    }

    public MessageResponse atualizar(Coleta coleta) throws RequestFailureException {

        Call<MessageResponse> call = service.atualizarColeta(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                coleta.getId(),
                coleta
        );

        Response<MessageResponse> coletaAtualizada;
        try {
            coletaAtualizada = call.execute();
        } catch (IOException e) {
            throw new RequestFailureException("Erro ao atualizar coleta", e);
        }

        if(coletaAtualizada.isSuccessful()) {
            return coletaAtualizada.body();
        }

        throw new RequestFailureException("Não foi possível atualizar a coleta");
    }

    public void deletar(Coleta coleta) throws RequestFailureException {
        Call<Void> call = service.deletarColeta(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                coleta.getId()
        );

        Response<Void> coletaDeletada;
        try {
            coletaDeletada = call.execute();
        } catch (IOException e) {
            throw new RequestFailureException("Erro ao deletar coleta", e);
        }

        if(!coletaDeletada.isSuccessful()) {
            throw new RequestFailureException("Não foi possível deletar a coleta");
        }
    }

    public MessageResponse salvarItem(Coleta coleta, LancamentoColeta item)
            throws RequestFailureException {

        Call<MessageResponse> call = service.salvarItem(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                coleta.getId(),
                item
        );

        Response<MessageResponse> itemSalvo;
        try {
            itemSalvo = call.execute();
        } catch (IOException e) {
            throw new RequestFailureException("Erro ao salvar item coleta", e);
        }

        if(itemSalvo.isSuccessful()) {
            return itemSalvo.body();
        }

        throw new RequestFailureException("Não foi possível salvar o item da coleta");
    }

    public MessageResponse atualizarItem(Coleta coleta, LancamentoColeta item)
            throws RequestFailureException {

        Call<MessageResponse> call = service.atualizarItem(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                coleta.getId(),
                item.getId(),
                item
        );

        Response<MessageResponse> itemAtualizado;
        try {
            itemAtualizado = call.execute();
        } catch (IOException e) {
            throw new RequestFailureException("Erro ao atualizar item", e);
        }

        if(itemAtualizado.isSuccessful()) {
            return itemAtualizado.body();
        }

        throw new RequestFailureException("Não foi possível atualizar o item");
    }

    public void deletarItem(Coleta coleta, LancamentoColeta item)
            throws RequestFailureException {

        Call<Void> call = service.deletarItem(
                SharedPrefManager.getInstance(context).getAuthorizationToken(),
                coleta.getId(),
                item.getId()
        );

        Response<Void> response;
        try {
            response = call.execute();
        }catch(IOException e) {
            throw new RequestFailureException("Erro ao deletar item", e);
        }

        if(!response.isSuccessful()) {
            throw new RequestFailureException("Erro ao deletar item");
        }
    }
}
