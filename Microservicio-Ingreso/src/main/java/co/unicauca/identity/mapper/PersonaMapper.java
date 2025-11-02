package co.unicauca.identity.mapper;

import co.unicauca.identity.dto.request.RegisterRequest;
import co.unicauca.identity.dto.response.LoginResponse;
import co.unicauca.identity.dto.response.PersonaResponse;
import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.enumRol;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidad Persona y DTOs
 * SINGLE_TABLE Strategy - Mapeo simplificado
 *
 * Patrón Mapper con MapStruct para conversiones automáticas
 */
@Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonaMapper {

    PersonaMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(PersonaMapper.class);

    /**
     * Mapeo corregido para la nueva estructura de PersonaResponse
     */
    @Mapping(target = "id", source = "idUsuario")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStringSet")
    @Mapping(target = "department", source = "departamento", qualifiedByName = "enumToString")
    @Mapping(target = "programa", source = "programa", qualifiedByName = "enumToString")
    PersonaResponse toResponse(Persona persona);

    // Métodos de conversión personalizados actualizados
    @Named("rolesToStringSet")
    static Set<String> rolesToStringSet(Set<enumRol> roles) {
        return roles != null ?
                roles.stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
                : null;
    }

    @Named("enumToString")
    static String enumToString(Enum<?> enumValue) {
        return enumValue != null ? enumValue.name() : null;
    }

    // Método de utilidad para usar el factory method de PersonaResponse
    default PersonaResponse toResponseWithConditionalLogic(Persona persona) {
        if (persona == null) return null;

        Set<String> rolesString = persona.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        String departament = persona.getDepartamento() != null ? persona.getDepartamento().name() : null;
        String programa = persona.getPrograma() != null ? persona.getPrograma().name() : null;

        return PersonaResponse.create(
                persona.getIdUsuario(),
                persona.getName(),
                persona.getLastname(),
                persona.getEmail(),
                rolesString,
                departament,
                programa
        );
    }
}