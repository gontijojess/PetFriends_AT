package infnet.gontijo.pedidos_petfriends.pedidos.infra.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import infnet.gontijo.pedidos_petfriends.pedidos.domain.Pedido;
import infnet.gontijo.pedidos_petfriends.pedidos.domain.PedidoStatus;
import infnet.gontijo.pedidos_petfriends.pedidos.eventos.EstadoPedidoMudou;
import infnet.gontijo.pedidos_petfriends.pedidos.infra.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PedidoService {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PubSubTemplate pubSubTemplate;
    @Autowired
    JacksonPubSubMessageConverter converter;
    @Autowired
    private PedidoRepository repository;

    public Pedido obterPorId(long id) {
        return repository.getReferenceById(id);
    }

    public Pedido fecharPedido(long id) {
        Pedido pedido = repository.getReferenceById(id);
        pedido.fecharPedido();
        pedido = repository.save(pedido);
        enviarAlmoxarifado(new EstadoPedidoMudou(pedido.getId(), PedidoStatus.FECHADO));
        return pedido;
    }

    public Pedido cancelarPedido(long id) {
        Pedido pedido = repository.getReferenceById(id);
        pedido.cancelarPedido();
        pedido = repository.save(pedido);
        return pedido;
    }

    public Pedido criarPedido(Pedido pedido) {
        Pedido retorno = null;
        if (pedido != null) {
            retorno = repository.save(pedido);
        }
        return retorno;
    }

    public void processarEvento(EstadoPedidoMudou evento) {
        Long id = evento.getIdPedido();
        switch (evento.getEstado().toString()) {
            case "NOVO":
                break;
            case "FECHADO":
                break;
            case "EM_PREPARACAO":
                LOG.info("***** EM_PREPARACAO ---> " + id);
                try{
                    Pedido pedido = repository.findById(id).orElse(null);
                    pedido.prepararPedido();
                    LOG.info("***** EM_PREPARACAO ---> " + pedido);
                    repository.save(pedido);
                    break;
                } catch (Exception ex) {
                    LOG.info("***** ERRO AO PREPARAR ---> " + ex.getMessage());
                }
            case "EM_TRANSITO":
                LOG.info("***** EM_TRANSITO ---> " + id);
                try{
                    Pedido pedido = repository.findById(id).orElse(null);
                    pedido.enviarPedido();
                    LOG.info("***** EM_TRANSITO ---> " + pedido);
                    repository.save(pedido);
                    enviarTransporte(new EstadoPedidoMudou(pedido.getId(), PedidoStatus.EM_TRANSITO));
                    break;
                } catch (Exception ex) {
                    LOG.info("***** ERRO AO ENVIAR ---> " + ex.getMessage());
                }
                break;
            case "ENTREGUE":
                LOG.info("***** ENTREGUE ---> " + id);
                try{
                    Pedido pedido = repository.findById(id).orElse(null);
                    pedido.entregarPedido();
                    LOG.info("***** ENTREGUE ---> " + pedido);
                    repository.save(pedido);
                    break;
                } catch (Exception ex) {
                    LOG.info("***** ERRO AO TENTAR ENTREGAR ---> " + ex.getMessage());
                }
                break;
            case "CANCELADO":
                LOG.info("***** CANCELADO ---> " + id);
                try{
                    Pedido pedido = repository.findById(id).orElse(null);
                    pedido.cancelarPedido();
                    LOG.info("***** CANCELADO ---> " + pedido);
                    repository.save(pedido);
                    break;
                } catch (Exception ex) {
                    LOG.info("***** ERRO AO TENTAR CANCELAR ---> " + ex.getMessage());
                }
                break;
        }

    }

    private void enviarAlmoxarifado(EstadoPedidoMudou estado) {
        Map<String, String> headers = new HashMap<>();
        headers.put("source", "Almoxarifado");
        pubSubTemplate.setMessageConverter(converter);
        pubSubTemplate.publish("PetFriends", estado, headers);
        LOG.info("***** Mensagem Publicada ---> " + estado);
    }

    private void enviarTransporte(EstadoPedidoMudou estado) {
        Map<String, String> headers = new HashMap<>();
        headers.put("source", "Transporte");
        pubSubTemplate.setMessageConverter(converter);
        pubSubTemplate.publish("PetFriends", estado, headers);
        LOG.info("***** Mensagem Publicada ---> " + estado);
    }

    @ServiceActivator(inputChannel = "inputMessageChannel")
    private void receber(EstadoPedidoMudou payload,
                         @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) ConvertedBasicAcknowledgeablePubsubMessage<EstadoPedidoMudou> message,
                         @Header("source") String source) {

        if ("Pedido".equals(source)) {
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
}
