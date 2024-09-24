package infnet.gontijo.pedidos_petfriends.pedidos.infra.repository;
import infnet.gontijo.pedidos_petfriends.pedidos.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository  extends JpaRepository<Pedido, Long> {
}
