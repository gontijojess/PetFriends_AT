package infnet.gontijo.almoxarifado_petfriends.infra.repository;

import infnet.gontijo.almoxarifado_petfriends.domain.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email.getEndereco();
    }

    @Override
    public Email convertToEntityAttribute(String endereco) {
        return new Email(endereco);
    }
}