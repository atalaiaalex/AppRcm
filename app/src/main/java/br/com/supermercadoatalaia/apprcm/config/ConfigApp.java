package br.com.supermercadoatalaia.apprcm.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class ConfigApp {

    public static String SERVER;
    public static String SERVER_HOST = "http://192.168.2.199:1234";

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

            SERVER = linha;

            return linha;
        }

        throw new IOException("Arquivo sem configuração ou não encontrado!");
    }

    public void salvarTxt (String dado) throws IOException {
        new File(pasta + "/" + NOME_CONFIG).deleteOnExit();
        FileWriter arquivo = new FileWriter(pasta + "/" + NOME_CONFIG);
        PrintWriter outputStream;

        SERVER = dado;

        outputStream = new PrintWriter(arquivo);
        outputStream.println(dado);
        arquivo.flush();
        arquivo.close();
    }
}
