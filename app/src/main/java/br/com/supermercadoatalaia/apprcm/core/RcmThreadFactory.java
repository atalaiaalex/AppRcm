package br.com.supermercadoatalaia.apprcm.core;

import java.io.IOException;
import java.util.concurrent.ThreadFactory;

public class RcmThreadFactory implements ThreadFactory {

    private Thread thread;

    @Override
    public Thread newThread(Runnable runnable) throws IllegalThreadStateException {
        if(thread != null && (thread.isAlive() || thread.isDaemon())) {

            if(thread.getName().equals("")){
                thread.setName("Sem nome");
            }

            throw new IllegalThreadStateException("A tarefa " + thread.getName() + " está em execução.");
        }

        thread = new Thread(runnable);

        return thread;
    }

    public void newThread(Runnable runnable, String nomeTarefa)
            throws IllegalThreadStateException {
        thread = newThread(runnable);
        thread.setName(nomeTarefa);
    }

    public boolean isOciosa() {
        return thread.isDaemon();
    }

    public void iniciar() {
        thread.start();
    }

    public void parar() {
        thread.interrupt();
    }
}
