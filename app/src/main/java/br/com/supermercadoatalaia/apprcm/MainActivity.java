package br.com.supermercadoatalaia.apprcm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.supermercadoatalaia.apprcm.adapter.LancamentoColetaAdapter;
import br.com.supermercadoatalaia.apprcm.controller.ColetaController;
import br.com.supermercadoatalaia.apprcm.controller.FornecedorController;
import br.com.supermercadoatalaia.apprcm.controller.PedidoController;
import br.com.supermercadoatalaia.apprcm.controller.ProdutoController;
import br.com.supermercadoatalaia.apprcm.core.ApiConsumer;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.Fornecedor;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;
import br.com.supermercadoatalaia.apprcm.domain.model.OcorrenciaFornecedor;
import br.com.supermercadoatalaia.apprcm.domain.model.Pedido;
import br.com.supermercadoatalaia.apprcm.domain.model.ProdUnidade;

public class MainActivity extends AppCompatActivity {

    private ColetaController coletaController;
    private FornecedorController fornecedorController;
    private PedidoController pedidoController;
    private ProdutoController produtoController;

    public static final int REQUEST_LEITURA = 21;
    public static final int REQUEST_CONSULTA = 22;
    private static final int PERMISSAO_IO = 1;

    private Coleta coleta;
    private LancamentoColeta item;
    private List<Pedido> pedidos;
    private Fornecedor fornecedor;
    private ProdUnidade produto;
    private Long numeroNotaFiscal;
    private String unidade;
    private Double qntNaEmb;
    private Double qntEmb;
    private Double qntTotal;
    private Calendar vencimento;

    private EditText edtCnpjCfp;
    private EditText edtNumeroNotaFiscal;
    private EditText edtEan;
    private EditText edtQntNaEmb;
    private EditText edtQntEmb;
    private EditText edtQntTotal;

    private TextView txvRazaoSocial;
    private TextView txvDataMvto;
    private TextView txvPedidoId;
    private TextView txvDescricao;
    private TextView txvUnidade;
    private TextView txvUsuario;

    private Button btnBuscarColeta;
    private Button btnAlterarColeta;
    private Button btnExcluirColeta;
    private Button btnAlterarItem;
    private Button btnExcluirItem;
    private Button btnLimpar;
    private Button btnLancar;
    private Button btnNovaColeta;
    private Button btnIniciarColeta;
    private Button btnLogout;

    private DatePicker dpkValidade;

    private ListView listLancamentoColeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coletaController = new ColetaController(getApplicationContext());
        fornecedorController = new FornecedorController(getApplicationContext());
        pedidoController = new PedidoController(getApplicationContext());
        produtoController = new ProdutoController(getApplicationContext());

        initComponents();
        initPermissoes();

