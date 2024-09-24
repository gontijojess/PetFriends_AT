package infnet.gontijo.almoxarifado_petfriends.infra.repository;
import infnet.gontijo.almoxarifado_petfriends.domain.OrdemDeServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, Long> {
}
