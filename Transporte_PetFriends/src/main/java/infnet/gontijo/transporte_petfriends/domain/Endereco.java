package infnet.gontijo.transporte_petfriends.domain;

import java.io.Serializable;
import java.util.Objects;

public class Endereco implements Serializable {
    private final String rua;
    private final String numero;
    private final String bairro;
    private final String cidade;
    private final String estado;
    private final String cep;

    public Endereco(String estado, String cidade, String bairro, String rua, String numero, String cep) {
        if (rua == null || rua.isEmpty()) {
            throw new IllegalArgumentException("Rua inválida");
        }
        if (numero == null || numero.isEmpty()) {
            throw new IllegalArgumentException("Número inválido");
        }
        if (bairro == null || bairro.isEmpty()) {
            throw new IllegalArgumentException("Bairro inválido");
        }
        if (cidade == null || cidade.isEmpty()) {
            throw new IllegalArgumentException("Cidade inválida");
        }
        if (estado == null || estado.isEmpty()) {
            throw new IllegalArgumentException("Estado inválido");
        }
        if (cep == null || !isValidCep(cep)) {
            throw new IllegalArgumentException("CEP inválido");
        }
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }

    private boolean isValidCep(String cep) {
        return cep.matches("\\d{5}-\\d{3}");
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        Endereco endereco = (Endereco) objeto;
        return Objects.equals(rua, endereco.rua) &&
                Objects.equals(numero, endereco.numero) &&
                Objects.equals(bairro, endereco.bairro) &&
                Objects.equals(cidade, endereco.cidade) &&
                Objects.equals(estado, endereco.estado) &&
                Objects.equals(cep, endereco.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estado, cidade, bairro, rua, numero, cep);
    }

    public String toString() {
        return String.join(", ", estado, cidade, bairro, rua, numero, cep);
    }

    public static Endereco fromString(String enderecoStr) {
        String[] partes = enderecoStr.split(",");
        if (partes.length != 6) {
            throw new IllegalArgumentException("Formato de endereço inválido");
        }
        return new Endereco(partes[0].trim(), partes[1].trim(), partes[2].trim(), partes[3].trim(), partes[4].trim(), partes[5].trim());
    }
}
