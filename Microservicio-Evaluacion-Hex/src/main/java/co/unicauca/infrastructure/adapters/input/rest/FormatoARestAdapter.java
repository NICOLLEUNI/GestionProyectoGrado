package co.unicauca.infrastructure.adapters.input.rest;

import co.unicauca.application.ports.input.FormatoAFacadeInPort;
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
    public ResponseEntity<List<FormatoAResponse>> listarFormatosA() {
        List<FormatoAResponse> formatos = formatoAFacade.listarFormatosA();
        return ResponseEntity.ok(formatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormatoAResponse> obtenerFormatoAPorId(@PathVariable Long id) {
        FormatoAResponse response = formatoAFacade.obtenerFormatoAPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/programa/{programa}")
    public ResponseEntity<List<FormatoAResponse>> listarFormatosPorPrograma(@PathVariable String programa) {
        List<FormatoAResponse> formatos = formatoAFacade.listarFormatosPorPrograma(programa);
        return ResponseEntity.ok(formatos);
    }

    @PutMapping("/{id}/estado/{nuevoEstado}")
    public ResponseEntity<FormatoAResponse> actualizarEstado(
            @PathVariable Long id,
            @PathVariable String nuevoEstado,
            @RequestParam(required = false) String observaciones
    ) {
        FormatoAResponse response = formatoAFacade.actualizarEstado(id,
                Enum.valueOf(co.unicauca.domain.entities.EnumEstado.class, nuevoEstado),
                observaciones);
        return ResponseEntity.ok(response);
    }
}
