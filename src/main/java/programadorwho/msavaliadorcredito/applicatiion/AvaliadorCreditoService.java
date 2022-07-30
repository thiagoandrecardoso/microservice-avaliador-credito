package programadorwho.msavaliadorcredito.applicatiion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import programadorwho.msavaliadorcredito.domain.model.DadosCliente;
import programadorwho.msavaliadorcredito.domain.model.SituacaoCliente;
import programadorwho.msavaliadorcredito.infra.clients.ClienteResourceClient;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;

    public SituacaoCliente obtersituacaoCliente(String cpf) {

        ResponseEntity<DadosCliente> dadosClienteResponseEntity = clienteResourceClient.datasClient(cpf);

        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponseEntity.getBody())
                .build();
    }
}
