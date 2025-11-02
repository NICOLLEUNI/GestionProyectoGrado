package co.unicauca.identity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuración de base de datos y JPA
 * SINGLE_TABLE: Optimizado para el modelo actual con auditoría
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {

    // La anotación @EnableJpaAuditing habilita automáticamente:
    // - @CreatedDate
    // - @LastModifiedDate
    // - @CreatedBy
    // - @LastModifiedBy

    // Esta configuración es suficiente para el modelo SINGLE_TABLE
    // JPA detectará automáticamente las entidades y repositorios
}