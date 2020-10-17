package br.com.supermercadoatalaia.apprcm;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import br.com.supermercadoatalaia.apprcm.core.RcmThreadFactory;

public class Botao {
    private final RcmThreadFactory rcmThreadFactory;
    private final Context context;

    public Botao(Context context) {
        rcmThreadFactory = new RcmThreadFactory();
        this.context = context;
    }

    public View.OnClickListener construir() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    rcmThreadFactory.newThread(new Runnable() {
                        @Override
                        public void run() {
                            funcao();
                        }
                    }, "Usando API");

                    rcmThreadFactory.iniciar();
                } catch(IllegalThreadStateException e) {
                    Toast.makeText(
                            context,
                            "Erro na conexão!!!\n" + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        };
    }

    protected void funcao() {
        Toast.makeText(
                context,
                "Sem implementação",
                Toast.LENGTH_LONG
        ).show();
    }
}
