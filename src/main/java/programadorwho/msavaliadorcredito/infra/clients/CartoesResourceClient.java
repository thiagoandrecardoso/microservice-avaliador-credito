package programadorwho.msavaliadorcredito.infra.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import programadorwho.msavaliadorcredito.domain.model.Cartao;
import programadorwho.msavaliadorcredito.domain.model.CartaoCliente;

import java.util.List;

@FeignClient(value = "mscards", path="/cartoes")
public interface CartoesResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<List<CartaoCliente>> getCardByClient(@RequestParam("cpf") String cpf);

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCardRevenueTo(@RequestParam("renda") Long renda);
}
