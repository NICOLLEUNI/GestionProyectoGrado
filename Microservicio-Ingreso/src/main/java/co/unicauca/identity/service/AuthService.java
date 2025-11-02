package co.unicauca.identity.service;

import co.unicauca.identity.dto.request.LoginRequest;
import co.unicauca.identity.dto.request.RegisterRequest;
import co.unicauca.identity.dto.response.LoginResponse;
import co.unicauca.identity.dto.response.PersonaResponse;
import co.unicauca.identity.dto.response.RolesResponse;
import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import org.springframework.data.domain.Page;

/**
 * Interfaz para servicios de autenticación y gestión de identidad
 * Optimizada para SINGLE_TABLE strategy
 */
public interface AuthService {

    /**
     * Registra un nuevo usuario en el sistema con múltiples roles
     * SINGLE_TABLE: Crea una única entidad Persona con todos los campos
     */
    PersonaResponse register(RegisterRequest request);

    /**
     * Autentica un usuario y genera token JWT
     * SINGLE_TABLE: Búsqueda simple en una tabla
     */
    LoginResponse login(LoginRequest request);

    /**
     * Obtiene el perfil del usuario autenticado
     */
    PersonaResponse getProfile(Long userId);

    /**
     * Obtiene roles, programas y departamentos disponibles
     */
    //RolesResponse getAvailableRolesAndPrograms();
    RolesResponse getAvailableRolesAndPrograms();
    /**
     * Obtiene ID de usuario por email
     * SINGLE_TABLE: Consulta directa sin joins complejos
     */
    Long getUserIdByEmail(String email);

    /**
     * Busca usuarios con criterios específicos
     * SINGLE_TABLE: Consultas simplificadas en una tabla
     */
    Page<PersonaResponse> searchUsers(String query, enumRol rol, EnumPrograma programa,
                                      EnumDepartamento departamento, int page, int size);

    /**
     * Verifica si un email ya está registrado
     * SINGLE_TABLE: Verificación nativa con unique constraint
     */
    boolean isEmailRegistered(String email);

    /**
     * Convierte entidad Persona a DTO PersonaResponse
     * SINGLE_TABLE: Mapeo directo sin herencia compleja
     */
    PersonaResponse mapPersonaToResponse(co.unicauca.identity.entity.Persona persona);
}