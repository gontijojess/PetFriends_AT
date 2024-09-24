package infnet.gontijo.transporte_petfriends.infra.repository;

import infnet.gontijo.transporte_petfriends.domain.ConhecimentoDeTransporte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConhecimentoDeTransporteRepository extends JpaRepository<ConhecimentoDeTransporte, Long> {
}
