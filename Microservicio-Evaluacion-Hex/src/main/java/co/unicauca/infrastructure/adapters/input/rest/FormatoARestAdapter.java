package co.unicauca.infrastructure.adapters.input.rest;

import co.unicauca.application.ports.input.FormatoAFacadeInPort;
import co.unicauca.domain.entities.EnumEstado;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.infrastructure.dto.request.FormatoARequest;
import co.unicauca.infrastructure.dto.response.FormatoAResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/formatoA")
public class FormatoARestAdapter {

    private final FormatoAFacadeInPort formatoAFacade;

    public FormatoARestAdapter(FormatoAFacadeInPort formatoAFacade) {
        this.formatoAFacade = formatoAFacade;
    }

    @PostMapping
    public ResponseEntity<FormatoAResponse> crearFormatoA(@RequestBody FormatoARequest request) {
        FormatoAResponse response = formatoAFacade.crearFormatoA(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FormatoA>> listarFormatosA() {
        List<FormatoA> formatos = formatoAFacade.listarFormatosA();
        return ResponseEntity.ok(formatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormatoA> obtenerFormatoAPorId(@PathVariable Long id) {
        FormatoA response = formatoAFacade.obtenerFormatoAPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/programa/{programa}")
    public ResponseEntity<List<FormatoA>> listarFormatosPorPrograma(@PathVariable String programa) {
        List<FormatoA> formatos = formatoAFacade.listarFormatosPorPrograma(programa);
        return ResponseEntity.ok(formatos);
    }

    @PutMapping("/{id}/estado/{nuevoEstado}/{observaciones}")
    public ResponseEntity<FormatoAResponse> actualizarEstado(
            @PathVariable Long id,
            @PathVariable EnumEstado nuevoEstado,
            @PathVariable (required = false)String observaciones
    ) {
        FormatoAResponse response = formatoAFacade.actualizarEstado(id, nuevoEstado, observaciones);
        return ResponseEntity.ok(response);
    }
}
