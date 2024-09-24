package infnet.gontijo.transporte_petfriends.controllers;

import infnet.gontijo.transporte_petfriends.domain.CNPJ;
import infnet.gontijo.transporte_petfriends.domain.ConhecimentoDeTransporte;
import infnet.gontijo.transporte_petfriends.dto.EnderecoDTO;
import infnet.gontijo.transporte_petfriends.infra.repository.ConhecimentoDeTransporteRepository;
import infnet.gontijo.transporte_petfriends.infra.service.ConhecimentoDeTransporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transporte")
public class ConhecimentoDeTransporteController {

    @Autowired
    private ConhecimentoDeTransporteService service;

    @GetMapping("/{id}")
    public ConhecimentoDeTransporte obterPorId(@PathVariable(value = "id") long id){
        return service.obterPorId(id);
    }

    @GetMapping()
    public List<ConhecimentoDeTransporte> findAll(){
        return service.obterLista();
    }

    @PatchMapping("/pedido-rota/{id}")
    public ConhecimentoDeTransporte pedidoEmRota(@PathVariable(value = "id") long id, @RequestBody EnderecoDTO enderecoDTO){
        return service.pedidoEmRota(id, enderecoDTO.getEstado(), enderecoDTO.getCidade(), enderecoDTO.getBairro(), enderecoDTO.getRua(), enderecoDTO.getNumero(), enderecoDTO.getCep());
    }

    @PatchMapping("/alterar-transportadora/{id}")
    public ConhecimentoDeTransporte alterarTransportadora(@PathVariable(value = "id") long  id, @RequestBody String cnpj){
        return service.alterarTransportadora(id, cnpj);
    }

    @PatchMapping("/pedido-entregue/{id}")
    public ConhecimentoDeTransporte pedidoEntregue(@PathVariable(value = "id") long  id){
        return service.pedidoEntregue(id);
    }

    @PatchMapping("/pedido-extraviado/{id}")
    public ConhecimentoDeTransporte pedidoExtraviado(@PathVariable(value = "id") long  id){
        return service.pedidoExtraviado(id);
    }

}