package br.com.supermercadoatalaia.apprcm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;

import br.com.supermercadoatalaia.apprcm.controller.ColetaController;
import br.com.supermercadoatalaia.apprcm.controller.FornecedorController;
import br.com.supermercadoatalaia.apprcm.controller.PedidoController;
import br.com.supermercadoatalaia.apprcm.controller.ProdutoController;
import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.core.RcmThreadFactory;
import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.Fornecedor;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;
import br.com.supermercadoatalaia.apprcm.domain.model.Pedido;
import br.com.supermercadoatalaia.apprcm.domain.model.ProdUnidade;

public class MainActivity extends AppCompatActivity {

    private ColetaController coletaController;
    private FornecedorController fornecedorController;
    private PedidoController pedidoController;
    private ProdutoController produtoController;
    private ConfigApp configApp;

    public static final int REQUEST_LEITURA = 21;
    public static final int REQUEST_CONSULTA = 22;
    private static final int PERMISSAO_IO = 1;

    private Coleta coleta;
    private LancamentoColeta item;
    private Pedido pedido;
    private Fornecedor fornecedor;
    private ProdUnidade produto;
    private Long numeroNotaFiscal;
    private String unidade;
    private Double qntNaEmb;
    private Double qntEmb;
    private Double qntTotal;

    private EditText edtCnpjCfp;
    private EditText edtNumeroNotaFiscal;
    private EditText edtEan;
    private EditText edtQntNaEmb;
    private EditText edtQntEmb;
    private EditText edtQntTotal;

    private Button btnBuscarColeta;
    private Button btnAlterarColeta;
    private Button btnExcluirColeta;
    private Button btnAlterarItem;
    private Button btnExcluirItem;
    private Button btnLimpar;

    private TextView txvRazaoSocial;
    private TextView txvDataMvto;
    private TextView txvPedidoId;
    private TextView txvDescricao;

    private ListView listLancamentoColeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initPermissoes();

        String pasta = getExternalFilesDir(ConfigApp.PASTA_CONFIG).getAbsolutePath();
        configApp = new ConfigApp(pasta);

