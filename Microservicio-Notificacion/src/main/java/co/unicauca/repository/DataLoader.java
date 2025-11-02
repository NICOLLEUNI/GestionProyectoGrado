package co.unicauca.repository;

import co.unicauca.entity.EnumRol;
import co.unicauca.entity.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.EnumSet;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PersonaRepository personaRepository;




    @Override
    public void run(String... args) throws Exception {

        // ✅ Crear Personas
        Persona director = new Persona();
        director.setIdUsuario(1L);
        director.setName("Carlos Arteaga");
        director.setLastname("López");
        director.setEmail("carlos.arteaga@unicauca.edu.co");
        director.setDepartment("Ingeniería Electrónica");
        director.setRoles(EnumSet.of(EnumRol.DOCENTE));

        Persona codirector = new Persona();
        codirector.setIdUsuario(2L);
        codirector.setName("María Soto");
        codirector.setLastname("Pérez");
        codirector.setEmail("maria.soto@unicauca.edu.co");
        codirector.setDepartment("Ingeniería de Sistemas");
        codirector.setRoles(EnumSet.of(EnumRol.DOCENTE));

        Persona estudiante1 = new Persona();
        estudiante1.setIdUsuario(3L);
        estudiante1.setName("Nicolle");
        estudiante1.setLastname("Montaño");
        estudiante1.setEmail("nicolle.montano@unicauca.edu.co");
        estudiante1.setDepartment("Ingeniería de Sistemas");
        estudiante1.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));
        estudiante1.setPrograma("Ingenieria de Sistemas");

        Persona estudiante2 = new Persona();
        estudiante2.setIdUsuario(4L);
        estudiante2.setName("Juan");
        estudiante2.setLastname("Pérez");
        estudiante2.setEmail("juan.perez@unicauca.edu.co");
        estudiante2.setDepartment("Ingeniería de Sistemas");
        estudiante2.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));
        estudiante2.setPrograma("Ingenieria de Electronica ");
        // ✅ Jefe de Departamento Ingeniería Electrónica
        Persona jefeElectronica = new Persona();
        jefeElectronica.setIdUsuario(5L);
        jefeElectronica.setName("Ricardo");
        jefeElectronica.setLastname("Valencia");
        jefeElectronica.setEmail("ricardo.valencia@unicauca.edu.co");
        jefeElectronica.setDepartment("Ingeniería Electrónica");
        jefeElectronica.setRoles(EnumSet.of(EnumRol.JEFE_DEPARTAMENTO, EnumRol.DOCENTE));

// ✅ Jefe de Departamento Ingeniería de Sistemas
        Persona jefeSistemas = new Persona();
        jefeSistemas.setIdUsuario(6L);
        jefeSistemas.setName("Paola");
        jefeSistemas.setLastname("Ramos");
        jefeSistemas.setEmail("paola.ramos@unicauca.edu.co");
        jefeSistemas.setDepartment("Ingeniería de Sistemas");
        jefeSistemas.setRoles(EnumSet.of(EnumRol.JEFE_DEPARTAMENTO, EnumRol.DOCENTE));
        // ✅ Coordinador Ingeniería de Sistemas
        Persona coordSistemas = new Persona();
        coordSistemas.setIdUsuario(7L);
        coordSistemas.setName("Luis");
        coordSistemas.setLastname("Muñoz");
        coordSistemas.setEmail("luis.munoz@unicauca.edu.co");
        coordSistemas.setDepartment("Ingenieria de Sistemas");
        coordSistemas.setPrograma("Ingenieria de Sistemas");
        coordSistemas.setRoles(EnumSet.of(EnumRol.COORDINADOR));

// ✅ Coordinador Ingeniería Electrónica
        Persona coordElectronica = new Persona();
        coordElectronica.setIdUsuario(8L);
        coordElectronica.setName("Andrea");
        coordElectronica.setLastname("Gómez");
        coordElectronica.setEmail("andrea.gomez@unicauca.edu.co");
        coordElectronica.setDepartment("Ingeniería Electrónica");
        coordElectronica.setPrograma("Ingeniería Electrónica");
        coordElectronica.setRoles(EnumSet.of(EnumRol.COORDINADOR));

        personaRepository.saveAll(Arrays.asList(director, codirector, estudiante1, estudiante2 , jefeElectronica, jefeSistemas ,coordElectronica,coordSistemas));
        // ✅ Logs
        System.out.println("📘 Datos iniciales cargados correctamente:");
        System.out.println("- Personas: " + personaRepository.count());
    }
}
