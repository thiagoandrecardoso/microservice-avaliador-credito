package programadorwho.msavaliadorcredito.applicatiion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import programadorwho.msavaliadorcredito.domain.model.CartaoCliente;
import programadorwho.msavaliadorcredito.domain.model.DadosCliente;
import programadorwho.msavaliadorcredito.domain.model.SituacaoCliente;
import programadorwho.msavaliadorcredito.infra.clients.CartoesResourceClient;
import programadorwho.msavaliadorcredito.infra.clients.ClienteResourceClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;

    public SituacaoCliente obtersituacaoCliente(String cpf) {

        ResponseEntity<DadosCliente> dadosClienteResponseEntity = clienteResourceClient.datasClient(cpf);
        ResponseEntity<List<CartaoCliente>> cartoesResponseEntity = cartoesResourceClient.getCardByClient(cpf);

        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponseEntity.getBody())
                .cartoes(cartoesResponseEntity.getBody())
                .build();
    }
}
