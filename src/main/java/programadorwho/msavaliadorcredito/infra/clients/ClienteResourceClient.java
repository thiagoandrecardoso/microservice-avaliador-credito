package programadorwho.msavaliadorcredito.infra.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import programadorwho.msavaliadorcredito.domain.model.DadosCliente;

@FeignClient(value="msclients", path="/clientes")
public interface ClienteResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<DadosCliente> datasClient(@RequestParam("cpf") String cpf);

}
