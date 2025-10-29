-- V1__create_personas_and_roles_tables.sql
-- Migración inicial para el microservicio de identidad con SINGLE_TABLE strategy
-- Basado en la entidad Persona con múltiples roles

-- =============================================
-- TABLA PRINCIPAL: personas
-- SINGLE_TABLE strategy: Una tabla para todos los tipos de persona
-- =============================================

CREATE TABLE personas (
    -- ✅ IDENTIFICACIÓN
                          id_usuario BIGSERIAL PRIMARY KEY,

    -- ✅ DATOS PERSONALES BÁSICOS (OBLIGATORIOS)
                          name VARCHAR(100) NOT NULL,
                          lastname VARCHAR(100) NOT NULL,
                          phone VARCHAR(20),
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,

    -- ✅ CAMPOS ESPECÍFICOS POR ROL (NULLABLE - condicionales)
                          programa VARCHAR(100),  -- Solo para ESTUDIANTE (puede ser null para otros roles)
                          departamento VARCHAR(50), -- Solo para DOCENTE/COORDINADOR/JEFE (null para ESTUDIANTE)

    -- ✅ AUDITORÍA Y METADATOS
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- ✅ DISCRIMINADOR PARA SINGLE_TABLE (opcional para futura extensión)
                          tipo_persona VARCHAR(50) DEFAULT 'PERSONA'
);

-- =============================================
-- TABLA DE ROLES: persona_roles
-- Relación muchos-a-muchos: Una persona puede tener múltiples roles
-- =============================================

CREATE TABLE persona_roles (
                               id_usuario BIGINT NOT NULL,
                               rol VARCHAR(30) NOT NULL,

    -- ✅ CLAVE PRIMARIA COMPUESTA
                               PRIMARY KEY (id_usuario, rol),

    -- ✅ CLAVE FORÁNEA
                               CONSTRAINT fk_persona_roles_persona
                                   FOREIGN KEY (id_usuario)
                                       REFERENCES personas(id_usuario)
                                       ON DELETE CASCADE
);

-- =============================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- =============================================

--
CREATE UNIQUE INDEX idx_personas_email_lower ON personas (LOWER(email));

-- ✅ ÍNDICE PARA BÚSQUEDAS POR NOMBRE/APELLIDO
CREATE INDEX idx_personas_nombre_apellido ON personas (name, lastname);

-- ✅ ÍNDICE PARA BÚSQUEDAS POR PROGRAMA (estudiantes)
CREATE INDEX idx_personas_programa ON personas (programa) WHERE programa IS NOT NULL;

-- ✅ ÍNDICE PARA BÚSQUEDAS POR DEPARTAMENTO (docentes/coordinadores/jefes)
CREATE INDEX idx_personas_departamento ON personas (departamento) WHERE departamento IS NOT NULL;

-- ✅ ÍNDICE PARA BÚSQUEDAS POR ROL (optimiza joins con persona_roles)
CREATE INDEX idx_persona_roles_rol ON persona_roles (rol);

-- ✅ ÍNDICE PARA BÚSQUEDAS POR FECHA
CREATE INDEX idx_personas_created_at ON personas (created_at);

-- =============================================
-- CONSTRAINTS DE VALIDACIÓN
-- =============================================

-- ✅ CONSTRAINT PARA EMAIL INSTITUCIONAL
-- Asegura que los emails sean del dominio @unicauca.edu.co
ALTER TABLE personas
    ADD CONSTRAINT chk_personas_email_institucional
        CHECK (email LIKE '%@unicauca.edu.co');

-- ✅ CONSTRAINT PARA VALORES VÁLIDOS DE PROGRAMA
ALTER TABLE personas
    ADD CONSTRAINT chk_personas_programa_valido
        CHECK (programa IN (
                            'INGENIERIA_DE_SISTEMAS',
                            'INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES',
                            'AUTOMATICA_INDUSTRIAL',
                            'TECNOLOGIA_EN_TELEMATICA'
            ) OR programa IS NULL);

-- ✅ CONSTRAINT PARA VALORES VÁLIDOS DE DEPARTAMENTO
ALTER TABLE personas
    ADD CONSTRAINT chk_personas_departamento_valido
        CHECK (departamento IN (
                                'SISTEMAS',
                                'ELECTRONICA',
                                'TELECOMUNICACIONES',
                                'TELEMATICA',
                                'AUTOMATICA_INDUSTRIAL'
            ) OR departamento IS NULL);

-- ✅ CONSTRAINT PARA VALORES VÁLIDOS DE ROL
ALTER TABLE persona_roles
    ADD CONSTRAINT chk_persona_roles_rol_valido
        CHECK (rol IN (
                       'ESTUDIANTE',
                       'DOCENTE',
                       'COORDINADOR',
                       'JEFE_DEPARTAMENTO'
            ));

