package co.unicauca.repository;

import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.*;
import co.unicauca.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PersonaService personaService;
    private final ProyectoGradoService proyectoGradoService;
    private final FormatoAVersionService formatoAVersionService;
    private final AnteproyectoService anteproyectoService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Cargando datos de prueba completos...");

        try {
            // 1. Crear estudiantes
            PersonaRequest estudiante1 = new PersonaRequest(
                    20L,
                    "Daniel",
                    "Torres",
                    "dani@unicauca.edu.co",
                    new HashSet<>(Arrays.asList("ESTUDIANTE")),
                    "Ingeniería",
                    "Ingeniería de Sistemas"
            );

            PersonaRequest estudiante2 = new PersonaRequest(
                    21L,
                    "Maria",
                    "Lopez",
                    "maria@unicauca.edu.co",
                    new HashSet<>(Arrays.asList("ESTUDIANTE")),
                    "Ingeniería",
                    "Ingeniería de Sistemas"
            );

            Persona persona1 = personaService.guardarPersona(estudiante1);
            Persona persona2 = personaService.guardarPersona(estudiante2);

            System.out.println("✅ Estudiantes creados: " + persona1.getEmail() + " (ID: " + persona1.getIdUsuario() + "), " +
                    persona2.getEmail() + " (ID: " + persona2.getIdUsuario() + ")");

            // 2. Crear FormatoA
            FormatoAVersionRequest formatoARequest1 = new FormatoAVersionRequest(
                    30L,
                    1,    // numVersion
                    LocalDate.now(), // fecha
                    "Sistema de Gestión Académica Inteligente", // titulo
                    "INVESTIGACION", // modalidad
                    "APROBADO", // estado
                    "Proyecto aprobado por el comité académico", // observaciones
                    1, // counter
                    40L // idFormatoA
            );

            var formatoAVersion1 = formatoAVersionService.crearVersion(formatoARequest1);
            System.out.println("✅ FormatoA Version 1 creado - ID: " + formatoAVersion1.getId());

            // 3. Crear Proyecto de Grado
            ProyectoGradoRequest proyectoRequest = new ProyectoGradoRequest(
                    50L,
                    "Sistema de Gestión Académica con Machine Learning", // nombre
                    LocalDate.now(), // fecha
                    Arrays.asList("dani@unicauca.edu.co", "maria@unicauca.edu.co"), // estudiantesEmail
                    40L // IdFormatoA
            );

            var proyecto = proyectoGradoService.crearProyecto(proyectoRequest);
            System.out.println("✅ Proyecto de Grado creado - ID: " + proyecto.id());

            // 4. Crear Anteproyecto
            AnteproyectoRequest anteproyectoRequest = new AnteproyectoRequest(
                    60L,
                    "Anteproyecto: Sistema de Gestión Académica Inteligente con IA", // titulo
                    LocalDate.now(), // fecha
                    "APROBADO", // estado
                    "Anteproyecto aprobado para desarrollo con machine learning", // observaciones
                    50L // idProyectoGrado
            );

            var anteproyecto = anteproyectoService.crearAnteproyecto(anteproyectoRequest);
            System.out.println("✅ Anteproyecto creado - ID: " + anteproyecto.id());

            // 5. Crear segunda versión del FormatoA (para tener historial)
            FormatoAVersionRequest formatoARequest2 = new FormatoAVersionRequest(
                    31L,
                    2,    // numVersion
                    LocalDate.now().plusDays(7), // fecha
                    "Sistema de Gestión Académica - Versión Mejorada", // titulo
                    "INVESTIGACION", // modalidad
                    "ENTREGADO", // estado
                    "En revisión por mejoras propuestas", // observaciones
                    2, // counter
                    40L // mismo idFormatoA para el historial
            );

            var formatoAVersion2 = formatoAVersionService.crearVersion(formatoARequest2);
            System.out.println("✅ FormatoA Version 2 creada - ID: " + formatoAVersion2.getId());

            System.out.println("🎉 Datos de prueba cargados exitosamente!");
            System.out.println("📊 Resumen:");
            System.out.println("   - Estudiantes: 2 (IDs: 20, 21)");
            System.out.println("   - Proyecto de Grado: 1 (ID: 50)");
            System.out.println("   - Anteproyecto: 1 (ID: 60)");
            System.out.println("   - Versiones FormatoA: 2 (IDs: 30, 31)");
            System.out.println("   - Email para probar: dani@unicauca.edu.co");

        } catch (Exception e) {
            System.err.println("❌ Error cargando datos de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}