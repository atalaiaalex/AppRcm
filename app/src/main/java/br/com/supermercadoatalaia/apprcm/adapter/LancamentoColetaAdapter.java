package br.com.supermercadoatalaia.apprcm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import br.com.supermercadoatalaia.apprcm.R;
import br.com.supermercadoatalaia.apprcm.domain.model.LancamentoColeta;

public class LancamentoColetaAdapter extends BaseAdapter {

    private List<LancamentoColeta> itens;
    private Context context;

    public LancamentoColetaAdapter(List<LancamentoColeta> itens, Context context) {
        this.itens = itens;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public LancamentoColeta getItem(int i) {
        return itens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.item_list_lancamento_coleta, null);
        } else {
            layout = view;
        }

        TextView txvListItemDescricao = layout.findViewById(R.id.txvListItemDescricao);
        TextView txvListItemQntNaEmb = layout.findViewById(R.id.txvListItemQntNaEmb);
        TextView txvListItemQntEmb = layout.findViewById(R.id.txvListItemQntEmb);
        TextView txvListItemQntTotal = layout.findViewById(R.id.txvListItemQntTotal);
        TextView txvListItemVencimento = layout.findViewById(R.id.txvListItemVencimento);

        if(!itens.isEmpty()) {
            LancamentoColeta item = getItem(i);

            txvListItemDescricao.setText(item.getProdutoDescricao());
            txvListItemQntNaEmb.setText(String.format("%1$,.2f", item.getQntNaEmb()));
            txvListItemQntEmb.setText(String.format("%1$,.2f", item.getQntEmb()));
            txvListItemQntTotal.setText(String.format("%1$,.2f", item.getQntTotal()));
            txvListItemVencimento.setText(String.format("%1$te/%1$tm/%1$ty", item.getVencimento()));
        }

        return layout;
    }
}
