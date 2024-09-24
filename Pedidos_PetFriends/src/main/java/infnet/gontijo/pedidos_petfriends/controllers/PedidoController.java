package infnet.gontijo.pedidos_petfriends.controllers;

import infnet.gontijo.pedidos_petfriends.pedidos.domain.Pedido;
import infnet.gontijo.pedidos_petfriends.pedidos.infra.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @GetMapping("/{id}")
    public Pedido obterPorId(@PathVariable(value = "id") long id){
        return service.obterPorId(id);
    }

    @PatchMapping("/fechar-pedido/{id}")
    public Pedido fecharPedido(@PathVariable(value = "id") long id){
        return service.fecharPedido(id);
    }

    @PatchMapping("/cancelar-pedido/{id}")
    public Pedido cancelarPedido(@PathVariable(value = "id") long id){
        return service.cancelarPedido(id);
    }
}
