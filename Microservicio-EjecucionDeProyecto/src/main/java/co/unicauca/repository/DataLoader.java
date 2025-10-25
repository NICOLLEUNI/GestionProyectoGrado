package co.unicauca.repository;

import co.unicauca.entity.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class DataLoader {

    private final FormatoARepository formatoARepository;
    private final ProyectoGradoRepository proyectoGradoRepository;
    private final PersonaRepository personaRepository;

    public DataLoader(FormatoARepository formatoARepository,
                      ProyectoGradoRepository proyectoGradoRepository,
                      PersonaRepository personaRepository) {
        this.formatoARepository = formatoARepository;
        this.proyectoGradoRepository = proyectoGradoRepository;
        this.personaRepository = personaRepository;
    }

    @Bean
    @Transactional
    public CommandLineRunner loadData() {
        return args -> {
            // 1. Guardar Personas PRIMERO
            PersonaEntity estudiante1 = new PersonaEntity(
                    "juan.perez@unicauca.edu.co",
                    "Juan Pérez",
                    "juan.perez@unicauca.edu.co",
                    new HashSet<>(Arrays.asList("ESTUDIANTE")),
                    "Ingeniería de Sistemas"
            );

            PersonaEntity estudiante2 = new PersonaEntity(
                    "maria.lopez@unicauca.edu.co",
                    "María López",
                    "maria.lopez@unicauca.edu.co",
                    new HashSet<>(Arrays.asList("ESTUDIANTE")),
                    "Ingeniería de Sistemas"
            );

            PersonaEntity projectManager = new PersonaEntity(
                    "director.proyecto@unicauca.edu.co",
                    "Dr. Carlos Rodríguez",
                    "director.proyecto@unicauca.edu.co",
                    new HashSet<>(Arrays.asList("DIRECTOR")),
                    "Ingeniería de Sistemas"
            );

            // Guardar todas las personas ANTES de usarlas en otras entidades
            personaRepository.saveAll(Arrays.asList(estudiante1, estudiante2, projectManager));

            System.out.println("Personas guardadas exitosamente.");

            // 2. Crear Proyecto Grado SIN formatoA (primero)
            ProyectoGradoEntity proyectoGrado = new ProyectoGradoEntity();
            proyectoGrado.setTitulo("Proyecto de Inteligencia Artificial");
            proyectoGrado.setFechaCreacion(LocalDateTime.now()); // ← AGREGAR FECHA
            proyectoGrado.setEstado("ACTIVO");
            proyectoGrado.setEstudiantesEmail(Arrays.asList(
                    estudiante1.getEmail(),
                    estudiante2.getEmail()
            ));
            // NO asignar formatoAActual todavía
            proyectoGrado = proyectoGradoRepository.save(proyectoGrado);

            System.out.println("Proyecto de Grado guardado exitosamente con ID: " + proyectoGrado.getId());

            // 3. Crear y guardar Formato A
            FormatoAEntity formatoA = new FormatoAEntity();
            formatoA.setTitle("Formato A de Proyecto Grado");
            formatoA.setMode(EnumModalidad.PRACTICA_PROFESIONAL);
            formatoA.setState(EnumEstado.ENTREGADO);
            formatoA.setProjectManagerEmail(projectManager.getEmail());
            formatoA.setGeneralObjetive("Desarrollar un sistema de inteligencia artificial para análisis predictivo");
            formatoA.setSpecificObjetives("1. Implementar algoritmos de Machine Learning\n2. Crear interfaz de usuario intuitiva\n3. Realizar pruebas de rendimiento");

            // Asignar estudiantes que YA están persistidos
            formatoA.setEstudiantes(Arrays.asList(estudiante1, estudiante2));

            // Guardar formatoA
            formatoA = formatoARepository.save(formatoA);

            System.out.println("Formato A guardado exitosamente con ID: " + formatoA.getId());

            // 4. Actualizar el proyecto con el formatoA YA PERSISTIDO
            proyectoGrado.setFormatoAActual(formatoA);
            proyectoGradoRepository.save(proyectoGrado);

            System.out.println("Proyecto actualizado con Formato A.");
            System.out.println("===========================================");
            System.out.println("Datos de ejemplo cargados exitosamente.");
            System.out.println("===========================================");
        };
    }
}