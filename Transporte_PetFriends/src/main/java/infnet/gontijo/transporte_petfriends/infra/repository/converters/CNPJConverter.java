package infnet.gontijo.transporte_petfriends.infra.repository.converters;
import infnet.gontijo.transporte_petfriends.domain.CNPJ;
import infnet.gontijo.transporte_petfriends.infra.service.ConhecimentoDeTransporteService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class CNPJConverter implements AttributeConverter<CNPJ, String> {

    @Override
    public String convertToDatabaseColumn(CNPJ cnpj) {
        return cnpj.getValor();
    }

    @Override
    public CNPJ convertToEntityAttribute(String valor) {
        return new CNPJ(valor);
    }
}