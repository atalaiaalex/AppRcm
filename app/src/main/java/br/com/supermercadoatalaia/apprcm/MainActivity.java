package br.com.supermercadoatalaia.apprcm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.supermercadoatalaia.apprcm.adapter.LancamentoColetaAdapter;
import br.com.supermercadoatalaia.apprcm.controller.ColetaController;
import br.com.supermercadoatalaia.apprcm.controller.FornecedorController;
import br.com.supermercadoatalaia.apprcm.controller.LoginController;
import br.com.supermercadoatalaia.apprcm.controller.PedidoCompraController;
import br.com.supermercadoatalaia.apprcm.controller.ProdutoController;
import br.com.supermercadoatalaia.apprcm.core.SharedPrefManager;
import br.com.supermercadoatalaia.apprcm.domain.model.Coleta;
import br.com.supermercadoatalaia.apprcm.domain.model.Fornecedor;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;
import br.com.supermercadoatalaia.apprcm.domain.model.OcorrenciaFornecedor;
import br.com.supermercadoatalaia.apprcm.domain.model.PedidoCompra;
import br.com.supermercadoatalaia.apprcm.domain.model.ProdUnidade;
import br.com.supermercadoatalaia.apprcm.dto.help.MessageResponse;
import br.com.supermercadoatalaia.apprcm.exception.RegistroNotFoundException;
import br.com.supermercadoatalaia.apprcm.exception.RequestFailureException;
import br.com.supermercadoatalaia.apprcm.util.MaskDateTextWatcher;

public class MainActivity extends AppCompatActivity {

    private ColetaController coletaController;
    private FornecedorController fornecedorController;
    private PedidoCompraController pedidoCompraController;
    private ProdutoController produtoController;

    private Coleta coleta;
    private LancamentoColeta item;
    private List<PedidoCompra> pedidoCompras;
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
    private EditText edtValidade;
    private EditText edtChave;

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

    private ImageButton btiScannerEan;
    private ImageButton btiScannerChave;

    private DatePicker dpkValidade;

