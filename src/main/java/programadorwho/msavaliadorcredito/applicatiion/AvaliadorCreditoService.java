package programadorwho.msavaliadorcredito.applicatiion;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import programadorwho.msavaliadorcredito.applicatiion.exception.DadosClientNotFoundException;
import programadorwho.msavaliadorcredito.applicatiion.exception.ErrorCommMicroServiceException;
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

    public SituacaoCliente obtersituacaoCliente(String cpf) throws DadosClientNotFoundException,
            ErrorCommMicroServiceException {

        try {
            ResponseEntity<DadosCliente> dadosClienteResponseEntity = clienteResourceClient.datasClient(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponseEntity = cartoesResourceClient.getCardByClient(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponseEntity.getBody())
                    .cartoes(cartoesResponseEntity.getBody())
                    .build();

        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClientNotFoundException();
            }
            throw new ErrorCommMicroServiceException(e.getMessage(), status);
        }
    }
}
