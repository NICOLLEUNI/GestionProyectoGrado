package co.unicauca.infrastructure.adapters.output.persistence.repository;

import co.unicauca.application.ports.output.PersonaRepoOutPort;
import co.unicauca.domain.entities.EnumRol;
import co.unicauca.domain.entities.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.EnumSet;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PersonaRepoOutPort personaRepoOutPort;

    @Override
    public void run(String... args) throws Exception {

        // ✅ Personas con roles existentes - MANTENIENDO TUS EMAILS ORIGINALES
        Persona director = new Persona(
                1L,
                "Carlos Arteaga",
                "López",
                "carlos.arteaga@unicauca.edu.co",
                "ELECTRONICA",
                null,
                EnumSet.of(EnumRol.DOCENTE)
        );

        Persona codirector = new Persona(
                2L,
                "María Soto",
                "Pérez",
                "maria.soto@unicauca.edu.co",
                "SISTEMAS",
                null,
                EnumSet.of(EnumRol.DOCENTE)
        );

        Persona estudiante1 = new Persona(
                3L,
                "Nicolle",
                "Montaño",
                "nicolle.montano@unicauca.edu.co",
                "SISTEMAS",
                "INGENIERIA_DE_SISTEMAS",
                EnumSet.of(EnumRol.ESTUDIANTE)
        );

        Persona estudiante2 = new Persona(
                4L,
                "Juan",
                "Pérez",
                "juan.perez@unicauca.edu.co",
                "ELECTRONICA",
                "INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES",
                EnumSet.of(EnumRol.ESTUDIANTE)
        );

        // ✅ Jefes y coordinadores
        Persona jefeElectronica = new Persona(
                5L,
                "Ricardo",
                "Valencia",
                "ricardo.valencia@unicauca.edu.co",
                "ELECTRONICA",
                null,
                EnumSet.of(EnumRol.JEFE_DEPARTAMENTO, EnumRol.DOCENTE)
        );

        Persona jefeSistemas = new Persona(
                6L,
                "Paola",
                "Ramos",
                "paola.ramos@unicauca.edu.co",
                "SISTEMAS",
                null,
                EnumSet.of(EnumRol.JEFE_DEPARTAMENTO, EnumRol.DOCENTE)
        );

        Persona coordSistemas = new Persona(
                7L,
                "Luis",
                "Muñoz",
                "luis.munoz@unicauca.edu.co",
                "SISTEMAS",
                "INGENIERIA_DE_SISTEMAS",
                EnumSet.of(EnumRol.COORDINADOR)
        );

        Persona coordElectronica = new Persona(
                8L,
                "Andrea",
                "Gómez",
                "andrea.gomez@unicauca.edu.co",
                "ELECTRONICA",
                "INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES",
                EnumSet.of(EnumRol.COORDINADOR)
        );

        // ✅ 3 nuevos docentes
        Persona docente1 = new Persona(
                9L,
                "Jorge",
                "Hernández",
                "jorge.hernandez@unicauca.edu.co",
                "TELECOMUNICACIONES",
                null,
                EnumSet.of(EnumRol.DOCENTE)
        );

        Persona docente2 = new Persona(
                10L,
                "Ana",
                "Morales",
                "ana.morales@unicauca.edu.co",
                "TELEMATICA",
                null,
                EnumSet.of(EnumRol.DOCENTE)
        );

        Persona docente3 = new Persona(
                11L,
                "Mateo",
                "Torres",
                "mateo.torres@unicauca.edu.co",
                "AUTOMATICA_INDUSTRIAL",
                null,
                EnumSet.of(EnumRol.DOCENTE)
        );

        // ✅ 3 nuevos estudiantes
        Persona estudiante3 = new Persona(
                12L,
                "Laura",
                "Castaño",
                "laura.castano@unicauca.edu.co",
                "AUTOMATICA_INDUSTRIAL",
                "AUTOMATICA_INDUSTRIAL",
                EnumSet.of(EnumRol.ESTUDIANTE)
        );

        Persona estudiante4 = new Persona(
                13L,
                "David",
                "López",
                "david.lopez@unicauca.edu.co",
                "TELEMATICA",
                "TECNOLOGIA_EN_TELEMATICA",
                EnumSet.of(EnumRol.ESTUDIANTE)
        );

        Persona estudiante5 = new Persona(
                14L,
                "Sofía",
                "Méndez",
                "sofia.mendez@unicauca.edu.co",
                "TELECOMUNICACIONES",
                "INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES",
                EnumSet.of(EnumRol.ESTUDIANTE)
        );

        // ✅ Usuario con TODOS los roles
        Persona superUsuario = new Persona(
                15L,
                "Admin",
                "Master",
                "admin.master@unicauca.edu.co",
                "SISTEMAS",
                "INGENIERIA_DE_SISTEMAS",
                EnumSet.allOf(EnumRol.class)
        );

        // ✅ Guardar todas las personas usando el PORT
        try {
            personaRepoOutPort.save(director);
            personaRepoOutPort.save(codirector);
            personaRepoOutPort.save(estudiante1);
            personaRepoOutPort.save(estudiante2);
            personaRepoOutPort.save(jefeElectronica);
            personaRepoOutPort.save(jefeSistemas);
            personaRepoOutPort.save(coordSistemas);
            personaRepoOutPort.save(coordElectronica);
            personaRepoOutPort.save(docente1);
            personaRepoOutPort.save(docente2);
            personaRepoOutPort.save(docente3);
            personaRepoOutPort.save(estudiante3);
            personaRepoOutPort.save(estudiante4);
            personaRepoOutPort.save(estudiante5);
            personaRepoOutPort.save(superUsuario);

            System.out.println("📘 Datos iniciales cargados correctamente:");
            System.out.println("- Personas: " + personaRepoOutPort.findAll().size());
        } catch (Exception e) {
            System.out.println("⚠️ Algunas personas ya existen en la base de datos");
        }
    }
}