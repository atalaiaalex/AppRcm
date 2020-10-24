package br.com.supermercadoatalaia.apprcm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
import br.com.supermercadoatalaia.apprcm.core.ConfigApp;
import br.com.supermercadoatalaia.apprcm.core.exception.ApiException;
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

    private Button btnBuscarColeta;
    private Button btnAlterarColeta;
    private Button btnExcluirColeta;
    private Button btnAlterarItem;
    private Button btnExcluirItem;
    private Button btnLimpar;
    private Button btnLancar;
    private Button btnNovaColeta;
    private Button btnIniciarColeta;

    private DatePicker dpkValidade;

    private ListView listLancamentoColeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initComponents();
        initPermissoes();
        initConfigApp();
    }

    private void initConfigApp() {
        configApp = new ConfigApp(
                getExternalFilesDir(ConfigApp.PASTA_CONFIG).getAbsolutePath()
        );

        try {
            coletaController = new ColetaController(configApp);
            fornecedorController = new FornecedorController(configApp);
            pedidoController = new PedidoController(configApp);
            produtoController = new ProdutoController(configApp);
        } catch (IOException e) {
            configurar();
        }
    }

    private void habilitarCamposColeta() {
        edtCnpjCfp.setEnabled(true);
        edtNumeroNotaFiscal.setEnabled(true);
    }

    private void iniciarNovaColeta() {
        habilitarCamposColeta();

        fornecedor = new Fornecedor();
        pedidos = new ArrayList<>();
        coleta = new Coleta();
        numeroNotaFiscal = 0L;
        unidade = "";

        edtCnpjCfp.setText("");
        edtNumeroNotaFiscal.setText("");
        txvDataMvto.setText("");
        txvPedidoId.setText("");
        txvRazaoSocial.setText("");

        edtCnpjCfp.requestFocus();

        btnAlterarColeta.setText(R.string.botao_alterar);
        btnAlterarItem.setText(R.string.botao_alterar);
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

        produto = new ProdUnidade();
        item = new LancamentoColeta();
        vencimento = Calendar.getInstance();

        qntEmb = 0D;
        qntNaEmb = 0D;
        qntTotal = 0D;

        edtEan.setText("");
        txvDescricao.setText("");
        edtQntEmb.setText("0");
        edtQntNaEmb.setText("0");
        edtQntTotal.setText("0");

        dpkValidade.updateDate(
                vencimento.get(Calendar.YEAR),
                vencimento.get(Calendar.MONTH),
                vencimento.get(Calendar.DAY_OF_MONTH)
        );

        edtEan.requestFocus();
    }

    private void preencherCampos() throws ApiException, IOException {
        setFornecedor(fornecedorController.buscarPorId(coleta.getFornecedorId()));
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

        edtCnpjCfp.setEnabled(false);
        edtNumeroNotaFiscal.setEnabled(false);

        listLancamentoColeta.setAdapter(
                new LancamentoColetaAdapter(coleta.getItens(), this)
        );
    }

    private void preencherCamposItensDoList() {
        try {
            setProduto(produtoController.buscarPorId(item.getProdutoId(), pedidos.get(0).getUnidade()));
        } catch (ApiException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao buscar produto!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        qntTotal = item.getQntTotal();
        qntNaEmb = item.getQntNaEmb();
        qntEmb = item.getQntEmb();
        vencimento = item.getVencimento();

        preencherCamposItem();
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

        dpkValidade.updateDate(
                vencimento.get(Calendar.YEAR),
                vencimento.get(Calendar.MONTH),
                vencimento.get(Calendar.DAY_OF_MONTH)
        );

        edtEan.setEnabled(false);
        edtQntEmb.setEnabled(false);
        edtQntNaEmb.setEnabled(false);
        edtQntTotal.setEnabled(false);
        dpkValidade.setEnabled(false);
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
                        item.getDataAlteracao()
                )
        );
    }

    private void setQntsValidadeItem() throws ApiException, IOException {
        qntNaEmb = Double.valueOf(edtQntNaEmb.getText().toString());
        qntEmb = Double.valueOf(edtQntEmb.getText().toString());
        qntTotal = Double.valueOf(edtQntTotal.getText().toString());
        vencimento.set(dpkValidade.getYear(), dpkValidade.getMonth(), dpkValidade.getDayOfMonth());

        setProdutoPorEan();
    }

    private void setProdutoPorEan() throws ApiException, IOException {
        String ean = edtEan.getText().toString();

        setProduto(produtoController.buscarPorEan(ean, unidade));
    }

    private Set<Long> setPedidoIds(List<Pedido> pedidos) {
        Set<Long> ids = new HashSet<>();

        for(Pedido p : pedidos) {
            ids.add(p.getId());
        }

        return ids;
    }

    private void setColetaParaSalvar() throws ApiException, IOException {
        setFornecedorPedidoUnidade();

        setColeta(
                new Coleta(
                        null,
                        fornecedor.getId(),
                        numeroNotaFiscal,
                        setPedidoIds(pedidos),
                        new ArrayList<LancamentoColeta>(),
                        null,
                        null,
                        unidade
                )
        );
    }

    private void setColetaParaAlterar() throws ApiException, IOException {
        setFornecedorPedidoUnidade();

        setColeta(
                new Coleta(
                        coleta.getId(),
                        fornecedor.getId(),
                        numeroNotaFiscal,
                        setPedidoIds(pedidos),
                        coleta.getItens(),
                        coleta.getDataMovimento(),
                        coleta.getDataAlteracao(),
                        unidade
                )
        );
    }

    private void setFornecedorPedidoUnidade() throws ApiException, IOException {
        String cnpjCfp = edtCnpjCfp.getText().toString();
        setNumeroNotaFiscal(
                Long.valueOf(edtNumeroNotaFiscal.getText().toString())
        );

        setFornecedor(fornecedorController.buscarPorCnpjCpf(cnpjCfp));
        setPedidos(
            pedidoController.buscarPorFornecedorNotaFiscal(
                fornecedor.getId(),
                numeroNotaFiscal
            )
        );

        setUnidade(pedidos.get(0).getUnidade());
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
            iniciarNovaColeta();
            Toast.makeText(this, "Deletado!!!", Toast.LENGTH_LONG).show();
        } catch (ApiException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao deletar coleta!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void atualizarColeta() {
        String repAlterar = getResources().getString(R.string.botao_alterar);
        if(btnAlterarColeta.getText().toString().equals(repAlterar)) {
            habilitarCamposItem();
            btnAlterarColeta.setText(R.string.botao_salvar);
        } else {
            try {
                setColetaParaAlterar();
                coleta = coletaController.atualizarColeta(coleta);
                btnAlterarColeta.setText(R.string.botao_alterar);
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
            habilitarCamposItem();
            btnAlterarItem.setText(R.string.botao_salvar);
        } else {
            try {
                setItemParaAlterar();
                int indexItem = coleta.getItens().indexOf(item);
                item = coletaController.atualizarItemColeta(coleta, item);
                btnAlterarItem.setText(R.string.botao_alterar);
                coleta.getItens().set(indexItem, item);
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
            coletaController.deletarItemColeta(coleta, item);
            iniciarNovoItem();
        } catch (ApiException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao deletar item!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscarColeta() {
        try {
            coleta = coletaController.buscarPorId(coleta.getId());
            preencherCampos();
        } catch (ApiException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException | ParseException e) {
            Toast.makeText(this, "Erro ao buscar coleta!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscarColetaPorFornecedorNotaFiscal() {
        try {
            setFornecedorPedidoUnidade();
            coleta = coletaController.buscarPorFornecedorNotaFiscal(fornecedor.getId(), numeroNotaFiscal);
            preencherCampos();
        } catch (ApiException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException | ParseException e) {
            Toast.makeText(this, "Erro ao buscar coleta!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
                        iniciarNovoItem();
                        edtEan.setText(data.getStringExtra(LeitorActivity.LEITURA));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    setProdutoPorEan();
                                } catch (ApiException apie) {
                                    Toast.makeText(getApplicationContext(), apie.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Erro ao buscar produto!!!\n"+e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                                preencherCamposItem();
                            }
                        });
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

        btnBuscarColeta = findViewById(R.id.btnBuscarColeta);
        btnAlterarColeta = findViewById(R.id.btnAlterarColeta);
        btnExcluirColeta = findViewById(R.id.btnExcluirColeta);
        btnAlterarItem = findViewById(R.id.btnAlterarItem);
        btnExcluirItem = findViewById(R.id.btnExcluirItem);
        btnLimpar = findViewById(R.id.btnLimpar);
        btnLancar = findViewById(R.id.btnLancar);
        btnNovaColeta = findViewById(R.id.btnNovaColeta);
        btnIniciarColeta = findViewById(R.id.btnIniciarColeta);

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

        edtEan.setOnFocusChangeListener(edtEan_FocusChange());

        listLancamentoColeta.setOnItemClickListener(listLancamentoColeta_ItemClick());
    }

    private void configurar() {
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

    private AdapterView.OnItemClickListener listLancamentoColeta_ItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setItem((LancamentoColeta) adapterView.getItemAtPosition(i));
                preencherCamposItensDoList();
            }
        };
    }

    private View.OnFocusChangeListener edtEan_FocusChange() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    abrirLeitura();
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

    private View.OnClickListener btnLimpar_Click() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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

    private View.OnClickListener btnBuscarColeta_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buscarColeta();
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
                        iniciarNovoItem();
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
