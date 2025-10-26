package co.unicauca.service;

import co.unicauca.entity.FormatoAVersion;
import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.repository.FormatoAVersionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormatoAVersionService {

    private final FormatoAVersionRepository versionRepository;
    private static final Logger logger = LoggerFactory.getLogger(FormatoAVersionService.class);

    @Transactional
    public FormatoAVersionResponse crearVersion(FormatoAVersionRequest request) {
        FormatoAVersion version = new FormatoAVersion();

        version.setNumeroVersion(request.numVersion());
        version.setFecha(request.fecha());
        version.setTitle(request.title());
        version.setMode(EnumModalidad.valueOf(request.mode()));
        version.setState(EnumEstado.valueOf(request.state()));
        version.setObservations(request.observations());
        version.setCounter(request.counter());
        // ✅ AGREGADO: Guardar el idFormatoA que viene en el request
        version.setIdFormatoA(request.IdFormatoA());

        FormatoAVersion guardada = versionRepository.save(version);
        logger.info("✅ Versión {} creada para FormatoA: {} - {}",
                guardada.getNumeroVersion(), guardada.getIdFormatoA(), guardada.getTitle());

        return convertirAResponse(guardada);
    }

    @Transactional(readOnly = true)
    public FormatoAVersionResponse buscarPorId(Long id) {
        FormatoAVersion version = versionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Versión no encontrada"));
        return convertirAResponse(version);
    }

    @Transactional(readOnly = true)
    public List<FormatoAVersionResponse> listarTodas() {
        return versionRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FormatoAVersionResponse> buscarPorFormatoA(Long formatoAId) {
        // ✅ IMPLEMENTADO: Buscar versiones por idFormatoA
        return versionRepository.findByIdFormatoA(formatoAId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FormatoAVersionResponse> buscarPorProyecto(Long proyectoId) {
        // ❌ YA NO PODEMOS buscar por proyecto directamente
        // Necesitaríamos obtener el idFormatoA del proyecto primero
        logger.warn("⚠️ Usar buscarPorFormatoA() en lugar de buscarPorProyecto()");
        return List.of();
    }

    @Transactional
    public FormatoAVersionResponse actualizarVersion(Long id, FormatoAVersionRequest request) {
        FormatoAVersion version = versionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Versión no encontrada"));

        version.setTitle(request.title());
        version.setMode(EnumModalidad.valueOf(request.mode()));
        version.setState(EnumEstado.valueOf(request.state()));
        version.setObservations(request.observations());
        version.setCounter(request.counter());
        // ✅ AGREGADO: Actualizar también el idFormatoA si es necesario
        if (request.IdFormatoA() != null) {
            version.setIdFormatoA(request.IdFormatoA());
        }

        FormatoAVersion actualizada = versionRepository.save(version);
        logger.info("✅ Versión {} actualizada: {}", actualizada.getNumeroVersion(), actualizada.getTitle());

        return convertirAResponse(actualizada);
    }

    private FormatoAVersionResponse convertirAResponse(FormatoAVersion version) {
        return new FormatoAVersionResponse(
                version.getId(),
                version.getNumeroVersion(),
                version.getFecha(),
                version.getTitle(),
                version.getMode().name(),
                version.getState().name(),
                version.getObservations(),
                version.getCounter(),
                version.getIdFormatoA()  // ✅ CORREGIDO: Ahora SÍ disponible
        );
    }
}