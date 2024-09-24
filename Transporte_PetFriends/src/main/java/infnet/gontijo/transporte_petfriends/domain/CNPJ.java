package infnet.gontijo.transporte_petfriends.domain;

import java.io.Serializable;
import java.util.Objects;

public class CNPJ implements Serializable {

    private final String valor;

    public CNPJ(String valor) {
        if (valor == null || !isValidCNPJ(valor)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
        this.valor = formatCNPJ(valor);
    }

    public String getValor() {
        return valor;
    }

    private boolean isValidCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("[^\\d]", "");
        return cnpj.matches("\\d{14}");
    }

    private String formatCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("[^\\d]", "");
        return cnpj.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        CNPJ outro = (CNPJ) objeto;
        return Objects.equals(valor, outro.getValor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
