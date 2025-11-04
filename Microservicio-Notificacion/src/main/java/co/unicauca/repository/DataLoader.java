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

        // ✅ Personas con roles existentes
        Persona director = new Persona();
        director.setIdUsuario(1L);
        director.setName("Carlos Arteaga");
        director.setLastname("López");
        director.setEmail("carlos.arteaga@unicauca.edu.co");
        director.setDepartment("ELECTRONICA");
        director.setRoles(EnumSet.of(EnumRol.DOCENTE));

        Persona codirector = new Persona();
        codirector.setIdUsuario(2L);
        codirector.setName("María Soto");
        codirector.setLastname("Pérez");
        codirector.setEmail("maria.soto@unicauca.edu.co");
        codirector.setDepartment("SISTEMAS");
        codirector.setRoles(EnumSet.of(EnumRol.DOCENTE));

        Persona estudiante1 = new Persona();
        estudiante1.setIdUsuario(3L);
        estudiante1.setName("Nicolle");
        estudiante1.setLastname("Montaño");
        estudiante1.setEmail("nicolle.montano@unicauca.edu.co");
        estudiante1.setDepartment("SISTEMAS");
        estudiante1.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));
        estudiante1.setPrograma("INGENIERIA_DE_SISTEMAS");

        Persona estudiante2 = new Persona();
        estudiante2.setIdUsuario(4L);
        estudiante2.setName("Juan");
        estudiante2.setLastname("Pérez");
        estudiante2.setEmail("juan.perez@unicauca.edu.co");
        estudiante2.setDepartment("ELECTRONICA");
        estudiante2.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));
        estudiante2.setPrograma("INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES");

        // ✅ Jefes y coordinadores
        Persona jefeElectronica = new Persona();
        jefeElectronica.setIdUsuario(5L);
        jefeElectronica.setName("Ricardo");
        jefeElectronica.setLastname("Valencia");
        jefeElectronica.setEmail("ricardo.valencia@unicauca.edu.co");
        jefeElectronica.setDepartment("ELECTRONICA");
        jefeElectronica.setRoles(EnumSet.of(EnumRol.JEFE_DEPARTAMENTO, EnumRol.DOCENTE));

        Persona jefeSistemas = new Persona();
        jefeSistemas.setIdUsuario(6L);
        jefeSistemas.setName("Paola");
        jefeSistemas.setLastname("Ramos");
        jefeSistemas.setEmail("paola.ramos@unicauca.edu.co");
        jefeSistemas.setDepartment("SISTEMAS");
        jefeSistemas.setRoles(EnumSet.of(EnumRol.JEFE_DEPARTAMENTO, EnumRol.DOCENTE));

        Persona coordSistemas = new Persona();
        coordSistemas.setIdUsuario(7L);
        coordSistemas.setName("Luis");
        coordSistemas.setLastname("Muñoz");
        coordSistemas.setEmail("luis.munoz@unicauca.edu.co");
        coordSistemas.setDepartment("SISTEMAS");
        coordSistemas.setPrograma("INGENIERIA_DE_SISTEMAS");
        coordSistemas.setRoles(EnumSet.of(EnumRol.COORDINADOR));

        Persona coordElectronica = new Persona();
        coordElectronica.setIdUsuario(8L);
        coordElectronica.setName("Andrea");
        coordElectronica.setLastname("Gómez");
        coordElectronica.setEmail("andrea.gomez@unicauca.edu.co");
        coordElectronica.setDepartment("ELECTRONICA");
        coordElectronica.setPrograma("INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES");
        coordElectronica.setRoles(EnumSet.of(EnumRol.COORDINADOR));

        // ✅ 3 nuevos docentes
        Persona docente1 = new Persona();
        docente1.setIdUsuario(9L);
        docente1.setName("Jorge");
        docente1.setLastname("Hernández");
        docente1.setEmail("jorge.hernandez@unicauca.edu.co");
        docente1.setDepartment("TELECOMUNICACIONES");
        docente1.setRoles(EnumSet.of(EnumRol.DOCENTE));

        Persona docente2 = new Persona();
        docente2.setIdUsuario(10L);
        docente2.setName("Ana");
        docente2.setLastname("Morales");
        docente2.setEmail("ana.morales@unicauca.edu.co");
        docente2.setDepartment("TELEMATICA");
        docente2.setRoles(EnumSet.of(EnumRol.DOCENTE));

        Persona docente3 = new Persona();
        docente3.setIdUsuario(11L);
        docente3.setName("Mateo");
        docente3.setLastname("Torres");
        docente3.setEmail("mateo.torres@unicauca.edu.co");
        docente3.setDepartment("AUTOMATICA_INDUSTRIAL");
        docente3.setRoles(EnumSet.of(EnumRol.DOCENTE));

        // ✅ 3 nuevos estudiantes
        Persona estudiante3 = new Persona();
        estudiante3.setIdUsuario(12L);
        estudiante3.setName("Laura");
        estudiante3.setLastname("Castaño");
        estudiante3.setEmail("laura.castano@unicauca.edu.co");
        estudiante3.setDepartment("AUTOMATICA_INDUSTRIAL");
        estudiante3.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));
        estudiante3.setPrograma("AUTOMATICA_INDUSTRIAL");

        Persona estudiante4 = new Persona();
        estudiante4.setIdUsuario(13L);
        estudiante4.setName("David");
        estudiante4.setLastname("López");
        estudiante4.setEmail("david.lopez@unicauca.edu.co");
        estudiante4.setDepartment("TELEMATICA");
        estudiante4.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));
        estudiante4.setPrograma("TECNOLOGIA_EN_TELEMATICA");

        Persona estudiante5 = new Persona();
        estudiante5.setIdUsuario(14L);
        estudiante5.setName("Sofía");
        estudiante5.setLastname("Méndez");
        estudiante5.setEmail("sofia.mendez@unicauca.edu.co");
        estudiante5.setDepartment("TELECOMUNICACIONES");
        estudiante5.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));
        estudiante5.setPrograma("INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES");

        // ✅ Usuario con TODOS los roles
        Persona superUsuario = new Persona();
        superUsuario.setIdUsuario(15L);
        superUsuario.setName("Admin");
        superUsuario.setLastname("Master");
        superUsuario.setEmail("admin.master@unicauca.edu.co");
        superUsuario.setDepartment("SISTEMAS");
        superUsuario.setPrograma("INGENIERIA_DE_SISTEMAS");
        superUsuario.setRoles(EnumSet.allOf(EnumRol.class)); // ✅ Todos los roles

        personaRepository.saveAll(Arrays.asList(
                director, codirector, estudiante1, estudiante2,
                jefeElectronica, jefeSistemas, coordElectronica, coordSistemas,
                docente1, docente2, docente3,
                estudiante3, estudiante4, estudiante5,
                superUsuario
        ));

        // ✅ Logs
        System.out.println("📘 Datos iniciales cargados correctamente:");
        System.out.println("- Personas: " + personaRepository.count());
    }
}