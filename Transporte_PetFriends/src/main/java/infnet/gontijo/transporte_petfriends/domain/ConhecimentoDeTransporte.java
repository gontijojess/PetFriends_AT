package infnet.gontijo.transporte_petfriends.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import infnet.gontijo.transporte_petfriends.domain.enums.TransporteStatus;
import infnet.gontijo.transporte_petfriends.infra.repository.converters.CNPJConverter;
import infnet.gontijo.transporte_petfriends.infra.repository.converters.EnderecoConverter;
import infnet.gontijo.transporte_petfriends.infra.repository.converters.TransporteStatusConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "conhecimento_de_transporte", catalog = "transportedb", schema = "PUBLIC")
public class ConhecimentoDeTransporte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(name = "SHIPPING_DATE")
    @Temporal(TemporalType.DATE)
    private Date shippingDate;
    @Convert(converter = CNPJConverter.class)
    @Column(name = "CNPJ_FORNECEDOR")
    private CNPJ empresaFornecedoraCNPJ;
    @Convert(converter = CNPJConverter.class)
    @Column(name = "CNPJ_TRANSPORTE")
    private CNPJ empresaTransporteCNPJ;
    @Column(name = "ORDER_ID")
    private Long orderId;
    //Outras informações poderiam ser puxadas do microsserviço de customer a partir do id
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @Convert(converter = EnderecoConverter.class)
    @Column(name = "ENDEREÇO")
    private Endereco endereco;
    @Convert(converter = TransporteStatusConverter.class)
    @Column(name = "TRANSPORTE_STATUS")
    private TransporteStatus status;
    @JsonIgnoreProperties(value = "conhecimentoDeTransporte")
    @OneToMany(mappedBy = "conhecimentoDeTransporte", cascade = CascadeType.ALL)
    private List<ItemTransporte> itens;

    public ConhecimentoDeTransporte(){
        this.shippingDate = new Date();
        this.empresaFornecedoraCNPJ = new CNPJ("99786659554329");
        this.empresaTransporteCNPJ = new CNPJ("99999999999999");
        this.endereco = new Endereco("Teste","Teste","Teste","Teste","Teste","77777-777");
        this.status = TransporteStatus.AGUARDANDO;
    }

    public ConhecimentoDeTransporte(Long id){
        this.id = id;
        this.shippingDate = new Date();
        this.empresaFornecedoraCNPJ = new CNPJ("99786659554329");
        this.empresaTransporteCNPJ = new CNPJ("99999999999999");
        this.endereco = new Endereco("Teste","Teste","Teste","Teste","Teste","77777-777");

        this.status = TransporteStatus.AGUARDANDO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public CNPJ getEmpresaFornecedoraCNPJ() {
        return empresaFornecedoraCNPJ;
    }

    public void setEmpresaFornecedoraCNPJ(CNPJ empresaFornecedoraCNPJ) {
        this.empresaFornecedoraCNPJ = empresaFornecedoraCNPJ;
    }

    public CNPJ getEmpresaTransporteCNPJ() {
        return empresaTransporteCNPJ;
    }

    public void setEmpresaTransporteCNPJ(CNPJ empresaTransporteCNPJ) {
        this.empresaTransporteCNPJ = empresaTransporteCNPJ;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public TransporteStatus getStatus() {
        return status;
    }

    public void setStatus(TransporteStatus status) {
        this.status = status;
    }

    public List<ItemTransporte> getItens() {
        return itens;
    }

    public void setItens(List<ItemTransporte> itens) {
        this.itens = itens;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
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
        if (!(object instanceof ConhecimentoDeTransporte)) {
            return false;
        }
        ConhecimentoDeTransporte other = (ConhecimentoDeTransporte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Conhecimento de Transporte{" + "id=" + id + ", shippingDate=" + shippingDate + ", empresaFornecedoraCNPJ=" + empresaFornecedoraCNPJ + ", empresaTransporteCNPJ=" + empresaTransporteCNPJ + ", customerId=" + customerId + ", endereco=" + endereco + ", status=" + status + ", itens=" + itens + '}';
    }

    public void pedidoEmRota() {
        if (this.status != TransporteStatus.AGUARDANDO) {
            throw new IllegalStateException("Não é possível enviar um pedido que não se encontra aguardando.");
        }
        this.status = TransporteStatus.EM_ROTA;
    }

    public void entregarPedido() {
        if (this.status != TransporteStatus.EM_ROTA) {
            throw new IllegalStateException("Não é possível entregar um pedido que não se encontra separado.");
        }
        this.status = TransporteStatus.ENTREGUE;
    }

    public void notificarPedidoExtraviado() {
        if (this.status == TransporteStatus.ENTREGUE) {
            throw new IllegalStateException("Não é possível notificar extravio se o pedido já foi entregue.");
        }
        this.status = TransporteStatus.EXTRAVIADO;
    }
}
