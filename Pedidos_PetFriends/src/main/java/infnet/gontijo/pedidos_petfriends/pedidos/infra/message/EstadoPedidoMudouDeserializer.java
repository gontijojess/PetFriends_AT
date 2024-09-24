package infnet.gontijo.pedidos_petfriends.pedidos.infra.message;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import infnet.gontijo.pedidos_petfriends.pedidos.domain.PedidoStatus;
import infnet.gontijo.pedidos_petfriends.pedidos.eventos.EstadoPedidoMudou;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EstadoPedidoMudouDeserializer extends StdDeserializer<EstadoPedidoMudou> {

    public EstadoPedidoMudouDeserializer() {
        super(EstadoPedidoMudou.class);
    }

    @Override
    public EstadoPedidoMudou deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JacksonException {
        EstadoPedidoMudou evento = null;
        JsonNode node = jp.getCodec().readTree(jp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
        try {
            evento = new EstadoPedidoMudou(
                    node.get("idPedido").asLong(),
                    PedidoStatus.valueOf(node.get("estado").asText()),
                    sdf.parse(node.get("momento").asText())
            );
        } catch (ParseException e) {
            throw new IOException("Erro na data");
        }
        return evento;
    }
}
