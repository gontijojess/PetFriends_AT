package infnet.gontijo.pedidos_petfriends;
import infnet.gontijo.pedidos_petfriends.pedidos.domain.Pedido;
import infnet.gontijo.pedidos_petfriends.pedidos.infra.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PedidosPetFriendsApplication implements CommandLineRunner {

    @Autowired
    private PedidoService pedidoService;

    public static void main(String[] args) {
        SpringApplication.run(PedidosPetFriendsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Pedido pedido = new Pedido();
        pedido.setCustomerId(1L);
        pedido.adicionarItem(1L, 2, 10.90);
        pedido.adicionarItem(2L, 1, 5.50);
        pedidoService.criarPedido(pedido);

    }
}