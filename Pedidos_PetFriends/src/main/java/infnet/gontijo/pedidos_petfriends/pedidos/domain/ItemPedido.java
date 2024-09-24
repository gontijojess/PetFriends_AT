package infnet.gontijo.pedidos_petfriends.pedidos.domain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "itens_pedidos", catalog = "pedidodb", schema = "PUBLIC")
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private Integer quantity;
    @Column(precision = 20, scale = 2)
    private ValorMonetario total;
    @Column(name = "PRODUCT_ID")
    private Long productId;
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    @ManyToOne
    private Pedido orderId;

    public ItemPedido() {
    }

    public ItemPedido(Long id) {
        this.id = id;
    }

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

    public ValorMonetario getTotal() {
        return total;
    }

    public void setTotal(ValorMonetario total) {
        this.total = total;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Pedido getOrderId() {
        return orderId;
    }

    public void setOrderId(Pedido orderId) {
        this.orderId = orderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemPedido)) {
            return false;
        }
        ItemPedido other = (ItemPedido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "infnet.gontijo.pedidos_petfriends.domain.Item[ id=" + id + " ]";
    }

}
