package infnet.gontijo.almoxarifado_petfriends.controllers;

import infnet.gontijo.almoxarifado_petfriends.domain.OrdemDeServico;
import infnet.gontijo.almoxarifado_petfriends.dto.ResponsavelDTO;
import infnet.gontijo.almoxarifado_petfriends.infra.repository.OrdemDeServicoRepository;
import infnet.gontijo.almoxarifado_petfriends.infra.service.OrdemDeServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/ordem")
public class OrdemServicoController {

    @Autowired
    private OrdemDeServicoService service;

    @GetMapping("/{id}")
    public OrdemDeServico obterPorId(@PathVariable(value = "id") long id){
        return service.obterPorId(id);
    }

    @GetMapping()
    public List<OrdemDeServico> findAll(){
        return service.obterLista();
    }

    @PatchMapping("/definir-responsavel/{id}")
    public OrdemDeServico definirResponsavel(@PathVariable(value = "id") long id, @RequestBody ResponsavelDTO responsavelDTO){
        return service.definirResponsavel(id, responsavelDTO.getResponsavel(), responsavelDTO.getEmailResponsavel());
    }

    @PatchMapping("/pedido-separado/{id}")
    public OrdemDeServico enviarOrdem(@PathVariable(value = "id") long id){
        return service.enviarPedido(id);
    }

    @PatchMapping("/cancelar-pedido/{id}")
    public OrdemDeServico cancelarPedido(@PathVariable(value = "id") long id){
        return service.cancelarPedido(id);
    }

}