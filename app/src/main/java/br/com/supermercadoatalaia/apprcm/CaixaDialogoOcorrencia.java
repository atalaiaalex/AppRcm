package br.com.supermercadoatalaia.apprcm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import br.com.supermercadoatalaia.apprcm.domain.model.OcorrenciaFornecedor;

public class CaixaDialogoOcorrencia extends DialogFragment {

    private TextView txvMensagem;
    private String title;
    private OcorrenciaFornecedor ocorrencia;

    public CaixaDialogoOcorrencia(String title, OcorrenciaFornecedor ocorrencia) {
        this.title = title;
        this.ocorrencia = ocorrencia;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.caixa_dialogo_ocorrencia, null);

        txvMensagem = view.findViewById(R.id.txvMensagem);

        txvMensagem.setText(
                ocorrencia.getMensagem() +
                " - Usuário que fez ocorrência: " +
                ocorrencia.getUsuarioId()
        );

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton(R.string.botao_estou_ciente, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Ocorrencia avisada!", Toast.LENGTH_LONG).show();
                    }
                });

        return builder.create();
    }
}
