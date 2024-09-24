package infnet.gontijo.transporte_petfriends.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_transporte", catalog = "transportedb", schema = "PUBLIC")
public class ItemTransporte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private Integer quantity;
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Column(name = "PRODUCT_ID")
    private Long productId;
    @ManyToOne
    @JoinColumn(name = "ordem_preparacao_id", nullable = false)
    private ConhecimentoDeTransporte conhecimentoDeTransporte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public ConhecimentoDeTransporte getConhecimentoDeTransporte() {
        return conhecimentoDeTransporte;
    }

    public void setConhecimentoDeTransporte(ConhecimentoDeTransporte conhecimentoDeTransporte) {
        this.conhecimentoDeTransporte = conhecimentoDeTransporte;
    }
}