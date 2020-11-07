package br.com.supermercadoatalaia.apprcm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

import br.com.supermercadoatalaia.apprcm.core.ConfigApp;

public class CaixaDialogoHost extends DialogFragment {

    private EditText edtDialogoHost;
    private String title;
    private ConfigApp configApp;

    public CaixaDialogoHost(String title, ConfigApp configApp) {
        this.title = title;
        this.configApp = configApp;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.caixa_dialogo_host, null);

        edtDialogoHost = view.findViewById(R.id.edtDialogoHost);

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton(R.string.botao_salvar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            configApp.salvarTxt(edtDialogoHost.getText().toString());
                            Toast.makeText(getActivity(), "O app foi finalizado!", Toast.LENGTH_LONG).show();
                            getActivity().finishAffinity();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.botao_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            configApp.salvarTxt("null");
                            Toast.makeText(getActivity(), "O app foi finalizado!", Toast.LENGTH_LONG).show();
                            getActivity().finishAffinity();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        CaixaDialogoHost.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