        try {
            coletaController = new ColetaController(configApp);
            fornecedorController = new FornecedorController(configApp);
            pedidoController = new PedidoController(configApp);
            produtoController = new ProdutoController(configApp);
        } catch (IOException e) {
            configurar();
        }

    }

    /*
    Enter no edtNumeroNotaFiscal SalvaColeta
        focu no edtEan
    Entrada de foco no edtEan chama tela de leitura
        pela camera de codigo de barras
    Na tela de Leitura ter opção para digitar EAN manualmente
    Entrada no edtQntTotal desabilitará edtQntNaEmb e edtQntEmb
        se a multiplicação deles for zero, e o enter salvará o item
    Enter no edtQntNaEmb levará para o edtQntEmb
    Enter no edtQntEmb desabilitará o edtQntTotal se a
        multiplicação deles for maior que zero, e salvará o item
    Cada clique em um item(LancamentoColeta) vai instanciar o produto
        pelo Id
     */

    private void preencherCampos() throws IOException {
        limparCamposItens();

        setFornecedor(fornecedorController.buscarPorId(coleta.getFornecedorId()));
        setPedido(pedidoController.buscarPorId(coleta.getPedidoId()));

        edtCnpjCfp.setText(fornecedor.getCnpjCpf());
        edtNumeroNotaFiscal.setText(
                String.valueOf(pedido.getNotaFiscalBaixada())
        );
        txvRazaoSocial.setText(fornecedor.getRazaoSocial());
        txvDataMvto.setText(coleta.getDataMovimento().toString());
        txvPedidoId.setText(
                String.valueOf(coleta.getPedidoId())
        );

        //Preencher Adpater do List LancamentoColeta
    }

    private void preencherCamposItem() {
        edtEan.setText(produto.getEan());
        txvDescricao.setText(produto.getDescricao());
        edtQntTotal.setText(
                String.valueOf(qntTotal)
        );
        edtQntNaEmb.setText(
                String.valueOf(qntNaEmb)
        );
        edtQntEmb.setText(
                String.valueOf(qntEmb)
        );
    }

    private void setColeta(Coleta coleta) {
        this.coleta = coleta;
    }

    private void setItem(LancamentoColeta item) {
        this.item = item;
    }

    private void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    private void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    private void setProduto(ProdUnidade produto) {
        this.produto = produto;
    }

    private void setItemParaSalvar() throws IOException {
        qntNaEmb = Double.valueOf(edtQntNaEmb.getText().toString());
        qntEmb = Double.valueOf(edtQntEmb.getText().toString());
        qntTotal = Double.valueOf(edtQntTotal.getText().toString());
        setProdutoPorEan();

        setItem(
                new LancamentoColeta(
                    null,
                    produto.getId(),
                    qntNaEmb,
                    qntEmb,
                    qntTotal,
                    null
                )
        );
    }

    private void setItemParaAlterar() throws IOException {
        qntNaEmb = Double.valueOf(edtQntNaEmb.getText().toString());
        qntEmb = Double.valueOf(edtQntEmb.getText().toString());
        qntTotal = Double.valueOf(edtQntTotal.getText().toString());
        setProdutoPorEan();

        setItem(
                new LancamentoColeta(
                        item.getId(),
                        produto.getId(),
                        qntNaEmb,
                        qntEmb,
                        qntTotal,
                        item.getDataAlteracao()
                )
        );
    }

    private void setProdutoPorEan() throws IOException {
        String ean = edtEan.getText().toString();

        setProduto(produtoController.buscarPorEan(ean, unidade));
    }

    private void setColetaParaSalvar() throws IOException {
        setFornecedorPedidoUnidade();

        setColeta(
                new Coleta(
                        null,
                        fornecedor.getId(),
                        numeroNotaFiscal,
                        pedido.getId(),
                        new ArrayList<LancamentoColeta>(),
                        null,
                        null,
                        unidade
                )
        );
    }

    private void setColetaParaAlterar() throws IOException {
        setFornecedorPedidoUnidade();

        setColeta(
                new Coleta(
                        coleta.getId(),
                        fornecedor.getId(),
                        numeroNotaFiscal,
                        pedido.getId(),
                        coleta.getItens(),
                        coleta.getDataMovimento(),
                        coleta.getDataAlteracao(),
                        unidade
                )
        );
    }

    private void setFornecedorPedidoUnidade() throws IOException {
        String cnpjCfp = edtCnpjCfp.getText().toString();
        setNumeroNotaFiscal(
                Long.valueOf(edtNumeroNotaFiscal.getText().toString())
        );

        setFornecedor(fornecedorController.buscarPorCnpjCpf(cnpjCfp));
        setPedido(
            pedidoController.buscarPorFornecedorNotaFiscal(
                fornecedor.getId(),
                numeroNotaFiscal
            )
        );

        setUnidade(pedido.getUnidade());
    }

    private void setNumeroNotaFiscal(Long numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    private void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    private void deletarColeta() {
        try {
            coletaController.deletarColeta(coleta);
            setColeta(new Coleta());
            preencherCampos();
            Toast.makeText(this, "Deletado!!!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao deletar!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void atualizarColeta() {
        try {
            setColetaParaAlterar();
            coleta = coletaController.atualizarColeta(coleta);
            preencherCampos();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao alterar!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void salvarColeta() {
        try {
            setColetaParaSalvar();
            coleta = coletaController.salvarColeta(coleta);
            preencherCampos();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao salvar!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void salvarItemColeta() {
        try {
            setItemParaSalvar();
            item = coletaController.salvarItemColeta(coleta, item);
            btnBuscarColeta_Click();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao salvar!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void atualizarItemColeta() {
        try {
            setItemParaAlterar();
            item = coletaController.atualizarItemColeta(coleta, item);
            btnBuscarColeta_Click();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao alterar!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void deletarItemColeta() {
        try {
            coletaController.deletarItemColeta(coleta, item);
            btnBuscarColeta_Click();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao deletar!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscarColeta() {
        try {
            coleta = coletaController.buscarPorId(coleta.getId());
            preencherCampos();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao buscar!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscarColetaPorFornecedorNotaFiscal() {
        try {
            setFornecedorPedidoUnidade();
            coleta = coletaController.buscarPorFornecedorNotaFiscal(fornecedor.getId(), numeroNotaFiscal);
            preencherCampos();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao buscar!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void limparCamposItens() {
        edtEan.setText("");
        txvDescricao.setText("");
        edtQntEmb.setText("0");
        edtQntNaEmb.setText("0");
        edtQntTotal.setText("0");
    }

    private void abrirLeitura() {
        Intent intent = new Intent(this, LeitorActivity.class);

        startActivityForResult(intent, REQUEST_LEITURA);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LEITURA:
                if (resultCode == LeitorActivity.SUCESSO) {
                    //Dados vindo da intent chamada em modo de espera de resultado.
                    if (data != null) {
                        edtEan.setText(data.getStringExtra(LeitorActivity.LEITURA));
                        txvDescricao.setText("");
                        qntEmb = 0D;
                        qntNaEmb = 0D;
                        qntTotal = 0D;

                        new RcmThreadFactory().newThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    setProdutoPorEan();
                                } catch (IOException e) {
                                    Toast.makeText(
                                            null,
                                            "Erro na conexão!!!\n" + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                                preencherCamposItem();
                            }
                        }).start();
                    } else {
                        Toast.makeText(
                                this,
                                "Nenhum Código de barra capturado!",
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
        edtCnpjCfp = findViewById(R.id.edtCnpjCfp);
        edtNumeroNotaFiscal = findViewById(R.id.edtNumeroNotaFiscal);
        edtEan = findViewById(R.id.edtEan);
        edtQntNaEmb = findViewById(R.id.edtQntNaEmb);
        edtQntEmb = findViewById(R.id.edtQntEmb);
        edtQntTotal = findViewById(R.id.edtQntTotal);

        btnBuscarColeta = findViewById(R.id.btnBuscarColeta);
        btnAlterarColeta = findViewById(R.id.btnAlterarColeta);
        btnExcluirColeta = findViewById(R.id.btnExcluirColeta);
        btnAlterarItem = findViewById(R.id.btnAlterarItem);
        btnExcluirItem = findViewById(R.id.btnExcluirItem);
        btnLimpar = findViewById(R.id.btnLimpar);

        txvRazaoSocial = findViewById(R.id.txvRazaoSocial);
        txvDataMvto = findViewById(R.id.txvDataMvto);
        txvPedidoId = findViewById(R.id.txvPedidoId);
        txvDescricao = findViewById(R.id.txvDescricao);

        listLancamentoColeta = findViewById(R.id.listLancamentoColeta);

        btnBuscarColeta.setOnClickListener(btnBuscarColetaPorFornecedorNotaFiscal_Click());
        btnAlterarColeta.setOnClickListener(btnAlterarColeta_Click());
        btnExcluirColeta.setOnClickListener(btnExcluirColeta_Click());
        btnAlterarItem.setOnClickListener(btnAlterarItemColeta_Click());
        btnExcluirItem.setOnClickListener(btnExcluirItemColeta_Click());
        btnLimpar.setOnClickListener(btnLimpar_Click());

        edtEan.setOnClickListener(edtEan_Click());
    }

    public void configurar() {
        CaixaDialogo dialogo = new CaixaDialogo(
                "Path Server Host",
                configApp
        );

        dialogo.show(getSupportFragmentManager(), "DialogoHostApi");
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

    private View.OnClickListener edtEan_Click() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                abrirLeitura();
            }
        };
    }

    private View.OnClickListener btnLimpar_Click() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                limparCamposItens();
                preencherCamposItem();
            }
        };
    }

    private View.OnClickListener btnBuscarColetaPorFornecedorNotaFiscal_Click() {
        return new Botao(this){
            @Override
            protected void funcao() {
                buscarColetaPorFornecedorNotaFiscal();
            }
        }.construir();
    }

    private View.OnClickListener btnBuscarColeta_Click() {
        return new Botao(this){
            @Override
            protected void funcao() {
                buscarColeta();
            }
        }.construir();
    }

    private View.OnClickListener btnExcluirItemColeta_Click() {
        return new Botao(this){
            @Override
            protected void funcao() {
                deletarItemColeta();
            }
        }.construir();
    }

    private View.OnClickListener btnAlterarItemColeta_Click() {
        return new Botao(this){
            @Override
            protected void funcao() {
                atualizarItemColeta();
            }
        }.construir();
    }

    private View.OnClickListener btnSalvarItemColeta_Click() {
        return new Botao(this){
            @Override
            protected void funcao() {
                salvarItemColeta();
            }
        }.construir();
    }

    private View.OnClickListener btnSalvarColeta_Click() {
        return new Botao(this){
            @Override
            protected void funcao() {
                salvarColeta();
            }
        }.construir();
    }

    private View.OnClickListener btnAlterarColeta_Click() {
        return new Botao(this){
            @Override
            protected void funcao() {
                atualizarColeta();
            }
        }.construir();
    }

    private View.OnClickListener btnExcluirColeta_Click() {
        return new Botao(this){
            @Override
            protected void funcao() {
                deletarColeta();
            }
        }.construir();
    }
}
