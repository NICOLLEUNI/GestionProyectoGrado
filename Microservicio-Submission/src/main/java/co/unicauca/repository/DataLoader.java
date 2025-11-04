

package co.unicauca.repository;


import co.unicauca.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

/**
 * Carga datos iniciales de prueba para el microservicio Submission.
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
        Persona director = new Persona();
        director.setIdUsuario(1L);
        director.setName("Carlos Arteaga");
        director.setLastname("López");
        director.setEmail("carlos.arteaga@unicauca.edu.co");
        director.setDepartment("Ingeniería Electrónica");
        director.setPrograma("Ingeniería Electrónica");
        director.setRoles(Set.of(EnumRol.DOCENTE));

        Persona codirector = new Persona();
        codirector.setIdUsuario(2L);
        codirector.setName("María Soto");
        codirector.setLastname("Pérez");
        codirector.setEmail("maria.soto@unicauca.edu.co");
        codirector.setDepartment("Ingeniería de Sistemas");
        codirector.setPrograma("Ingeniería de Sistemas");
        codirector.setRoles(Set.of(EnumRol.DOCENTE));

        Persona estudiante1 = new Persona();
        estudiante1.setIdUsuario(3L);
        estudiante1.setName("Nicolle");
        estudiante1.setLastname("Montaño");
        estudiante1.setEmail("nicolle.montano@unicauca.edu.co");
        estudiante1.setDepartment("Ingeniería de Sistemas");
        estudiante1.setPrograma("Ingeniería de Sistemas");
        estudiante1.setRoles(Set.of(EnumRol.ESTUDIANTE));

        Persona estudiante2 = new Persona();
        estudiante2.setIdUsuario(4L);
        estudiante2.setName("Juan");
        estudiante2.setLastname("Pérez");
        estudiante2.setEmail("juan.perez@unicauca.edu.co");
        estudiante2.setDepartment("Ingeniería de Sistemas");
        estudiante2.setPrograma("Ingeniería de Sistemas");
        estudiante2.setRoles(Set.of(EnumRol.ESTUDIANTE));

        personaRepository.saveAll(Arrays.asList(director, codirector, estudiante1, estudiante2));

        // ✅ Logs
        System.out.println("📘 Datos iniciales cargados correctamente:");
        System.out.println("- Personas: " + personaRepository.count());
        System.out.println("- Anteproyectos: " + anteproyectoRepository.count());
        System.out.println("- Formatos A: " + formatoARepository.count());
    }
}
