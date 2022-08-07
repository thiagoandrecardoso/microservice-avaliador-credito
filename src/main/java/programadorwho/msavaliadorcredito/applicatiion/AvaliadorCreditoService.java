package programadorwho.msavaliadorcredito.applicatiion;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import programadorwho.msavaliadorcredito.applicatiion.exception.DadosClientNotFoundException;
import programadorwho.msavaliadorcredito.applicatiion.exception.ErrorCommMicroServiceException;
import programadorwho.msavaliadorcredito.domain.model.*;
import programadorwho.msavaliadorcredito.infra.clients.CartoesResourceClient;
import programadorwho.msavaliadorcredito.infra.clients.ClienteResourceClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClientNotFoundException,
            ErrorCommMicroServiceException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponseEntity = clienteResourceClient.datasClient(cpf);
            ResponseEntity<List<Cartao>> cardReponseEntity = cartoesResourceClient.getCardRevenueTo(renda);

            List<Cartao> cartoes = cardReponseEntity.getBody();

            assert cartoes != null;
            var listaCartoesAprovados = cartoes.stream().map(cartao -> {

                DadosCliente dadosClienteR = dadosClienteResponseEntity.getBody();
                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBigDecimal = BigDecimal.valueOf(dadosClienteR.getIdade());

                var fator = idadeBigDecimal.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);
                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClientNotFoundException();
            }
            throw new ErrorCommMicroServiceException(e.getMessage(), status);
        }

    }
}
