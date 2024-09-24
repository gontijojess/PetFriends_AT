package infnet.gontijo.almoxarifado_petfriends.infra.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import infnet.gontijo.almoxarifado_petfriends.domain.Email;
import infnet.gontijo.almoxarifado_petfriends.domain.ItemPreparacao;
import infnet.gontijo.almoxarifado_petfriends.domain.OrdemDeServico;
import infnet.gontijo.almoxarifado_petfriends.domain.PedidoStatus;
import infnet.gontijo.almoxarifado_petfriends.dto.PedidoDTO;
import infnet.gontijo.almoxarifado_petfriends.eventos.EstadoPedidoMudou;
import infnet.gontijo.almoxarifado_petfriends.infra.repository.OrdemDeServicoRepository;
import infnet.gontijo.almoxarifado_petfriends.infra.service.feign.PedidoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdemDeServicoService {

    private static final Logger LOG = LoggerFactory.getLogger(OrdemDeServicoService.class);

    @Autowired
    private PubSubTemplate pubSubTemplate;
    @Autowired
    JacksonPubSubMessageConverter converter;
    @Autowired
    private OrdemDeServicoRepository repository;
    @Autowired
    private PedidoClient pedidoClient;


    public OrdemDeServico obterPorId(long id) {
        return repository.getReferenceById(id);
    }

    public List<OrdemDeServico> obterLista() {
        return repository.findAll();
    }

    public OrdemDeServico definirResponsavel(Long id, String responsavel, String emailString) {
        LOG.info("***** Def-resp --->");
        OrdemDeServico ods = repository.getReferenceById(id);
        LOG.info("***** Def-resp ---> ordem: " + ods);
        ods.setResponsavel(responsavel);
        Email email = new Email(emailString);
        ods.setEmailResponsavel(email);
        ods = repository.save(ods);
        LOG.info("***** Def-resp ---> ordem responsavel e status fechado: " + ods);
        enviar(new EstadoPedidoMudou(ods.getOrderId(), PedidoStatus.EM_PREPARACAO));
        return ods;
    }

    public OrdemDeServico enviarPedido(long id) {
        OrdemDeServico ods = repository.getReferenceById(id);
        if (!Objects.equals(ods.getEmailResponsavel().toString(), "default@example.com")){
            ods.fecharOrdem();
            ods = repository.save(ods);
            enviar(new EstadoPedidoMudou(ods.getOrderId(), PedidoStatus.EM_TRANSITO));
            return ods;
        }
        return ods;
    }

    public OrdemDeServico cancelarPedido(long id) {
        OrdemDeServico ods = repository.getReferenceById(id);
        enviar(new EstadoPedidoMudou(ods.getOrderId(), PedidoStatus.CANCELADO));
        return ods;
    }

    public OrdemDeServico criarOrdemDeServico(Long idPedido) {
        OrdemDeServico retorno = null;
        try{
            Optional<PedidoDTO> pedido = buscarPedido(idPedido);
            LOG.info("***** EM_PREPARACAO ---> pedido: " + pedido);
            if (pedido != null) {
                OrdemDeServico ordem = new OrdemDeServico();
                ordem.setOrderId(pedido.get().getId());
                ordem.setCustomerId(pedido.get().getCustomerId());
                ordem.setValorTotal(pedido.get().getValorTotal().getQuantia());

                List<ItemPreparacao> itensOrdem = pedido.get().getItemList().stream()
                        .map(itemDto -> {
                            ItemPreparacao itemOrdem = new ItemPreparacao();
                            itemOrdem.setProductId(itemDto.getProductId());
                            itemOrdem.setQuantity(itemDto.getQuantity());
                            itemOrdem.setTotal(itemDto.getTotal().getQuantia());
                            itemOrdem.setOrdemDeServico(ordem);
                            return itemOrdem;
                        })
                        .collect(Collectors.toList());
                ordem.setItens(itensOrdem);
                retorno = repository.save(ordem);
                LOG.info("***** EM_PREPARACAO Ordem final ---> " + ordem);
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
                Long idPedido = evento.getIdPedido();
                LOG.info("***** EM_PREPARACAO ---> id Pedido: " + idPedido);
                criarOrdemDeServico(idPedido);
                break;
            case "EM_PREPARACAO":
                break;
            case "EM_TRANSITO":
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

        if ("Almoxarifado".equals(source)) {
            LOG.info("***** Mensagem Recebida ---> " + payload);
            message.ack();
            this.processarEvento(payload);
        } else {
            LOG.info("***** Mensagem ignorada, não é do serviço Pedido.");
            message.nack();
        }
//
//        LOG.info("***** Mensagem Recebida ---> " + payload);
//        message.ack();
//        this.processarEvento(payload);
    }

    public Optional<PedidoDTO> buscarPedido(Long pedidoId) {
        LOG.info("***** BUSCANDO PEDIDO DTO SERVICE ---> " );
        return pedidoClient.getPedidoById(pedidoId);
    }
}
