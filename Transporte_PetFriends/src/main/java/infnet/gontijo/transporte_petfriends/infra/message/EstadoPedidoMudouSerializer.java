package infnet.gontijo.transporte_petfriends.infra.message;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import infnet.gontijo.transporte_petfriends.eventos.EstadoPedidoMudou;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class EstadoPedidoMudouSerializer extends StdSerializer<EstadoPedidoMudou> {

    public EstadoPedidoMudouSerializer() {
        super(EstadoPedidoMudou.class);
    }

    @Override
    public void serialize(EstadoPedidoMudou evento, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("idPedido", evento.getIdPedido());
        jgen.writeStringField("estado", evento.getEstado().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
        String data = sdf.format(evento.getMomento());
        jgen.writeStringField("momento", data);
    }
}