        iniciarNovaColeta();
    }

    private void setNumeroNotaFiscal(Long numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    private void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    private void setColeta(Coleta coleta) {
        this.coleta = coleta;
    }

    private void setItem(LancamentoColeta item) {
        this.item = item;
    }

    private void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    private void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    private void setProduto(ProdUnidade produto) {
        this.produto = produto;
    }

    private void iniciarNovaColeta() {
        mudarBotoesNovaColeta();

        setColeta(new Coleta());
        numeroNotaFiscal = 0L;
        unidade = "";

        setProduto(new ProdUnidade());
        iniciarNovoItem();

        edtCnpjCfp.setText("");
        edtNumeroNotaFiscal.setText("");
        txvDataMvto.setText("");
        txvPedidoId.setText("");
        txvRazaoSocial.setText("");

        edtCnpjCfp.requestFocus();
    }

    private void mudarBotoesNovaColeta() {
        btnIniciarColeta.setEnabled(true);
        btnBuscarColeta.setEnabled(true);

        btnAlterarColeta.setEnabled(false);
        btnAlterarColeta.setText(R.string.botao_alterar);
        btnExcluirColeta.setEnabled(false);
        btnExcluirColeta.setText(R.string.botao_excluir);

        listLancamentoColeta.setEnabled(false);

        habilitarCamposColeta();
        desabilitarBotoesItem();
        desabilitarCamposItem();
    }

    private void mudarBotoesIniciarColeta() {
        btnAlterarColeta.setEnabled(true);
        btnAlterarColeta.setText(R.string.botao_alterar);
        btnExcluirColeta.setEnabled(true);
        btnExcluirColeta.setText(R.string.botao_excluir);

        listLancamentoColeta.setEnabled(true);

        btnIniciarColeta.setEnabled(false);
        btnBuscarColeta.setEnabled(false);

        mudarBotoesIniciarItem();
    }

    private void mudarBotoesAlterarColeta() {
        btnAlterarColeta.setEnabled(true);
        btnAlterarColeta.setText(R.string.botao_salvar);
        btnExcluirColeta.setEnabled(true);
        btnExcluirColeta.setText(R.string.botao_cancelar);

        btnIniciarColeta.setEnabled(false);
        btnBuscarColeta.setEnabled(false);

        listLancamentoColeta.setEnabled(false);

        desabilitarBotoesItem();
        desabilitarCamposItem();
    }

    private void mudarBotoesIniciarItem() {
        listLancamentoColeta.setEnabled(true);

        btnLancar.setEnabled(true);
        btnLimpar.setEnabled(true);
        btnLimpar.setText(R.string.botao_limpar);

        btnAlterarItem.setEnabled(false);
        btnAlterarItem.setText(R.string.botao_alterar);
        btnExcluirItem.setEnabled(false);

        habilitarCamposItem();
    }

    private void mudarBotoesAlterarItem() {
        btnAlterarItem.setEnabled(true);
        btnAlterarItem.setText(R.string.botao_salvar);
        btnLimpar.setEnabled(true);
        btnLimpar.setText(R.string.botao_cancelar);

        btnLancar.setEnabled(false);
        btnExcluirItem.setEnabled(false);

        habilitarCamposItem();
    }

    private void mudarBotoesClickList() {
        habilitarBotoesItem();
        desabilitarCamposItem();

        btnLancar.setEnabled(false);
    }

    private void habilitarCamposColeta() {
        edtCnpjCfp.setEnabled(true);
        edtNumeroNotaFiscal.setEnabled(true);
    }

    private void desabilitarBotoesItem() {
        btnLancar.setEnabled(false);
        btnLimpar.setEnabled(false);
        btnAlterarItem.setEnabled(false);
        btnExcluirItem.setEnabled(false);
    }

    private void habilitarBotoesItem() {
        btnLancar.setEnabled(true);
        btnLimpar.setEnabled(true);
        btnAlterarItem.setEnabled(true);
        btnExcluirItem.setEnabled(true);
    }

    private void desabilitarCamposItem() {
        edtEan.setEnabled(false);
        edtQntEmb.setEnabled(false);
        edtQntNaEmb.setEnabled(false);
        edtQntTotal.setEnabled(false);
        dpkValidade.setEnabled(false);
    }

    private void habilitarCamposItem() {
        edtEan.setEnabled(true);
        edtQntEmb.setEnabled(true);
        edtQntNaEmb.setEnabled(true);
        edtQntTotal.setEnabled(true);
        dpkValidade.setEnabled(true);
    }

    private void iniciarNovoItem() {
        listLancamentoColeta.setAdapter(
                new LancamentoColetaAdapter(coleta.getItens(), this)
        );

        habilitarCamposItem();

        vencimento = Calendar.getInstance();

        qntEmb = 0D;
        qntNaEmb = 0D;
        qntTotal = 0D;

        edtEan.setText("");
        txvDescricao.setText("");
        edtQntEmb.setText("");
        edtQntNaEmb.setText("");
        edtQntTotal.setText("");

        dpkValidade.updateDate(
                vencimento.get(Calendar.YEAR),
                vencimento.get(Calendar.MONTH),
                vencimento.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void preencherCampos() throws ApiException, IOException {
        setFornecedor(new Fornecedor());
        setFornecedor(fornecedorController.buscarPorId(coleta.getFornecedorId()));
        setPedidos(new ArrayList<Pedido>());
        setPedidos(pedidoController.buscarPorFornecedorNotaFiscal(fornecedor.getId() ,coleta.getNumeroNotaFiscal()));

        edtCnpjCfp.setText(fornecedor.getCnpjCpf());
        edtNumeroNotaFiscal.setText(
                String.valueOf(pedidos.get(0).getNotaFiscalBaixada())
        );
        txvRazaoSocial.setText(fornecedor.getRazaoSocial());
        txvDataMvto.setText(String.format(
                "%1$td/%1$tm/%1$ty %1$tH:%1$tM",
                coleta.getDataMovimento()
                )
        );
        txvPedidoId.setText(
                setPedidoIds(pedidos).toString()
        );
        txvUnidade.setText("Unid." + pedidos.get(0).getUnidade());

        edtCnpjCfp.setEnabled(false);
        edtNumeroNotaFiscal.setEnabled(false);

        listLancamentoColeta.setAdapter(
                new LancamentoColetaAdapter(coleta.getItens(), this)
        );
    }

    private void preencherCamposItensDoList() {
        ProdUnidade prod = new ProdUnidade();
        prod.setId(item.getProdutoId());
        prod.setEan(item.getProdutoEan());
        prod.setDescricao(item.getProdutoDescricao());
        prod.setDiasValidadeMinima(item.getDiasValidadeMinima());

        setProduto(prod);

        qntTotal = item.getQntTotal();
        qntNaEmb = item.getQntNaEmb();
        qntEmb = item.getQntEmb();
        vencimento = item.getVencimento();

        preencherCamposItem();
        desabilitarCamposItem();
    }

    private void preencherCamposItem() {
        edtEan.setText(produto.getEan());
        txvDescricao.setText(produto.getDescricao());

        if(qntTotal > 0) {
            edtQntTotal.setText(
                    String.format("%1$,.2f", qntTotal)
            );
        } else {
            edtQntTotal.setText("");
        }
        if(qntNaEmb > 0) {
            edtQntNaEmb.setText(
                    String.format("%1$,.2f", qntNaEmb)
            );
        } else {
            edtQntNaEmb.setText("");
        }
        if(qntEmb > 0) {
            edtQntEmb.setText(
                    String.format("%1$,.2f", qntEmb)
            );
        } else {
            edtQntEmb.setText("");
        }

        dpkValidade.updateDate(
                vencimento.get(Calendar.YEAR),
                vencimento.get(Calendar.MONTH),
                vencimento.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void setItemParaSalvar() throws ApiException, IOException {
        setQntsValidadeItem();

        setItem(
                new LancamentoColeta(
                    null,
                    produto.getId(),
                    produto.getDescricao(),
                    qntNaEmb,
                    qntEmb,
                    qntTotal,
                    vencimento,
                    produto.getDiasValidadeMinima(),
                    produto.getEan(),
                    null
                )
        );
    }

    private void setItemParaAlterar() throws ApiException, IOException {
        setQntsValidadeItem();

        setItem(
                new LancamentoColeta(
                        item.getId(),
                        produto.getId(),
                        produto.getDescricao(),
                        qntNaEmb,
                        qntEmb,
                        qntTotal,
                        vencimento,
                        produto.getDiasValidadeMinima(),
                        produto.getEan(),
                        item.getDataAlteracao()
                )
        );
    }

    private void setQntsValidadeItem() throws ApiException, IOException {
        if(edtQntNaEmb.getText().toString().isEmpty()) {
            qntNaEmb = 0D;
        } else {
            qntNaEmb = Double.valueOf(edtQntNaEmb.getText().toString());
        }
        if(edtQntEmb.getText().toString().isEmpty()) {
            qntEmb = 0D;
        } else {
            qntEmb = Double.valueOf(edtQntEmb.getText().toString());
        }
        if(edtQntTotal.getText().toString().isEmpty()) {
            qntTotal = 0D;
        } else {
            qntTotal = Double.valueOf(edtQntTotal.getText().toString());
        }
        vencimento.set(dpkValidade.getYear(), dpkValidade.getMonth(), dpkValidade.getDayOfMonth());

        buscarProdutoPorEan();
    }

    private void buscarProdutoPorEan() throws ApiException, IOException {
        String ean = edtEan.getText().toString();
        edtEan.setText(ean13(ean));

        if(!produto.getEan().equals(edtEan.getText().toString())) {
            setProduto(new ProdUnidade());
            setProduto(produtoController.buscarPorEan(ean, unidade));
        }
    }

    private void logout() {
        ApiConsumer apiConsumer = new ApiConsumer();

        try {
            apiConsumer.iniciarConexao(
                    "GET",
                    new URL(ApiConsumer.LOGOUT),
                    getApplicationContext()
            );
            apiConsumer.processarComResposta();
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        } catch (IOException e) {

        }
    }

    private String ean13(String ean) {

        while(ean.length() < 13) {
            ean = "0" + ean;
        }

        return ean;
    }

    private Set<Long> setPedidoIds(List<Pedido> pedidos) {
        Set<Long> ids = new HashSet<>();

        for(Pedido p : pedidos) {
            ids.add(p.getId());
        }

        return ids;
    }

    private void setColetaParaSalvar() throws ApiException, IOException, NumberFormatException, ParseException {
        setFornecedorPedidoUnidade();

        setColeta(
                new Coleta(
                        null,
                        fornecedor.getId(),
                        numeroNotaFiscal,
                        pedidos.get(0).getSerie(),
                        setPedidoIds(pedidos),
                        new ArrayList<LancamentoColeta>(),
                        null,
                        null,
                        unidade
                )
        );
    }

    private void setColetaParaAlterar() throws ApiException, IOException, NumberFormatException, ParseException {
        setFornecedorPedidoUnidade();

        setColeta(
                new Coleta(
                        coleta.getId(),
                        fornecedor.getId(),
                        numeroNotaFiscal,
                        pedidos.get(0).getSerie(),
                        setPedidoIds(pedidos),
                        coleta.getItens(),
                        coleta.getDataMovimento(),
                        coleta.getDataAlteracao(),
                        unidade
                )
        );
    }

    private void setFornecedorPedidoUnidade()
            throws ApiException, IOException, ParseException {
        String cnpjCfp = edtCnpjCfp.getText().toString();

        try {
            setNumeroNotaFiscal(
                    Long.valueOf(edtNumeroNotaFiscal.getText().toString())
            );
        } catch(NumberFormatException e) {
            setNumeroNotaFiscal(0L);
            edtNumeroNotaFiscal.setText("0");
        }

        setFornecedor(new Fornecedor());

        if(cnpjCfp.length() >= 14) {
            setFornecedor(fornecedorController.buscarPorCnpjCpf(cnpjCfp));
        } else {
            setFornecedor(fornecedorController.buscarPorId(Long.valueOf(cnpjCfp)));
        }

        setPedidos(new ArrayList<Pedido>());
        setPedidos(
            pedidoController.buscarPorFornecedorNotaFiscal(
                fornecedor.getId(),
                numeroNotaFiscal
            )
        );

        setUnidade(pedidos.get(0).getUnidade());

        try {
            List<OcorrenciaFornecedor> ocorrencias =
                    fornecedorController.listarOcorrencias(fornecedor.getId())
            ;

            for(OcorrenciaFornecedor ocorrencia : ocorrencias) {
                CaixaDialogoOcorrencia dialogo = new CaixaDialogoOcorrencia(
                        "Ocorrência para o fornecedor " + fornecedor.getId(),
                        ocorrencia
                );

                dialogo.show(getSupportFragmentManager(), "DialogoOcorrencia");
            }
        } catch(ApiException e) {
            e.printStackTrace();// coloquei para ver que erro dá aqui nas ocorrencias
        } catch(Exception ea) {
            ea.printStackTrace();
        }
    }

    private void deletarColeta() {
        String repDeletar = getResources().getString(R.string.botao_excluir);

        if(btnExcluirColeta.getText().toString().equals(repDeletar)) {
            try {
                coletaController.deletarColeta(coleta);
                mudarBotoesNovaColeta();
                Toast.makeText(this, "Deletado!!!", Toast.LENGTH_LONG).show();
            } catch (ApiException apie) {
                Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Erro ao deletar coleta!!!\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        iniciarNovaColeta();
    }

    private void atualizarColeta() {
        String repAlterar = getResources().getString(R.string.botao_alterar);
        if(btnAlterarColeta.getText().toString().equals(repAlterar)) {
            habilitarCamposColeta();
            mudarBotoesAlterarColeta();
        } else {
            try {
                setColetaParaAlterar();
                coleta = coletaController.atualizarColeta(coleta);
                mudarBotoesIniciarColeta();
                preencherCampos();
            } catch (ApiException apie) {
                Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
            } catch (IOException | ParseException e) {
                Toast.makeText(this, "Erro ao alterar coleta!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void salvarColeta() {
        try {
            setColetaParaSalvar();
            coleta = coletaController.salvarColeta(coleta);
            preencherCampos();
            mudarBotoesIniciarColeta();
            iniciarNovoItem();
            edtEan.requestFocus();
        } catch (ApiException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException | ParseException e) {
            Toast.makeText(this, "Erro ao salvar coleta!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void salvarItemColeta() {
        try {
            setItemParaSalvar();
            item = coletaController.salvarItemColeta(coleta, item);
            coleta.getItens().add(item);
            mudarBotoesIniciarItem();
            iniciarNovoItem();
        } catch (ApiException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException | ParseException e) {
            Toast.makeText(this, "Erro ao salvar item!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void atualizarItemColeta() {
        String repAlterar = getResources().getString(R.string.botao_alterar);
        if(btnAlterarItem.getText().toString().equals(repAlterar)) {
            mudarBotoesAlterarItem();
        } else {
            try {
                setItemParaAlterar();
                int indexItem = coleta.getItens().indexOf(item);
                item = coletaController.atualizarItemColeta(coleta, item);
                coleta.getItens().set(indexItem, item);
                mudarBotoesIniciarItem();
                iniciarNovoItem();
            } catch (ApiException apie) {
                Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
            } catch (IOException | ParseException e) {
                Toast.makeText(this, "Erro ao alterar item!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void deletarItemColeta() {
        try {
            int indexItem = coleta.getItens().indexOf(item);
            coletaController.deletarItemColeta(coleta, item);
            coleta.getItens().remove(indexItem);
            mudarBotoesIniciarItem();
            iniciarNovoItem();
        } catch (ApiException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao deletar item!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscarColetaPorFornecedorNotaFiscal() {
        try {
            setFornecedorPedidoUnidade();
            coleta = coletaController.buscarPorFornecedorNotaFiscal(fornecedor.getId(), numeroNotaFiscal);
            preencherCampos();
            mudarBotoesIniciarColeta();
        } catch (ApiException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Verifique o cnpj e núm. da NF digitado!\n"+nfe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException | ParseException e) {
            Toast.makeText(this, "Erro ao buscar coleta!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void abrirLeitura() {
        Intent intent = new Intent(this, LeitorActivity.class);

        startActivityForResult(intent, REQUEST_LEITURA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LEITURA:
                if (resultCode == LeitorActivity.SUCESSO) {
                    iniciarNovoItem();

                    //Dados vindo da intent chamada em modo de espera de resultado.
                    if (data != null) {
                        edtEan.setText(data.getStringExtra(LeitorActivity.LEITURA));
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Nenhum EAN capturado!",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
                break;
            case REQUEST_CONSULTA:
                //Faz outra coisa
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void initComponents() {
        setContentView(R.layout.activity_main);

        edtCnpjCfp = findViewById(R.id.edtCnpjCfp);
        edtNumeroNotaFiscal = findViewById(R.id.edtNumeroNotaFiscal);
        edtEan = findViewById(R.id.edtEan);
        edtQntNaEmb = findViewById(R.id.edtQntNaEmb);
        edtQntEmb = findViewById(R.id.edtQntEmb);
        edtQntTotal = findViewById(R.id.edtQntTotal);

        txvRazaoSocial = findViewById(R.id.txvRazaoSocial);
        txvDataMvto = findViewById(R.id.txvDataMvto);
        txvPedidoId = findViewById(R.id.txvPedidoId);
        txvDescricao = findViewById(R.id.txvDescricao);
        txvUnidade = findViewById(R.id.txvUnidade);
        txvUsuario = findViewById(R.id.txvUsuario);

        btnBuscarColeta = findViewById(R.id.btnBuscarColeta);
        btnAlterarColeta = findViewById(R.id.btnAlterarColeta);
        btnExcluirColeta = findViewById(R.id.btnExcluirColeta);
        btnAlterarItem = findViewById(R.id.btnAlterarItem);
        btnExcluirItem = findViewById(R.id.btnExcluirItem);
        btnLimpar = findViewById(R.id.btnLimpar);
        btnLancar = findViewById(R.id.btnLancar);
        btnNovaColeta = findViewById(R.id.btnNovaColeta);
        btnIniciarColeta = findViewById(R.id.btnIniciarColeta);
        btnLogout = findViewById(R.id.btnLogout);

        dpkValidade = findViewById(R.id.dpkValidade);

        listLancamentoColeta = findViewById(R.id.listLancamentoColeta);

        btnBuscarColeta.setOnClickListener(btnBuscarColetaPorFornecedorNotaFiscal_Click());
        btnAlterarColeta.setOnClickListener(btnAlterarColeta_Click());
        btnExcluirColeta.setOnClickListener(btnExcluirColeta_Click());
        btnAlterarItem.setOnClickListener(btnAlterarItemColeta_Click());
        btnExcluirItem.setOnClickListener(btnExcluirItemColeta_Click());
        btnLimpar.setOnClickListener(btnLimpar_Click());
        btnLancar.setOnClickListener(btnSalvarItemColeta_Click());
        btnIniciarColeta.setOnClickListener(btnSalvarColeta_Click());
        btnNovaColeta.setOnClickListener(btnNovaColeta_Click());
        btnLogout.setOnClickListener(btnLogout_Click());

        edtEan.setOnFocusChangeListener(edtEan_FocusChange());

        listLancamentoColeta.setOnItemClickListener(listLancamentoColeta_ItemClick());

        txvUsuario.setText(
                "Usuário: " +
                SharedPrefManager.getInstance(getApplicationContext()).getUsuario().getNome()
        );
    }

    private void initPermissoes () {
        //USUARIO DAR A PERMISSAO PARA LER
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_IO);
            }
        }

        //USUARIO DAR A PERMISSAO PARA ESCREVER
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_IO);
            }
        }
    }

    private AdapterView.OnItemClickListener listLancamentoColeta_ItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setItem((LancamentoColeta) adapterView.getItemAtPosition(i));
                mudarBotoesClickList();
                preencherCamposItensDoList();
            }
        };
    }

    private boolean isFocusBuscaEan() {
        return edtQntTotal.isFocused() ||
                edtQntNaEmb.isFocused() ||
                edtQntEmb.isFocused() ||
                btnLancar.isFocused() ||
                btnAlterarItem.isFocused();
    }

    private View.OnFocusChangeListener edtEan_FocusChange() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    abrirLeitura();
                } else if(isFocusBuscaEan()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                buscarProdutoPorEan();
                                preencherCamposItem();
                            } catch (ApiException apie) {
                                Toast.makeText(getApplicationContext(), apie.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Erro ao buscar produto!!!\n"+e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                    });
                }
            }
        };
    }

    private View.OnClickListener btnNovaColeta_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarNovaColeta();
            }
        };
    }

    private View.OnClickListener btnLogout_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        };
    }

    private View.OnClickListener btnLimpar_Click() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mudarBotoesIniciarItem();
                iniciarNovoItem();
            }
        };
    }

    private View.OnClickListener btnBuscarColetaPorFornecedorNotaFiscal_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buscarColetaPorFornecedorNotaFiscal();
                    }
                });
            }
        };
    }

    private View.OnClickListener btnExcluirItemColeta_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deletarItemColeta();
                    }
                });
            }
        };
    }

    private View.OnClickListener btnAlterarItemColeta_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        atualizarItemColeta();
                    }
                });
            }
        };
    }

    private View.OnClickListener btnSalvarItemColeta_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        salvarItemColeta();
                    }
                });
            }
        };
    }

    private View.OnClickListener btnSalvarColeta_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        salvarColeta();
                    }
                });
            }
        };
    }

    private View.OnClickListener btnAlterarColeta_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        atualizarColeta();
                    }
                });
            }
        };
    }

    private View.OnClickListener btnExcluirColeta_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deletarColeta();
                    }
                });
            }
        };
    }
}