-- =============================================
-- TRIGGERS PARA ACTUALIZACIÓN AUTOMÁTICA
-- =============================================

-- ✅ TRIGGER PARA ACTUALIZAR updated_at AUTOMÁTICAMENTE
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_personas_updated_at
    BEFORE UPDATE ON personas
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =============================================
-- VISTAS PARA CONSULTAS FRECUENTES
-- =============================================

-- ✅ VISTA PARA ESTUDIANTES (roles + programa)
CREATE OR REPLACE VIEW vista_estudiantes AS
SELECT
    p.id_usuario,
    p.name,
    p.lastname,
    p.email,
    p.phone,
    p.programa,
    p.created_at
FROM personas p
         INNER JOIN persona_roles pr ON p.id_usuario = pr.id_usuario
WHERE pr.rol = 'ESTUDIANTE'
  AND p.programa IS NOT NULL;

-- ✅ VISTA PARA DOCENTES/COORDINADORES/JEFES (roles + departamento)
CREATE OR REPLACE VIEW vista_personal_academico AS
SELECT
    p.id_usuario,
    p.name,
    p.lastname,
    p.email,
    p.phone,
    p.departamento,
    pr.rol,
    p.created_at
FROM personas p
         INNER JOIN persona_roles pr ON p.id_usuario = pr.id_usuario
WHERE pr.rol IN ('DOCENTE', 'COORDINADOR', 'JEFE_DEPARTAMENTO')
  AND p.departamento IS NOT NULL;

-- =============================================
-- COMENTARIOS PARA DOCUMENTACIÓN
-- =============================================

COMMENT ON TABLE personas IS 'Tabla principal de usuarios del sistema - SINGLE_TABLE strategy: almacena estudiantes, docentes, coordinadores y jefes de departamento';
COMMENT ON TABLE persona_roles IS 'Tabla de relación muchos-a-muchos entre personas y roles - permite múltiples roles por usuario';

COMMENT ON COLUMN personas.id_usuario IS 'Identificador único autoincremental de la persona';
COMMENT ON COLUMN personas.name IS 'Nombres de la persona (obligatorio)';
COMMENT ON COLUMN personas.lastname IS 'Apellidos de la persona (obligatorio)';
COMMENT ON COLUMN personas.phone IS 'Número de celular (10 dígitos, opcional)';
COMMENT ON COLUMN personas.email IS 'Email institucional @unicauca.edu.co (único, obligatorio)';
COMMENT ON COLUMN personas.password IS 'Contraseña hasheada (obligatorio)';
COMMENT ON COLUMN personas.programa IS 'Programa académico - solo para estudiantes (condicional)';
COMMENT ON COLUMN personas.departamento IS 'Departamento académico - solo para docentes/coordinadores/jefes (condicional)';
COMMENT ON COLUMN personas.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN personas.updated_at IS 'Fecha y hora de última actualización del registro';
COMMENT ON COLUMN personas.tipo_persona IS 'Discriminador para herencia SINGLE_TABLE (futura extensión)';

COMMENT ON COLUMN persona_roles.id_usuario IS 'Referencia a la persona (clave foránea)';
COMMENT ON COLUMN persona_roles.rol IS 'Rol asignado a la persona (ESTUDIANTE, DOCENTE, COORDINADOR, JEFE_DEPARTAMENTO)';

-- =============================================
-- DATOS INICIALES (OPCIONAL)
-- =============================================

-- ✅ INSERTAR ROLES BÁSICOS (para referencia)
-- Nota: Los roles ya están definidos como ENUM en la aplicación,
-- pero podemos insertar datos de prueba si es necesario

-- Ejemplo de usuario administrador inicial (opcional)
-- INSERT INTO personas (name, lastname, email, password, departamento, tipo_persona)
-- VALUES (
--     'Admin',
--     'Sistema',
--     'admin@unicauca.edu.co',
--     '$2a$10$hashed_password_here', -- Reemplazar con hash real
--     'SISTEMAS',
--     'ADMIN'
-- );

-- INSERT INTO persona_roles (id_usuario, rol)
-- VALUES (1, 'DOCENTE'), (1, 'COORDINADOR');

COMMENT ON CONSTRAINT chk_personas_email_institucional ON personas IS 'Valida que el email sea institucional (@unicauca.edu.co)';
COMMENT ON CONSTRAINT chk_personas_programa_valido ON personas IS 'Valida que el programa sea uno de los valores permitidos';
COMMENT ON CONSTRAINT chk_personas_departamento_valido ON personas IS 'Valida que el departamento sea uno de los valores permitidos';
COMMENT ON CONSTRAINT chk_persona_roles_rol_valido ON personas IS 'Valida que el rol sea uno de los valores permitidos';