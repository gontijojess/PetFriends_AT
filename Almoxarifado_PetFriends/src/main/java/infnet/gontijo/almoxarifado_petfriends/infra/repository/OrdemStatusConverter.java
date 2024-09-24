package infnet.gontijo.almoxarifado_petfriends.infra.repository;
import infnet.gontijo.almoxarifado_petfriends.domain.OrdemStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrdemStatusConverter implements AttributeConverter<OrdemStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrdemStatus ordemStatus) {
        return ordemStatus.toString();
    }

    @Override
    public OrdemStatus convertToEntityAttribute(String ordemStatus) {
        return OrdemStatus.valueOf(ordemStatus);
    }
}