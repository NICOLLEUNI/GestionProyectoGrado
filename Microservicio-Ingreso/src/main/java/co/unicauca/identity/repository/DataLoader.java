package co.unicauca.identity.repository;

import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.EnumSet;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("Contras*123");

        Persona director = new Persona(
                "Carlos", "Arteaga López", "3001234567",
                "carlos.arteaga@unicauca.edu.co", password
        );
        director.setDepartamento(EnumDepartamento.ELECTRONICA);
        director.setRoles(EnumSet.of(enumRol.DOCENTE));

        Persona codirector = new Persona(
                "María", "Soto Pérez", "3017654321",
                "maria.soto@unicauca.edu.co", password
        );
        codirector.setDepartamento(EnumDepartamento.SISTEMAS);
        codirector.setRoles(EnumSet.of(enumRol.DOCENTE));

        Persona docente1 = new Persona(
                "Andrés", "García Vivas", "3021122334",
                "andres.garcia@unicauca.edu.co", password
        );
        docente1.setDepartamento(EnumDepartamento.TELECOMUNICACIONES);
        docente1.setRoles(EnumSet.of(enumRol.DOCENTE));

        Persona docente2 = new Persona(
                "Sandra", "Ramírez Díaz", "3032233445",
                "sandra.ramirez@unicauca.edu.co", password
        );
        docente2.setDepartamento(EnumDepartamento.TELEMATICA);
        docente2.setRoles(EnumSet.of(enumRol.DOCENTE));

        Persona docente3 = new Persona(
                "Jorge", "Hernández Becerra", "3045566778",
                "jorge.hernandez@unicauca.edu.co", password
        );
        docente3.setDepartamento(EnumDepartamento.AUTOMATICA_INDUSTRIAL);
        docente3.setRoles(EnumSet.of(enumRol.DOCENTE));

        Persona jefeDepto = new Persona(
                "Ricardo", "Valencia Torres", "3056677889",
                "ricardo.valencia@unicauca.edu.co", password
        );
        jefeDepto.setDepartamento(EnumDepartamento.ELECTRONICA);
        jefeDepto.setRoles(EnumSet.of(enumRol.JEFE_DEPARTAMENTO, enumRol.DOCENTE));

        Persona coordinador = new Persona(
                "Paola", "Ramos Castillo", "3067788990",
                "paola.ramos@unicauca.edu.co", password
        );
        coordinador.setDepartamento(EnumDepartamento.SISTEMAS);
        coordinador.setRoles(EnumSet.of(enumRol.COORDINADOR, enumRol.DOCENTE));
        coordinador.setPrograma(EnumPrograma.INGENIERIA_DE_SISTEMAS);

        Persona estudiante1 = new Persona(
                "Nicolle", "Montaño", "3078899001",
                "nicolle.montano@unicauca.edu.co", password
        );
        estudiante1.setDepartamento(EnumDepartamento.SISTEMAS);
        estudiante1.setRoles(EnumSet.of(enumRol.ESTUDIANTE));
        estudiante1.setPrograma(EnumPrograma.INGENIERIA_DE_SISTEMAS);

        Persona estudiante2 = new Persona(
                "Juan", "Pérez Ortiz", "3089900112",
                "juan.perez@unicauca.edu.co", password
        );
        estudiante2.setDepartamento(EnumDepartamento.ELECTRONICA);
        estudiante2.setRoles(EnumSet.of(enumRol.ESTUDIANTE));
        estudiante2.setPrograma(EnumPrograma.INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES);

        Persona estudiante3 = new Persona(
                "Luisa", "Fernández Gómez", "3090011223",
                "luisa.fernandez@unicauca.edu.co", password
        );
        estudiante3.setDepartamento(EnumDepartamento.TELEMATICA);
        estudiante3.setRoles(EnumSet.of(enumRol.ESTUDIANTE));
        estudiante3.setPrograma(EnumPrograma.TECNOLOGIA_EN_TELEMATICA);

        Persona superUsuario = new Persona(
                "Admin", "Master", "3101122334",
                "admin.master@unicauca.edu.co", password
        );
        superUsuario.setDepartamento(EnumDepartamento.SISTEMAS);
        superUsuario.setRoles(EnumSet.allOf(enumRol.class));
        superUsuario.setPrograma(EnumPrograma.INGENIERIA_DE_SISTEMAS);

        personaRepository.saveAll(Arrays.asList(
                director, codirector, docente1, docente2, docente3,
                jefeDepto, coordinador, estudiante1, estudiante2, estudiante3,
                superUsuario
        ));

        System.out.println("📘 Datos iniciales cargados correctamente con contraseñas encriptadas.");
    }
}