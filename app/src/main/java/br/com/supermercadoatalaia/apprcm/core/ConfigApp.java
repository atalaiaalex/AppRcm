package br.com.supermercadoatalaia.apprcm.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class ConfigApp {

    private String pasta;

    public static final String PASTA_CONFIG = "config";
    private static final String NOME_CONFIG = "app.conf";

    public ConfigApp(String pasta){
        this.pasta = pasta;
    }

    public String lerTxt() throws IOException {
        BufferedReader leitor =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(pasta+"/"+NOME_CONFIG), Charset.defaultCharset()
                        )
                );

        if(leitor.ready()){
            String linha = leitor.readLine();

            if(linha.equals("null")) {
                throw new IOException("Arquivo com configuração inválida!");
            }

            return linha;
        }

        throw new IOException("Arquivo sem configuração ou não encontrado!");
    }

    public void salvarTxt (String dado) throws IOException {
        /*
        try {
            new File(pasta + "/" + NOME_CONFIG).delete();
        } catch(Exception e) { }
        */

        FileWriter arquivo = new FileWriter(pasta + "/" + NOME_CONFIG);
        PrintWriter outputStream;

        outputStream = new PrintWriter(arquivo);
        outputStream.println(dado);
        arquivo.flush();
        arquivo.close();
    }

}
