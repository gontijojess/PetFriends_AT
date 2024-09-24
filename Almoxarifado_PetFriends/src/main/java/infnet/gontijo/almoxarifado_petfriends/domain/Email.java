package infnet.gontijo.almoxarifado_petfriends.domain;

import java.io.Serializable;
import java.util.Objects;

public class Email implements Serializable {

    private final String endereco;

    public Email(String endereco) {
        if (endereco == null || !isValidEmail(endereco)) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.endereco = endereco;
    }

    public String getEndereco() {
        return endereco;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    @Override
    public boolean equals(Object objeto) {
            final Email outro = (Email) objeto;
        return Objects.equals(this.endereco, outro.getEndereco());
    }

    @Override
    public String toString() {
        return endereco;
    }
}

