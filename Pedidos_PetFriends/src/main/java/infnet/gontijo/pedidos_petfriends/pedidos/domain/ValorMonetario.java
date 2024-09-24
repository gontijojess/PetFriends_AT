package infnet.gontijo.pedidos_petfriends.pedidos.domain;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ValorMonetario implements Serializable {

    private final BigDecimal quantia;

    public ValorMonetario(BigDecimal quantia) {
        if (quantia == null || quantia.signum() < 0) {
            throw new IllegalArgumentException("Valor Monetário não pode ser negativo");
        }
        this.quantia = quantia.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getQuantia() {
        return this.quantia;
    }

    public ValorMonetario somar(ValorMonetario outro) {
        if (outro == null) {
            throw new IllegalArgumentException("Outro valor não pode ser nulo");
        }
        return new ValorMonetario(this.quantia.add(outro.getQuantia()));
    }

    public ValorMonetario subtrair(ValorMonetario outro) {
        if (outro == null) {
            throw new IllegalArgumentException("Outro valor não pode ser nulo");
        }
        return new ValorMonetario(this.quantia.subtract(outro.getQuantia()));
    }

    @Override
    public boolean equals(Object objeto) {
        final ValorMonetario outro = (ValorMonetario) objeto;
        return Objects.equals(this.quantia, outro.getQuantia());
    }
}
