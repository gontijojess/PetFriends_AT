package infnet.gontijo.pedidos_petfriends.pedidos.infra.repository;
import infnet.gontijo.pedidos_petfriends.pedidos.domain.PedidoStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PedidoStatusConverter implements AttributeConverter<PedidoStatus, String> {

    @Override
    public String convertToDatabaseColumn(PedidoStatus pedidoStatus) {
        return pedidoStatus.toString();
    }

    @Override
    public PedidoStatus convertToEntityAttribute(String pedidoStatus) {
        return PedidoStatus.valueOf(pedidoStatus);
    }
}