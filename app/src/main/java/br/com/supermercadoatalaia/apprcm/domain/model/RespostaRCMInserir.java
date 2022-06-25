package br.com.supermercadoatalaia.apprcm.domain.model;

import java.io.Serializable;
import java.util.List;

import br.com.supermercadoatalaia.apprcm.dto.help.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespostaRCMInserir implements Serializable {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response implements Serializable {

        private String status;
        private String message;
        private List<Message> messages;
        private String transacao;

        public String getMessagesToString() {
            String str = "\n**Mensagens Flex**\n";

            str += "{status=" + status + "}\n";
            str += "{msg_cabeçalho=" + message + "}\n";
            str += "{transação=" + transacao + "}\n";

            for(Message msg : messages) {
                str += "{msg=" + msg.getMessage() + "}\n";
            }

            return str;
        }
    }

    private Response response;
}
