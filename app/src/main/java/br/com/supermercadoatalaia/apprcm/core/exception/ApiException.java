package br.com.supermercadoatalaia.apprcm.core.exception;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

import br.com.supermercadoatalaia.apprcm.core.HttpResposta;

public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String FORMATO_DATA_HORA = "dd/MM/yy HH:mm";

    public ApiException(HttpResposta httpResposta, JsonReader jsonReader) {
        super(instanciarErro(httpResposta, jsonReader));
    }

    private static String instanciarErro(HttpResposta httpResposta, JsonReader jsonReader) {
        String mensagem = "";
        Calendar timeStamp = Calendar.getInstance();

        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if(key.equals("msg") && jsonReader.peek() != JsonToken.NULL) {
                    mensagem = jsonReader.nextString();
                } else if(key.equals("timeStamp") && jsonReader.peek() != JsonToken.NULL) {
                    timeStamp.setTimeInMillis(jsonReader.nextLong());
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            return e.getMessage();
        }

        DateFormat dateFormat = new SimpleDateFormat(FORMATO_DATA_HORA);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-03:00"));

        return httpResposta.getCode() +
                " - " +
                httpResposta.getMensagem() +
                " - " +
                mensagem +
                " as " +
                dateFormat.format(timeStamp.getTime())
        ;
    }
}
