package co.unicauca.service;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.dto.FormatoAEditRequest;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.infra.dto.notification.VersionNotification;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.FormatoVersionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VersionService {

    private final FormatoVersionRepository formatoVersionRepository;
    private final RabbitMQPublisher rabbitMQPublisher;

    public VersionService(FormatoVersionRepository formatoVersionRepository, RabbitMQPublisher rabbitMQPublisher) {
        this.formatoVersionRepository = formatoVersionRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    public FormatoAVersion crearVersionInicial(FormatoA formatoA) {
        FormatoAVersion version1 = new FormatoAVersion();
        version1.setNumeroVersion(1);
        version1.setFecha(LocalDate.now());
        version1.setTitle(formatoA.getTitle());
        version1.setMode(formatoA.getMode());
        version1.setGeneralObjetive(formatoA.getGeneralObjetive());
        version1.setSpecificObjetives(formatoA.getSpecificObjetives());
        version1.setArchivoPDF(formatoA.getArchivoPDF());
        version1.setCartaLaboral(formatoA.getCartaLaboral());
        version1.setState(EnumEstado.ENTREGADO);
        version1.setObservations(null);
        version1.setCounter(0);
        version1.setFormatoA(formatoA);

        FormatoAVersion versionGuardada = formatoVersionRepository.save(version1);

        // ⭐⭐ PUBLICAR VERSIÓN INICIAL ⭐⭐
        FormatoAVersionResponse response = convertirAVersionResponse(versionGuardada);
        rabbitMQPublisher.publicarVersionCreada(response);

        // 2. Para notificaciones (solo correos)
        VersionNotification notificacion = convertirAVersionNotificacionEvent(versionGuardada);
        rabbitMQPublisher.publicarNotificacionVersionCreada(notificacion);

        return versionGuardada;
    }

    /**
     * Actualiza los campos de archivoPDF o cartaLaboral de la última versión del FormatoA.
     */
    public void actualizarRutasArchivos(FormatoA formatoA) {
        try {
            // Buscar la última versión asociada a este FormatoA
            FormatoAVersion ultimaVersion = formatoVersionRepository.findTopByFormatoAOrderByNumeroVersionDesc(formatoA);
            if (ultimaVersion == null) {
                System.err.println("⚠️ No se encontró versión para el FormatoA con ID: " + formatoA.getId());
                return;
            }

            // Actualizar las rutas
            ultimaVersion.setArchivoPDF(formatoA.getArchivoPDF());
            ultimaVersion.setCartaLaboral(formatoA.getCartaLaboral());

            // Guardar cambios
            formatoVersionRepository.save(ultimaVersion);

            System.out.println("✅ Versión actualizada con nuevas rutas de archivos para FormatoA ID " + formatoA.getId());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error al actualizar rutas en versión de FormatoA ID: " + formatoA.getId());
        }
    }

    public FormatoAVersion crearVersionConEvaluacion(FormatoA formatoAActualizado, FormatoARequest request) {
        FormatoAVersion version = new FormatoAVersion();
        version.setNumeroVersion(obtenerProximaVersion(formatoAActualizado));
        version.setFecha(java.time.LocalDate.now());

        //Copia los datos ACTUALIZADOS del FormatoA
        version.setTitle(formatoAActualizado.getTitle());
        version.setMode(formatoAActualizado.getMode());
        version.setGeneralObjetive(formatoAActualizado.getGeneralObjetive());
        version.setSpecificObjetives(formatoAActualizado.getSpecificObjetives());
        version.setArchivoPDF(formatoAActualizado.getArchivoPDF());
        version.setCartaLaboral(formatoAActualizado.getCartaLaboral());
        version.setState(formatoAActualizado.getState()); // Estado ACTUALIZADO
        version.setObservations(formatoAActualizado.getObservations()); //Observaciones ACTUALIZADAS
        version.setCounter(formatoAActualizado.getCounter()); //Counter ACTUALIZADO
        version.setFormatoA(formatoAActualizado);

        FormatoAVersion versionGuardada = formatoVersionRepository.save(version);

        // ⭐⭐ PUBLICAR NUEVA VERSIÓN ⭐⭐
        FormatoAVersionResponse response = convertirAVersionResponse(versionGuardada);
        rabbitMQPublisher.publicarVersionCreada(response);

        VersionNotification notificacion = convertirAVersionNotificacionEvent(versionGuardada);
        rabbitMQPublisher.publicarNotificacionVersionCreada(notificacion);

        return versionGuardada;
    }

    public FormatoAVersion crearVersionReenviada(FormatoA formatoA, FormatoAEditRequest request) {
        FormatoAVersion version = new FormatoAVersion();
        version.setNumeroVersion(obtenerProximaVersion(formatoA));
        version.setFecha(LocalDate.now());
        version.setFormatoA(formatoA);
        version.setArchivoPDF(request.archivoPDF());
        version.setCartaLaboral(request.cartaLaboral());
        version.setGeneralObjetive(request.generalObjetive());
        version.setSpecificObjetives(request.specificObjetives());
        // ✅ Copiamos la modalidad (evita el NullPointer)
        version.setMode(formatoA.getMode());
        version.setState(EnumEstado.ENTREGADO);
        version.setObservations("Versión reenviada tras correcciones por el docente.");
        version.setCounter(formatoA.getCounter()); // mantener coherencia de contador

        FormatoAVersion versionGuardada = formatoVersionRepository.save(version);

        // ⭐⭐ PUBLICAR NUEVA VERSIÓN REENVIADA ⭐⭐
        FormatoAVersionResponse response = convertirAVersionResponse(versionGuardada);
        rabbitMQPublisher.publicarVersionCreada(response);

        VersionNotification notificacion = convertirAVersionNotificacionEvent(versionGuardada);
        rabbitMQPublisher.publicarNotificacionVersionCreada(notificacion);

        return versionGuardada;
    }


    /**
     * Convierte FormatoAVersion entity a FormatoAVersionResponse DTO
     */
    private FormatoAVersionResponse convertirAVersionResponse(FormatoAVersion version) {
        return new FormatoAVersionResponse(
                version.getId(),
                version.getNumeroVersion(),
                version.getFecha(),
                version.getTitle(),
                version.getMode().name(), // ← Convertir Enum a String
                version.getState().name(), // ← Convertir Enum a String
                version.getObservations(),
                version.getCounter(),
                version.getFormatoA().getId()
        );
    }

    private VersionNotification convertirAVersionNotificacionEvent(FormatoAVersion version) {
        // Usar la relación para obtener el FormatoA padre
        FormatoA formatoA = version.getFormatoA();

        return new VersionNotification(
                version.getId(),
                formatoA.getId(),
                version.getNumeroVersion(),
                version.getState().name(),
                formatoA.getEstudianteEmails(), // ← Del FormatoA relacionado
                formatoA.getProjectManagerEmail() // ← Del FormatoA relacionado
        );
    }

    public int obtenerProximaVersion(FormatoA formatoA) {
        // Buscar la versión más alta para este FormatoA y sumar 1
        Integer maxVersion = formatoVersionRepository.findMaxVersionByFormatoAId(formatoA.getId());
        return (maxVersion != null ? maxVersion : 0) + 1;
    }
    @Transactional
    public void eliminarVersionesPorFormatoA(Long idFormatoA) {
        formatoVersionRepository.deleteByFormatoAId(idFormatoA);
    }
}
