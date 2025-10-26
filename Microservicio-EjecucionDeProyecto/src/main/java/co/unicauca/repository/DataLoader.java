package co.unicauca.repository;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.*;
import co.unicauca.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Carga datos iniciales de prueba para el microservicio de Evaluación.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProyectoGradoService proyectoGradoService;

    @Autowired
    private AnteproyectoService anteproyectoService;

    @Autowired
    private FormatoAVersionService formatoAVersionService;

    @Autowired
    private ProyectoGradoRepository proyectoGradoRepository;

    @Override
    public void run(String... args) {
        if (proyectoGradoRepository.count() > 0) {
            System.out.println("⚠️ Datos ya existentes. Se omite la carga inicial.");
            return;
        }

        System.out.println("🚀 Cargando datos iniciales...");

        try {
            // Proyecto 1 - INVESTIGACIÓN
            ProyectoGradoResponse proyecto1 = crearProyecto(
                    "Sistema Inteligente para la Gestión de Proyectos de Grado",
                    List.of("nicolle.montano@unicauca.edu.co", "juan.perez@unicauca.edu.co"),
                    100L
            );
            crearAnteproyecto(
                    proyecto1.id(),
                    "Desarrollo de Sistema Inteligente para Gestión Académica",
                    LocalDate.now().minusDays(10),
                    "ENTREGADO",
                    "Anteproyecto inicial entregado para revisión"
            );
            crearFormatoAVersion(
                    "Formato A - Sistema Inteligente de Gestión",
                    "INVESTIGACION",
                    "ENTREGADO",
                    "Versión inicial del formato A",
                    1,
                    100L  // Mismo idFormatoA que el proyecto
            );

            // Proyecto 2 - PRÁCTICA PROFESIONAL
            ProyectoGradoResponse proyecto2 = crearProyecto(
                    "Plataforma IoT para Monitoreo Ambiental en Empresa XYZ",
                    List.of("maria.garcia@unicauca.edu.co", "carlos.rodriguez@unicauca.edu.co"),
                    200L
            );
            crearAnteproyecto(
                    proyecto2.id(),
                    "Implementación de Plataforma IoT para Monitoreo de Calidad del Aire",
                    LocalDate.now().minusDays(20),
                    "APROBADO",
                    "Anteproyecto aprobado por el comité"
            );
            crearFormatoAVersion(
                    "Formato A - Plataforma IoT Ambiental",
                    "PRACTICA_PROFESIONAL",
                    "APROBADO",
                    "Formato A aprobado para desarrollo",
                    1,
                    200L  // Mismo idFormatoA que el proyecto
            );

            // Proyecto 3 - PLAN COTERMINAL
            ProyectoGradoResponse proyecto3 = crearProyecto(
                    "Aplicación Móvil para Educación Inclusiva",
                    List.of("ana.martinez@unicauca.edu.co"),
                    300L
            );
            crearFormatoAVersion(
                    "Formato A - App Móvil Educativa",
                    "PLAN_COTERMINAL",
                    "ENTREGADO",
                    "Esperando retroalimentación del comité",
                    1,
                    300L  // Mismo idFormatoA que el proyecto
            );

            // ✅ VALIDAR todas las relaciones de cada proyecto
            validarRelacionesCompletas();

            // Logs finales
            System.out.println("\n✅ Datos iniciales cargados correctamente:");
            System.out.println("🎓 Proyectos de Grado: 3");
            System.out.println("📄 Anteproyectos: 2");
            System.out.println("📋 Versiones de Formato A: 3");

        } catch (Exception e) {
            System.err.println("❌ Error cargando datos iniciales: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== Métodos auxiliares ====================

    private ProyectoGradoResponse crearProyecto(String nombre, List<String> estudiantesEmail, Long idFormatoA) {
        ProyectoGradoRequest request = new ProyectoGradoRequest(
                null,
                nombre,
                LocalDate.now(),
                estudiantesEmail,
                idFormatoA,  // ✅ idFormatoA para relacionar con versiones
                null
        );
        ProyectoGradoResponse response = proyectoGradoService.crearProyecto(request);
        System.out.println("✅ Proyecto creado: " + nombre + " (FormatoA: " + idFormatoA + ")");
        return response;
    }

    private AnteproyectoResponse crearAnteproyecto(Long proyectoId, String titulo, LocalDate fechaEntrega,
                                                   String estado, String observaciones) {
        AnteproyectoRequest request = new AnteproyectoRequest(
                null,
                titulo,
                fechaEntrega,
                estado,
                observaciones,
                proyectoId
        );
        AnteproyectoResponse response = anteproyectoService.crearAnteproyecto(request);
        System.out.println("✅ Anteproyecto creado: " + titulo + " (Proyecto: " + proyectoId + ")");
        return response;
    }

    private FormatoAVersionResponse crearFormatoAVersion(String titulo, String modalidad,
                                                         String estado, String observaciones, int version,
                                                         Long idFormatoA) {
        FormatoAVersionRequest request = new FormatoAVersionRequest(
                null,
                version,
                LocalDate.now(),
                titulo,
                modalidad,
                estado,
                observaciones,
                version,
                idFormatoA  // ✅ Solo idFormatoA (sin proyectoId)
        );
        FormatoAVersionResponse response = formatoAVersionService.crearVersion(request);
        System.out.println("✅ Versión creada: " + titulo + " (FormatoA: " + idFormatoA + ")");
        return response;
    }

    /**
     * Validar todas las relaciones de cada proyecto en formato integrado
     */
    private void validarRelacionesCompletas() {
        try {
            System.out.println("\n📊 RELACIONES ESTABLECIDAS POR PROYECTO:");
            System.out.println("===========================================");

            List<ProyectoGradoResponse> proyectos = proyectoGradoService.obtenerTodosConRelaciones();

            for (ProyectoGradoResponse proyecto : proyectos) {
                System.out.println("\n🎓 PROYECTO: " + proyecto.nombre() + " (ID: " + proyecto.id() + ")");
                System.out.println("   └─ FormatoA asociado: " + proyecto.idFormatoA());

                // Buscar versiones de FormatoA
                List<FormatoAVersionResponse> versiones = formatoAVersionService.buscarPorFormatoA(proyecto.idFormatoA());
                if (!versiones.isEmpty()) {
                    System.out.println("   └─ Versión FormatoA: '" + versiones.get(0).title() +
                            "' (v" + versiones.get(0).numVersion() + ", " + versiones.get(0).state() + ")");
                } else {
                    System.out.println("   └─ ⚠️ Sin versiones de FormatoA");
                }

                // Buscar anteproyectos
                List<AnteproyectoResponse> anteproyectos = anteproyectoService.buscarPorProyecto(proyecto.id());
                if (!anteproyectos.isEmpty()) {
                    AnteproyectoResponse anteproyecto = anteproyectos.get(0);
                    System.out.println("   └─ Anteproyecto: '" + anteproyecto.titulo() +
                            "' (" + anteproyecto.estado() + ")");
                } else {
                    System.out.println("   └─ ⚠️ Sin anteproyecto");
                }

                System.out.println("   └─ Estudiantes: " + proyecto.estudiantesEmail().size() + " estudiante(s)");
            }

        } catch (Exception e) {
            System.out.println("❌ Error validando relaciones completas: " + e.getMessage());
        }
    }
}