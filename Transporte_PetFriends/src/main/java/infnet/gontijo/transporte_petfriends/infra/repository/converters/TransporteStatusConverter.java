package infnet.gontijo.transporte_petfriends.infra.repository.converters;
import infnet.gontijo.transporte_petfriends.domain.enums.TransporteStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransporteStatusConverter implements AttributeConverter<TransporteStatus, String> {

    @Override
    public String convertToDatabaseColumn(TransporteStatus transporteStatus) {
        return transporteStatus.toString();
    }

    @Override
    public TransporteStatus convertToEntityAttribute(String transporteStatus) {
        return TransporteStatus.valueOf(transporteStatus);
    }
}