    private ListView listLancamentoColeta;

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (result.getResultCode() == LeitorActivity.SUCESSO) {
                    iniciarNovoItem();

                    if (data != null) {
                        if(btiScannerChave.isEnabled()) {
                            edtChave.setText(data.getStringExtra(LeitorActivity.RETORNO_LEITURA));
                        }else if(btiScannerEan.isEnabled()) {
                            edtEan.setText(data.getStringExtra(LeitorActivity.RETORNO_LEITURA));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Nenhum Código de barra capturado!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coletaController = new ColetaController(getApplicationContext());
        fornecedorController = new FornecedorController(getApplicationContext());
        pedidoCompraController = new PedidoCompraController(getApplicationContext());
        produtoController = new ProdutoController(getApplicationContext());

        initComponents();
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

    private void setPedidos(List<PedidoCompra> pedidoCompras) {
        this.pedidoCompras = pedidoCompras;
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

        edtChave.setText("");
        edtCnpjCfp.setText("");
        edtNumeroNotaFiscal.setText("");
        txvDataMvto.setText("");
        txvPedidoId.setText("");
        txvRazaoSocial.setText("");
        txvUnidade.setText("");

        edtCnpjCfp.requestFocus();
    }

    private void mudarBotoesNovaColeta() {
        btiScannerChave.setEnabled(true);
        btnIniciarColeta.setEnabled(true);
        btnBuscarColeta.setEnabled(true);

        btnAlterarColeta.setEnabled(false);
        btnAlterarColeta.setText(R.string.botao_alterar);
        btnExcluirColeta.setEnabled(false);
        btnExcluirColeta.setText(R.string.botao_excluir);

        habilitarCamposColeta();
        desabilitarBotoesItem();
        desabilitarCamposItem();
    }

    private void mudarBotoesIniciarColeta() {
        btnAlterarColeta.setEnabled(true);
        btnAlterarColeta.setText(R.string.botao_alterar);
        btnExcluirColeta.setEnabled(true);
        btnExcluirColeta.setText(R.string.botao_excluir);

        btiScannerChave.setEnabled(false);
        btnIniciarColeta.setEnabled(false);
        btnBuscarColeta.setEnabled(false);

        mudarBotoesIniciarItem();
    }

    private void mudarBotoesAlterarColeta() {
        btnAlterarColeta.setEnabled(true);
        btnAlterarColeta.setText(R.string.botao_salvar);
        btnExcluirColeta.setEnabled(true);
        btnExcluirColeta.setText(R.string.botao_cancelar);

        btiScannerChave.setEnabled(false);
        btnIniciarColeta.setEnabled(false);
        btnBuscarColeta.setEnabled(false);

        desabilitarBotoesItem();
        desabilitarCamposItem();
    }

    private void mudarBotoesIniciarItem() {
        btnLancar.setEnabled(true);
        btnLimpar.setEnabled(true);
        btnLimpar.setText(R.string.botao_limpar);

        btnAlterarItem.setEnabled(false);
        btnAlterarItem.setText(R.string.botao_alterar);
        btnExcluirItem.setEnabled(false);

        btiScannerEan.setEnabled(true);

        habilitarCamposItem();
    }

    private void mudarBotoesAlterarItem() {
        btnAlterarItem.setEnabled(true);
        btnAlterarItem.setText(R.string.botao_salvar);
        btnLimpar.setEnabled(true);
        btnLimpar.setText(R.string.botao_cancelar);

        btiScannerEan.setEnabled(true);

        btnLancar.setEnabled(false);
        btnExcluirItem.setEnabled(false);

        habilitarCamposItem();
    }

    private void mudarBotoesClickList() {
        habilitarBotoesItem();
        desabilitarCamposItem();

        btnLancar.setEnabled(false);
        btiScannerEan.setEnabled(false);
    }

    private void habilitarCamposColeta() {
        edtChave.setEnabled(true);
        edtCnpjCfp.setEnabled(true);
        edtNumeroNotaFiscal.setEnabled(true);
    }

    private void desabilitarBotoesItem() {
        btnLancar.setEnabled(false);
        btnLimpar.setEnabled(false);
        btnAlterarItem.setEnabled(false);
        btnExcluirItem.setEnabled(false);
        btiScannerEan.setEnabled(false);
    }

    private void habilitarBotoesItem() {
        btnLancar.setEnabled(true);
        btnLimpar.setEnabled(true);
        btnAlterarItem.setEnabled(true);
        btnExcluirItem.setEnabled(true);
        btiScannerEan.setEnabled(true);
    }

    private void desabilitarCamposItem() {
        edtEan.setEnabled(false);
        edtQntEmb.setEnabled(false);
        edtQntNaEmb.setEnabled(false);
        edtQntTotal.setEnabled(false);
        edtValidade.setEnabled(false);
        dpkValidade.setEnabled(false);
    }

    private void habilitarCamposItem() {
        edtEan.setEnabled(true);
        edtQntEmb.setEnabled(true);
        edtQntNaEmb.setEnabled(true);
        edtQntTotal.setEnabled(true);
        edtValidade.setEnabled(true);
        dpkValidade.setEnabled(true);
    }

    private void ajustaListLancamentoColeta() {
        listLancamentoColeta.setAdapter(
                new LancamentoColetaAdapter(coleta.getItens(), this)
        );

        int numLinha = listLancamentoColeta.getAdapter().getCount();
        listLancamentoColeta.getLayoutParams().height = 100;

        if(numLinha > 0) {
            listLancamentoColeta.setNestedScrollingEnabled(true);
            if(numLinha > 2) {
                listLancamentoColeta.getLayoutParams().height = 300;
            }else if(numLinha > 1) {
                listLancamentoColeta.getLayoutParams().height = 200;
            }
        }else {
            listLancamentoColeta.setNestedScrollingEnabled(false);
        }
    }

    private void iniciarNovoItem() {
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
        edtValidade.setText(String.format("%1$td/%1$tm/%1$tY", vencimento));
        edtValidade.setSelection(edtValidade.length());

        dpkValidade.updateDate(
                vencimento.get(Calendar.YEAR),
                vencimento.get(Calendar.MONTH),
                vencimento.get(Calendar.DAY_OF_MONTH)
        );

        ajustaListLancamentoColeta();
    }

    private void preencherCampos() throws RegistroNotFoundException {
        setFornecedor(new Fornecedor());
        try {
            setFornecedor(fornecedorController.buscarPorId(coleta.getFornecedorId()));
        } catch (RegistroNotFoundException e) {
            e.printStackTrace();
        }
        setPedidos(new ArrayList<>());
        setPedidos(pedidoCompraController.buscarPorFornecedorNotaFiscal(fornecedor.getId() ,coleta.getNumeroNotaFiscal()));

        edtCnpjCfp.setText(fornecedor.getCnpjCpf());
        edtNumeroNotaFiscal.setText(
                String.valueOf(pedidoCompras.get(0).getNotaFiscalBaixada())
        );
        txvRazaoSocial.setText(fornecedor.getRazaoSocial());
        txvDataMvto.setText(String.format(
                "%1$td/%1$tm/%1$ty %1$tH:%1$tM",
                coleta.getDataMovimento()
                )
        );
        txvPedidoId.setText(
                setPedidoIds(pedidoCompras).toString()
        );
        txvUnidade.setText("Unid." + pedidoCompras.get(0).getUnidade());

        edtChave.setEnabled(false);
        edtCnpjCfp.setEnabled(false);
        edtNumeroNotaFiscal.setEnabled(false);

        ajustaListLancamentoColeta();
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
                    String.format("%1$.2f", qntTotal)
            );
        } else {
            edtQntTotal.setText("");
        }
        if(qntNaEmb > 0) {
            edtQntNaEmb.setText(
                    String.format("%1$.2f", qntNaEmb)
            );
        } else {
            edtQntNaEmb.setText("");
        }
        if(qntEmb > 0) {
            edtQntEmb.setText(
                    String.format("%1$.2f", qntEmb)
            );
        } else {
            edtQntEmb.setText("");
        }

        edtValidade.setText(String.format("%1$td/%1$tm/%1$tY", vencimento));
        edtValidade.setSelection(edtValidade.length());

        dpkValidade.updateDate(
                vencimento.get(Calendar.YEAR),
                vencimento.get(Calendar.MONTH),
                vencimento.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void setItemParaSalvar() throws RegistroNotFoundException, IOException {
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

    private void setItemParaAlterar() throws RegistroNotFoundException, IOException {
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

    private void setQntsValidadeItem() throws RegistroNotFoundException, IOException {
        if(edtQntNaEmb.getText().toString().length() == 0) {
            qntNaEmb = 0D;
        } else {
            qntNaEmb = Double.valueOf(edtQntNaEmb.getText().toString().replace(",", "."));
        }
        if(edtQntEmb.getText().toString().length() == 0) {
            qntEmb = 0D;
        } else {
            qntEmb = Double.valueOf(edtQntEmb.getText().toString().replace(",", "."));
        }
        if(edtQntTotal.getText().toString().length() == 0) {
            qntTotal = 0D;
        } else {
            qntTotal = Double.valueOf(edtQntTotal.getText().toString().replace(",", "."));
        }
        try {
            vencimento.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(edtValidade.getText().toString()));
            dpkValidade.updateDate(
                    vencimento.get(Calendar.YEAR),
                    vencimento.get(Calendar.MONTH),
                    vencimento.get(Calendar.DAY_OF_MONTH)
            );
        } catch (ParseException e) {
            throw new IOException("Data inválida", e);
        }

        vencimento.set(dpkValidade.getYear(), dpkValidade.getMonth(), dpkValidade.getDayOfMonth());

        buscarProdutoPorEan();
    }

    private void buscarProdutoPorEan() throws RegistroNotFoundException {
        String ean = edtEan.getText().toString();
        edtEan.setText(ean13(ean));

        if(!produto.getEan().equals(edtEan.getText().toString())) {
            setProduto(new ProdUnidade());
            setProduto(produtoController.buscarPorEanUnidade(ean, unidade));
        }
    }

    private void logout() {
        new LoginController(getApplicationContext()).logout();
    }

    private String ean13(String ean) {

        while(ean.length() < 13) {
            ean = "0" + ean;
        }

        return ean;
    }

    private Set<Long> setPedidoIds(List<PedidoCompra> pedidoCompras) {
        Set<Long> ids = new HashSet<>();

        for(PedidoCompra p : pedidoCompras) {
            ids.add(p.getId());
        }

        return ids;
    }

    private void setColetaParaSalvar() throws RegistroNotFoundException {
        setFornecedorPedidoUnidade();

        setColeta(
                new Coleta(
                        null,
                        fornecedor.getId(),
                        numeroNotaFiscal,
                        pedidoCompras.get(0).getSerie(),
                        setPedidoIds(pedidoCompras),
                        new ArrayList<LancamentoColeta>(),
                        null,
                        null,
                        unidade
                )
        );
    }

    private void setColetaParaAlterar() throws RegistroNotFoundException {
        setFornecedorPedidoUnidade();

        setColeta(
                new Coleta(
                        coleta.getId(),
                        fornecedor.getId(),
                        numeroNotaFiscal,
                        pedidoCompras.get(0).getSerie(),
                        setPedidoIds(pedidoCompras),
                        coleta.getItens(),
                        coleta.getDataMovimento(),
                        coleta.getDataAlteracao(),
                        unidade
                )
        );
    }

    private void setFornecedorPedidoUnidade() throws RegistroNotFoundException {
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

        try {
            if(cnpjCfp.length() >= 14) {
                setFornecedor(fornecedorController.buscarPorCnpjCpf(cnpjCfp));
            } else {
                setFornecedor(fornecedorController.buscarPorId(Long.valueOf(cnpjCfp)));
            }
        } catch (RegistroNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        setPedidos(new ArrayList<>());
        setPedidos(
            pedidoCompraController.buscarPorFornecedorNotaFiscal(
                fornecedor.getId(),
                numeroNotaFiscal
            )
        );

        setUnidade(pedidoCompras.get(0).getUnidade());

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
        } catch(RegistroNotFoundException e) {
            e.printStackTrace();// coloquei para ver que erro dá aqui nas ocorrencias
        }
    }

    private void deletarColeta() {
        String repDeletar = getResources().getString(R.string.botao_excluir);

        if(btnExcluirColeta.getText().toString().equals(repDeletar)) {
            try {
                coletaController.deletarColeta(coleta);
                mudarBotoesNovaColeta();
                Toast.makeText(this, "Deletado!!!", Toast.LENGTH_LONG).show();
            } catch (RequestFailureException apie) {
                Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
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
                MessageResponse mr = coletaController.atualizarColeta(coleta);
                coleta.setId(mr.getId());
                mudarBotoesIniciarColeta();
                preencherCampos();
            } catch (RegistroNotFoundException apie) {
                Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
            } catch (RequestFailureException e) {
                Toast.makeText(this, "Erro ao alterar coleta!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void salvarColeta() {
        try {
            setColetaParaSalvar();
            MessageResponse mr = coletaController.salvarColeta(coleta);
            coleta.setId(mr.getId());
            preencherCampos();
            mudarBotoesIniciarColeta();
            iniciarNovoItem();
            //edtEan.requestFocus(); //atrapalha em alguns momentos
        } catch (RegistroNotFoundException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (RequestFailureException e) {
            Toast.makeText(this, "Erro ao salvar coleta!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void salvarItemColeta() {
        try {
            setItemParaSalvar();
            MessageResponse mr = coletaController.salvarItemColeta(coleta, item);
            item.setId(mr.getId());
            coleta.getItens().add(item);
            if(coleta.getItens().size() == 1) {
                Log.i("Main salvar coleta", "tem 1 item na lista");
                coletaController.salvarColetaFlex(coleta, item);
            }
            mudarBotoesIniciarItem();
            iniciarNovoItem();
        } catch (RegistroNotFoundException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (RequestFailureException | IOException e) {
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
                MessageResponse mr = coletaController.atualizarItemColeta(coleta, item);
                item.setId(mr.getId());
                coleta.getItens().set(indexItem, item);
                mudarBotoesIniciarItem();
                iniciarNovoItem();
            } catch (RegistroNotFoundException apie) {
                Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
            } catch (IOException | RequestFailureException e) {
                Toast.makeText(this, "Erro ao alterar item!!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void deletarItemColeta() {
        try {
            int indexItem = coleta.getItens().indexOf(item);
            coletaController.deletarItemColeta(coleta, item);
            coleta.getItens().remove(indexItem);
            if(coleta.getItens().size() > 0) {
                mudarBotoesIniciarItem();
                iniciarNovoItem();
            }else {
                coletaController.deletarColeta(coleta);
                mudarBotoesIniciarColeta();
                iniciarNovaColeta();
                Toast.makeText(this, "Coleta deletada por falta de itens!", Toast.LENGTH_LONG).show();
            }
        } catch (RequestFailureException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscarColetaPorFornecedorNotaFiscal() {
        try {
            setFornecedorPedidoUnidade();
            coleta = coletaController.buscarPorFornecedorNotaFiscal(fornecedor.getId(), numeroNotaFiscal);
            preencherCampos();
            mudarBotoesIniciarColeta();
        } catch (RegistroNotFoundException apie) {
            Toast.makeText(this, apie.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Verifique o cnpj e núm. da NF digitado!\n"+nfe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void abrirLeitura() {
        Intent intent = new Intent(getApplicationContext(), LeitorActivity.class);
        activityResultLauncher.launch(intent);
    }

    private void initComponents() {
        setContentView(R.layout.activity_main);

        edtCnpjCfp = findViewById(R.id.edtCnpjCfp);
        edtNumeroNotaFiscal = findViewById(R.id.edtNumeroNotaFiscal);
        edtEan = findViewById(R.id.edtEan);
        edtQntNaEmb = findViewById(R.id.edtQntNaEmb);
        edtQntEmb = findViewById(R.id.edtQntEmb);
        edtQntTotal = findViewById(R.id.edtQntTotal);
        edtValidade = findViewById(R.id.edtValidade);
        edtChave = findViewById(R.id.edtChave);

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

        btiScannerEan = findViewById(R.id.btiScannerEan);
        btiScannerChave = findViewById(R.id.btiScannerChave);

        dpkValidade = findViewById(R.id.dpkValidade);

        listLancamentoColeta = findViewById(R.id.listLancamentoColeta);

        btnBuscarColeta.setOnClickListener(v -> buscarColetaPorFornecedorNotaFiscal());
        btnAlterarColeta.setOnClickListener(v -> atualizarColeta());
        btnExcluirColeta.setOnClickListener(v -> deletarColeta());
        btnAlterarItem.setOnClickListener(v -> atualizarItemColeta());
        btnExcluirItem.setOnClickListener(v -> deletarItemColeta());
        btnLimpar.setOnClickListener(v -> {
            mudarBotoesIniciarItem();
            iniciarNovoItem();
        });
        btnLancar.setOnClickListener(v -> salvarItemColeta());
        btnIniciarColeta.setOnClickListener(v -> salvarColeta());
        btnNovaColeta.setOnClickListener(v -> iniciarNovaColeta());
        btnLogout.setOnClickListener(v -> logout());

        btiScannerEan.setOnClickListener(v -> abrirLeitura());
        btiScannerChave.setOnClickListener(v -> abrirLeitura());

        edtEan.setOnFocusChangeListener(edtEan_FocusChange());
        edtValidade.addTextChangedListener(edtValidade_TextChanged());
        edtChave.addTextChangedListener(edtChave_TextChanged());

        listLancamentoColeta.setOnItemClickListener(listLancamentoColeta_ItemClick());
        listLancamentoColeta.setNestedScrollingEnabled(false);

        Calendar dataAtual = Calendar.getInstance();
        dpkValidade.init(
                dataAtual.get(Calendar.YEAR),
                dataAtual.get(Calendar.MONTH),
                dataAtual.get(Calendar.DAY_OF_MONTH),
                dpkValidade_DateChanged()
        );

        txvUsuario.setText(
                "Usuário: " +
                SharedPrefManager.getInstance(getApplicationContext()).getUsuario().getNome()
        );
    }

    private TextWatcher edtChave_TextChanged() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtChave.length() == 44) {
                    edtCnpjCfp.setText(edtChave.getText().subSequence(6, 20));
                    //edtSerie.setText(edtChave.getText().subSequence(22, 25));
                    edtNumeroNotaFiscal.setText(edtChave.getText().subSequence(25, 34));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        };
    }

    private TextWatcher edtValidade_TextChanged() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String atual = edtValidade.getText().toString();
                String novo = MaskDateTextWatcher.aplicaMascara(s.toString());

                if(!atual.equals(novo)) {
                    edtValidade.setText(novo);
                    edtValidade.setSelection(edtValidade.length());
                }

                if(edtValidade.length() == 10) {
                    try {
                        vencimento.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(edtValidade.getText().toString()));
                        dpkValidade.updateDate(
                                vencimento.get(Calendar.YEAR),
                                vencimento.get(Calendar.MONTH),
                                vencimento.get(Calendar.DAY_OF_MONTH)
                        );
                    } catch (ParseException e) {
                        //e.printStackTrace();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }

    private DatePicker.OnDateChangedListener dpkValidade_DateChanged() {
        return (view, year, monthOfYear, dayOfMonth) -> {
            vencimento.set(dpkValidade.getYear(), dpkValidade.getMonth(), dpkValidade.getDayOfMonth());
            edtValidade.setText(String.format("%1$td/%1$tm/%1$tY", vencimento));
            edtValidade.setSelection(edtValidade.length());
        };
    }

    private AdapterView.OnItemClickListener listLancamentoColeta_ItemClick() {
        return (adapterView, view, i, l) -> {
            if(listLancamentoColeta.getAdapter().getCount() > 0) {
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
        return (view, hasFocus) -> {
            if(isFocusBuscaEan()) {
                try {
                    buscarProdutoPorEan();
                    preencherCamposItem();
                } catch (RegistroNotFoundException apie) {
                    Toast.makeText(getApplicationContext(), apie.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}
