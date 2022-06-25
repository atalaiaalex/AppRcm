package br.com.supermercadoatalaia.apprcm.config;

import br.com.supermercadoatalaia.apprcm.domain.service.ColetaService;
import br.com.supermercadoatalaia.apprcm.domain.service.FornecedorService;
import br.com.supermercadoatalaia.apprcm.domain.service.LoginService;
import br.com.supermercadoatalaia.apprcm.domain.service.PedidoCompraService;
import br.com.supermercadoatalaia.apprcm.domain.service.ProdutoService;
import br.com.supermercadoatalaia.apprcm.domain.service.UsuarioService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitAtalaiaConfig {
    private final Retrofit retrofit;

    public RetrofitAtalaiaConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(ConfigApp.SERVER)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public LoginService getLoginService() {
        return this.retrofit.create(LoginService.class);
    }

    public ColetaService getColetaService() {
        return this.retrofit.create(ColetaService.class);
    }

    public FornecedorService getFornecedorService() {
        return this.retrofit.create(FornecedorService.class);
    }

    public UsuarioService getUsuarioService() {
        return this.retrofit.create(UsuarioService.class);
    }

    public PedidoCompraService getPedidoCompraService() {
        return this.retrofit.create(PedidoCompraService.class);
    }

    public ProdutoService getProdutoService() {
        return this.retrofit.create(ProdutoService.class);
    }
}
