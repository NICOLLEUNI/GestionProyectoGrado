package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumEstadoAnteproyecto;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.infra.dto.notification.AnteproyectoNotification;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.ProyectoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
public class AnteproyectoService {

    private final AnteproyectoRepository anteproyectoRepository;
    private final ProyectoRepository proyectoRepository;
    private final RabbitMQPublisher rabbitMQPublisher; // ← AGREGAR

    public AnteproyectoService(AnteproyectoRepository anteproyectoRepository, ProyectoRepository proyectoRepository, RabbitMQPublisher rabbitMQPublisher) {
        this.anteproyectoRepository = anteproyectoRepository;
        this.proyectoRepository = proyectoRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    /**
     * Subir anteproyecto buscando por coincidencia de título con FormatoA
     */
    public Anteproyecto subirAnteproyecto(Anteproyecto anteproyecto) {
        System.out.println("🔍 BUSCANDO PROYECTO con título: " + anteproyecto.getTitulo());

        // 1. BUSCAR proyecto
        ProyectoGrado proyecto = proyectoRepository
                .findByFormatoAActualTitleAndFormatoAActualState(
                        anteproyecto.getTitulo(),
                        EnumEstado.APROBADO
                )
                .orElseThrow(() -> {
                    String error = "No se encontró proyecto con FormatoA aprobado: " + anteproyecto.getTitulo();
                    System.out.println("❌ " + error);
                    return new RuntimeException(error);
                });

        System.out.println("✅ PROYECTO ENCONTRADO: " + proyecto.getId());

        // 2. VALIDAR
        validarPuedeSubirAnteproyecto(proyecto);

        // 3. CONFIGURAR y GUARDAR anteproyecto (SIN proyectoGrado temporalmente)
        anteproyecto.setFechaCreacion(LocalDate.now());
        anteproyecto.setEstado(EnumEstadoAnteproyecto.ENTREGADO);
        // NO establecer proyectoGrado aún para evitar ciclo

        Anteproyecto anteproyectoGuardado = anteproyectoRepository.save(anteproyecto);
        System.out.println("✅ ANTEPROYECTO GUARDADO (sin relación): " + anteproyectoGuardado.getId());

        // 4. ACTUALIZAR relación en una transacción separada
        anteproyectoGuardado.setProyectoGrado(proyecto);
        proyecto.setAnteproyecto(anteproyectoGuardado);

        anteproyectoRepository.save(anteproyectoGuardado);
        proyectoRepository.save(proyecto);

        System.out.println("✅ RELACIONES ACTUALIZADAS");

        // 5. PUBLICAR EVENTOS
        try {
            AnteproyectoResponse response = convertirAAnteproyectoResponse(anteproyectoGuardado);
            rabbitMQPublisher.publicarAnteproyectoCreado(response);

            AnteproyectoNotification notificacion = convertirAAnteproyectoNotificacionEvent(anteproyectoGuardado);
            rabbitMQPublisher.publicarNotificacionAnteproyectoCreado(notificacion);

            System.out.println("✅ EVENTOS PUBLICADOS");
        } catch (Exception e) {
            System.out.println("⚠️ ERROR PUBLICANDO EVENTOS: " + e.getMessage());
        }

        return anteproyectoGuardado;
    }

    /**
     * Convierte Anteproyecto entity a AnteproyectoResponse DTO
     */
    private AnteproyectoResponse convertirAAnteproyectoResponse(Anteproyecto anteproyecto) {
        return new AnteproyectoResponse(
                anteproyecto.getId(),
                anteproyecto.getTitulo(),
                anteproyecto.getFechaCreacion(),
                anteproyecto.getEstado().name(), // ← Convertir Enum a String
                anteproyecto.getProyectoGrado().getId()
        );
    }

    private AnteproyectoNotification convertirAAnteproyectoNotificacionEvent(Anteproyecto anteproyecto) {
        return new AnteproyectoNotification(
                anteproyecto.getId(),
                anteproyecto.getTitulo(),
                anteproyecto.getProyectoGrado().getFormatoAActual().getProjectManagerEmail() // Director del proyecto
        );
    }

    private void validarPuedeSubirAnteproyecto(ProyectoGrado proyecto) {
        System.out.println("🔍 VALIDANDO PROYECTO: " + proyecto.getId());

        if (proyecto.getAnteproyecto() != null) {
            throw new RuntimeException("El proyecto ya tiene un anteproyecto asignado");
        }

        if (!"ACTIVO".equals(proyecto.getEstado())) {
            throw new RuntimeException("El proyecto debe estar ACTIVO");
        }

        // ⭐⭐ COMENTAR TEMPORALMENTE ESTA VALIDACIÓN ⭐⭐
    /*
    if (!proyecto.getFormatoAActual().getTitle().equals(proyecto.getNombre())) {
        throw new RuntimeException("El título del anteproyecto debe coincidir con el título del FormatoA aprobado");
    }
    */

        System.out.println("✅ VALIDACIONES PASADAS");
    }
    public String obtenerRutaPdf(Long idAnteproyecto) {
        Anteproyecto anteproyecto = anteproyectoRepository.findById(idAnteproyecto)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró el anteproyecto con ID: " + idAnteproyecto));

        return anteproyecto.getRutaPdf();
    }
}
