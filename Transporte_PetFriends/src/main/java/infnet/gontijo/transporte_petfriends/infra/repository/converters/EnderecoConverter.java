package infnet.gontijo.transporte_petfriends.infra.repository.converters;

import infnet.gontijo.transporte_petfriends.domain.Endereco;
import infnet.gontijo.transporte_petfriends.infra.service.ConhecimentoDeTransporteService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter(autoApply = true)
public class EnderecoConverter implements AttributeConverter<Endereco, String> {

    private static final Logger LOG = LoggerFactory.getLogger(ConhecimentoDeTransporteService.class);

    @Override
    public String convertToDatabaseColumn(Endereco endereco) {
        return endereco.toString();
    }

    @Override
    public Endereco convertToEntityAttribute(String enderecoStr) {
        return Endereco.fromString(enderecoStr);
    }
}