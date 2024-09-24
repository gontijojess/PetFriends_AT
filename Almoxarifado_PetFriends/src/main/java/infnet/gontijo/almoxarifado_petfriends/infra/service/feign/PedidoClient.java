package infnet.gontijo.almoxarifado_petfriends.infra.service.feign;

import infnet.gontijo.almoxarifado_petfriends.dto.PedidoDTO;
import infnet.gontijo.almoxarifado_petfriends.infra.service.OrdemDeServicoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

@FeignClient(name = "pedido-service", url = "http://localhost:8080/pedidos")
public interface PedidoClient {
    @GetMapping({"/{id}"})
    Optional<PedidoDTO> getPedidoById(@PathVariable Long id);
}
