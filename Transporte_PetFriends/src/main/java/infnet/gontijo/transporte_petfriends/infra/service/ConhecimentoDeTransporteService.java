package infnet.gontijo.transporte_petfriends.infra.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import infnet.gontijo.transporte_petfriends.domain.CNPJ;
import infnet.gontijo.transporte_petfriends.domain.ConhecimentoDeTransporte;
import infnet.gontijo.transporte_petfriends.domain.Endereco;
import infnet.gontijo.transporte_petfriends.domain.ItemTransporte;
import infnet.gontijo.transporte_petfriends.domain.enums.PedidoStatus;
import infnet.gontijo.transporte_petfriends.dto.PedidoDTO;
import infnet.gontijo.transporte_petfriends.eventos.EstadoPedidoMudou;
import infnet.gontijo.transporte_petfriends.infra.repository.ConhecimentoDeTransporteRepository;
import infnet.gontijo.transporte_petfriends.infra.service.feign.PedidoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConhecimentoDeTransporteService {

    private static final Logger LOG = LoggerFactory.getLogger(ConhecimentoDeTransporteService.class);

    @Autowired
    private PubSubTemplate pubSubTemplate;
    @Autowired
    JacksonPubSubMessageConverter converter;
    @Autowired
    private ConhecimentoDeTransporteRepository repository;
    @Autowired
    private PedidoClient pedidoClient;

    public ConhecimentoDeTransporte obterPorId(long id) {
        return repository.getReferenceById(id);
    }

    public List<ConhecimentoDeTransporte> obterLista() {
        return repository.findAll();
    }

    public ConhecimentoDeTransporte pedidoExtraviado(long id) {
        ConhecimentoDeTransporte cdt = repository.getReferenceById(id);
        if (!Objects.equals(cdt.getEndereco().getCep(), "77777777")){
            cdt.notificarPedidoExtraviado();
            cdt = repository.save(cdt);
            return cdt;
        }
        return cdt;
    }

    public ConhecimentoDeTransporte pedidoEntregue(long id) {
        ConhecimentoDeTransporte cdt = repository.getReferenceById(id);
        cdt.entregarPedido();
        cdt = repository.save(cdt);
        enviar(new EstadoPedidoMudou(cdt.getOrderId(), PedidoStatus.ENTREGUE));
        return cdt;
    }

    public ConhecimentoDeTransporte alterarTransportadora(long id, String cnpjString) {
        ConhecimentoDeTransporte cdt = repository.getReferenceById(id);
        CNPJ cnpj = new CNPJ(cnpjString);
        cdt.setEmpresaTransporteCNPJ(cnpj);
        cdt = repository.save(cdt);
        return cdt;
    }

    public ConhecimentoDeTransporte pedidoEmRota(long id, String estado, String cidade, String bairro, String rua, String numero, String cep) {
        ConhecimentoDeTransporte cdt = repository.getReferenceById(id);
        LOG.info("***** DEFININDO ENDEREÇO ---> CDT: " + cdt);
        if (!Objects.equals(cdt.getEndereco().getCep(), "77777777")){
            Endereco endereco = new Endereco(estado, cidade, bairro, rua, numero, cep);
            cdt.setEndereco(endereco);
            cdt.pedidoEmRota();
            cdt = repository.save(cdt);
        }
        return cdt;
    }

    public ConhecimentoDeTransporte criarConhecimentoDeTransporte(Long idPedido) {
        ConhecimentoDeTransporte retorno = null;
        try{
            Optional<PedidoDTO> pedido = buscarPedido(idPedido);
            LOG.info("***** GERANDO CDT ---> pedido: " + pedido);
            if (pedido != null) {
                LOG.info("***** PEDIDO != null ---> ");
                ConhecimentoDeTransporte cdt = new ConhecimentoDeTransporte();
                cdt.setCustomerId(pedido.get().getCustomerId());
                cdt.setOrderId(pedido.get().getId());
                LOG.info("***** CUSTOMER E ORDER ID ---> pedido: " + cdt);

                List<ItemTransporte> itensOrdem = pedido.get().getItemList().stream()
                        .map(itemDto -> {
                            ItemTransporte itemTransporte = new ItemTransporte();
                            itemTransporte.setProductId(itemDto.getProductId());
                            itemTransporte.setQuantity(itemDto.getQuantity());
                            itemTransporte.setTotal(itemDto.getTotal().getQuantia());
                            itemTransporte.setConhecimentoDeTransporte(cdt);
                            return itemTransporte;
                        })
                        .collect(Collectors.toList());
                cdt.setItens(itensOrdem);
                LOG.info("***** CRIEI LISTA ---> pedido: " + cdt.getItens());

                retorno = repository.save(cdt);
                LOG.info("***** CDT GERADO ---> cdt: " + cdt);
            }
        } catch(Exception ex) {
            LOG.info("***** ERRO AO PREPARAR ---> " + ex.getMessage());
        }
        return retorno;
    }


    public void processarEvento(EstadoPedidoMudou evento) {
        switch (evento.getEstado().toString()) {
            case "NOVO":
                break;
            case "FECHADO":
                break;
            case "EM_PREPARACAO":
                break;
            case "EM_TRANSITO":
                Long idPedido = evento.getIdPedido();
                LOG.info("***** EM_TRANSITO ---> id Pedido: " + idPedido);
                criarConhecimentoDeTransporte(idPedido);
                break;
            case "ENTREGUE":
                break;
            case "CANCELADO":
                break;
        }
    }

    private void enviar(EstadoPedidoMudou estado) {
        Map<String, String> headers = new HashMap<>();
        headers.put("source", "Pedido");
        pubSubTemplate.setMessageConverter(converter);
        pubSubTemplate.publish("PetFriends", estado, headers);
        LOG.info("***** Mensagem Publicada ---> " + estado);
    }

    @ServiceActivator(inputChannel = "inputMessageChannel")
    private void receber(EstadoPedidoMudou payload,
                         @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) ConvertedBasicAcknowledgeablePubsubMessage<EstadoPedidoMudou> message,
                         @Header("source") String source) {

        if ("Transporte".equals(source)) {
            LOG.info("***** Mensagem Recebida ---> " + payload);
            message.ack();
            this.processarEvento(payload);
        } else {
            LOG.info("***** Mensagem ignorada, não é do serviço Pedido.");
            message.nack();
        }

//        LOG.info("***** Mensagem Recebida ---> " + payload);
//        message.ack();
//        this.processarEvento(payload);
    }

    public Optional<PedidoDTO> buscarPedido(Long pedidoId) {
        LOG.info("***** BUSCANDO PEDIDO DTO SERVICE ---> " );
        return pedidoClient.getPedidoById(pedidoId);
    }

}
