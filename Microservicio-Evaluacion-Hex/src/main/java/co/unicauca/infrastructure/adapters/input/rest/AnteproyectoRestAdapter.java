package co.unicauca.infrastructure.adapters.input.rest;

import co.unicauca.application.ports.input.AnteproyectoFacadeInPort;
import co.unicauca.application.services.facade.AnteproyectoFacade;
import co.unicauca.domain.entities.Anteproyecto;
import co.unicauca.infrastructure.dto.request.AnteproyectoRequest;
import co.unicauca.infrastructure.dto.response.AnteproyectoResponse;
import co.unicauca.infrastructure.dto.notification.AnteproyectoResponseNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/anteproyectos")
public class AnteproyectoRestAdapter {

    private final AnteproyectoFacadeInPort anteproyectoFacade;


    public AnteproyectoRestAdapter(AnteproyectoFacadeInPort anteproyectoFacade) {
        this.anteproyectoFacade = anteproyectoFacade;
    }

    @PostMapping
    public ResponseEntity<Anteproyecto> crearAnteproyecto(@RequestBody AnteproyectoRequest request) {
        // Llamas al servicio y obtienes la entidad completa
        Anteproyecto anteproyecto = anteproyectoFacade.crearAnteproyecto(request);

        // Devuelves la entidad directamente en ResponseEntity
        return ResponseEntity.ok(anteproyecto);
    }

    @GetMapping
    public ResponseEntity<List<AnteproyectoResponse>> listarAnteproyectos() {
        List<AnteproyectoResponse> lista = anteproyectoFacade.listarAnteproyectos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnteproyectoResponse> buscarPorId(@PathVariable Long id) {
        AnteproyectoResponse response = anteproyectoFacade.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/evaluadores")
    public ResponseEntity<AnteproyectoResponseNotification> asignarEvaluadores(
            @PathVariable Long id,
            @RequestParam String email1,
            @RequestParam String email2
    ) {
        AnteproyectoResponseNotification response = anteproyectoFacade.asignarEvaluadores(id, email1, email2);
        return ResponseEntity.ok(response);
    }
}
