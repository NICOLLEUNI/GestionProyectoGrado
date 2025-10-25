package co.unicauca.repository;


import co.unicauca.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Carga datos iniciales de prueba para el microservicio Evaluación.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private AnteproyectoRepository anteproyectoRepository;

    @Autowired
    private FormatoARepository formatoARepository;

    @Override
    public void run(String... args) throws Exception {

        // ✅ Crear Personas
        PersonaEntity director = new PersonaEntity();

        director.setName("Carlos Arteaga");
        director.setLastname("López");
        director.setEmail("carlos.arteaga@unicauca.edu.co");
        director.setDepartment("Ingeniería Electrónica");
        director.setRoles(EnumSet.of(EnumRol.DOCENTE));

        PersonaEntity codirector = new PersonaEntity();

        codirector.setName("María Soto");
        codirector.setLastname("Pérez");
        codirector.setEmail("maria.soto@unicauca.edu.co");
        codirector.setDepartment("Ingeniería de Sistemas");
        codirector.setRoles(EnumSet.of(EnumRol.DOCENTE));

        PersonaEntity estudiante1 = new PersonaEntity();

        estudiante1.setName("Nicolle");
        estudiante1.setLastname("Montaño");
        estudiante1.setEmail("nicolle.montano@unicauca.edu.co");
        estudiante1.setDepartment("Ingeniería de Sistemas");
        estudiante1.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));

        PersonaEntity estudiante2 = new PersonaEntity();

        estudiante2.setName("Juan");
        estudiante2.setLastname("Pérez");
        estudiante2.setEmail("juan.perez@unicauca.edu.co");
        estudiante2.setDepartment("Ingeniería de Sistemas");
        estudiante2.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));

        personaRepository.saveAll(Arrays.asList(director, codirector, estudiante1, estudiante2));

        // ✅ Crear un Anteproyecto
        AnteproyectoEntity anteproyecto = new AnteproyectoEntity();
        anteproyecto.setTitulo("Sistema Inteligente para la Gestión de Proyectos de Grado");
        anteproyecto.setEstado("EN_REVISION");
        anteproyecto.setObservaciones("Pendiente de revisión por el coordinador");
        anteproyecto.setFechaCreacion(LocalDate.now());
        anteproyectoRepository.save(anteproyecto);

        // ✅ Crear un FormatoA asociado
        FormatoAEntity formatoA = new FormatoAEntity();

        formatoA.setTitle("Formato A - Sistema Inteligente");
        formatoA.setMode("Presencial");
        formatoA.setProjectManager(director);
        formatoA.setProjectCoManager(codirector);
        formatoA.setGeneralObjetive("Desarrollar un sistema inteligente que optimice la gestión de proyectos de grado.");
        formatoA.setSpecificObjetives("1. Implementar IA.\n2. Diseñar base de datos.\n3. Crear API REST.");
        formatoA.setArchivoPDF("formatoA1.pdf");
        formatoA.setCartaLaboral("carta_laboral_1.pdf");
        formatoA.setEstudiantes(List.of(estudiante1, estudiante2));
        formatoA.setCounter(2);
        formatoA.setState(EnumEstado.ENTREGADO);


        formatoARepository.save(formatoA);

        // ✅ Logs
        System.out.println("📘 Datos iniciales cargados correctamente:");
        System.out.println("- Personas: " + personaRepository.count());
        System.out.println("- Anteproyectos: " + anteproyectoRepository.count());
        System.out.println("- Formatos A: " + formatoARepository.count());
    }
}