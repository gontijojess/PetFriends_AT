package infnet.gontijo.almoxarifado_petfriends;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AlmoxarifadoPetFriendsApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AlmoxarifadoPetFriendsApplication.class);

//    @Autowired
//    PedidoService service;
//    @Autowired
//    private PedidoService pedidoService;

    public static void main(String[] args) {
        SpringApplication.run(AlmoxarifadoPetFriendsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        Pedido pedido = new Pedido();
//        pedido.setCustomerId(1L);
//        pedido.adicionarItem(1L, 2, 10.90);
//        pedido.adicionarItem(2L, 1, 5.50);
//        pedidoService.criarPedido(pedido);


//        EstadoPedidoMudou estado = new EstadoPedidoMudou(1234L, PedidoStatus.FECHADO, new Date());
//        service.enviar(estado);
    }
}