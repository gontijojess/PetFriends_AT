package infnet.gontijo.almoxarifado_petfriends.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import infnet.gontijo.almoxarifado_petfriends.infra.repository.EmailConverter;
import infnet.gontijo.almoxarifado_petfriends.infra.repository.OrdemStatusConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ordem_de_servico", catalog = "almoxarifadodb", schema = "PUBLIC")
public class OrdemDeServico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(name = "PREPARATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date preparationDate;
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @Column(name = "RESPONSAVEL")
    private String responsavel;
    @Convert(converter = EmailConverter.class)
    @Column(name = "EMAIL_RESPONSAVEL")
    private Email emailResponsavel;
    @Convert(converter = OrdemStatusConverter.class)
    @Column(name = "ORDER_STATUS")
    private OrdemStatus status;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @JsonIgnoreProperties(value = "ordemDeServico")
    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPreparacao> itens;

    public OrdemDeServico() {
        this.preparationDate = new Date();
        this.status = OrdemStatus.ABERTA;
        this.emailResponsavel= new Email("default@example.com");
    }

    public OrdemDeServico(Long id) {
        this.id = id;
        this.preparationDate = new Date();
        this.status = OrdemStatus.ABERTA;
        this.emailResponsavel= new Email("default@example.com");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPreparationDate() {
        return preparationDate;
    }

    public void setPreparationDate(Date preparationDate) {
        this.preparationDate = preparationDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public OrdemStatus getStatus() {
        return status;
    }

    public void setStatus(OrdemStatus status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemPreparacao> getItens() {
        return itens;
    }

    public void setItens(List<ItemPreparacao> itens) {
        this.itens = itens;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Email getEmailResponsavel() {
        return emailResponsavel;
    }

    public void setEmailResponsavel(Email emailResponsavel) {
        this.emailResponsavel = emailResponsavel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrdemDeServico)) {
            return false;
        }
        OrdemDeServico other = (OrdemDeServico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ordem de Servico {" + "id=" + id + ", orderDate=" + preparationDate + ", customerId=" + customerId + ", orderId=" + orderId + ", valorTotal=" + valorTotal + '}';
    }

    public void fecharOrdem() {
        if (this.status != OrdemStatus.ABERTA) {
            throw new IllegalStateException("Não é possível fechar um pedido que já se encontra fechado.");
        }
        this.status = OrdemStatus.FECHADA;
    }
}
