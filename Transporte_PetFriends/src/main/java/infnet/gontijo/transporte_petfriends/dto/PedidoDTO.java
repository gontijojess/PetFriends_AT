package infnet.gontijo.transporte_petfriends.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class PedidoDTO {
    private Long id;
    private Date orderDate;
    private List<ItemDTO> itemList;
    private Long customerId;
    private String status;
    private Total valorTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public List<ItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemDTO> itemList) {
        this.itemList = itemList;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Total getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Total valorTotal) {
        this.valorTotal = valorTotal;
    }

    public static class ItemDTO {
        private Long id;
        private int quantity;
        private Total total;
        private Long productId;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Total getTotal() {
            return total;
        }

        public void setTotal(Total total) {
            this.total = total;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ItemDTO{" +
                    "id=" + id +
                    ", quantity=" + quantity +
                    ", total=" + total +
                    ", productId=" + productId +
                    '}';
        }
    }

    public static class Total {
        private BigDecimal quantia;

        public BigDecimal getQuantia() {
            return quantia;
        }

        public void setQuantia(BigDecimal quantia) {
            this.quantia = quantia;
        }

        @Override
        public String toString() {
            return "Total{" +
                    "quantia=" + quantia +
                    '}';
        }
    }
}