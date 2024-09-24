package infnet.gontijo.transporte_petfriends.dto;

import lombok.Data;

@Data
public class EnderecoDTO {
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
