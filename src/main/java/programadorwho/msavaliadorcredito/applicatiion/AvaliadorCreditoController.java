package programadorwho.msavaliadorcredito.applicatiion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import programadorwho.msavaliadorcredito.applicatiion.exception.DadosClientNotFoundException;
import programadorwho.msavaliadorcredito.applicatiion.exception.ErrorCommMicroServiceException;
import programadorwho.msavaliadorcredito.domain.model.DadosAvaliacao;
import programadorwho.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import programadorwho.msavaliadorcredito.domain.model.SituacaoCliente;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliiadorCreditoService;

    @GetMapping
    public String status() {
        return "Ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
        SituacaoCliente situacaoCliente = null;
        try {
            situacaoCliente = avaliiadorCreditoService.obtersituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClientNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErrorCommMicroServiceException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados){
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliiadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadosClientNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErrorCommMicroServiceException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }
}
