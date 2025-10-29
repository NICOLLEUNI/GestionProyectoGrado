package co.unicauca.identity.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * Utilidad especializada para operaciones con contraseñas
 * SINGLE_TABLE: Optimizado para el modelo actual
 */
@Component
@Slf4j
public class PasswordUtil {

    private static final String STRONG_PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$";
    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(STRONG_PASSWORD_REGEX);

    private final BCryptPasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom;

    public PasswordUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder(12); // Strength 12 para máxima seguridad
        this.secureRandom = new SecureRandom();
    }

    /**
     * Hashea una contraseña en texto plano usando BCrypt
     * SINGLE_TABLE: Compatible con el modelo actual
     */
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        try {
            String hashedPassword = passwordEncoder.encode(plainPassword);
            log.debug("Contraseña hasheada exitosamente");
            return hashedPassword;
        } catch (Exception e) {
            log.error("Error al hashear contraseña: {}", e.getMessage());
            throw new RuntimeException("Error al procesar la contraseña", e);
        }
    }

    /**
     * Verifica si una contraseña en texto plano coincide con el hash almacenado
     */
    public boolean matches(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        try {
            boolean matches = passwordEncoder.matches(plainPassword, hashedPassword);
            if (!matches) {
                log.warn("Intento de autenticación con contraseña incorrecta");
            }
            return matches;
        } catch (Exception e) {
            log.error("Error al verificar contraseña: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Valida que una contraseña cumpla con los requisitos de seguridad
     * SINGLE_TABLE: Mismos requisitos que las validaciones anteriores
     */
    public boolean isStrongPassword(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }

        return STRONG_PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Genera una contraseña temporal segura
     * Útil para reset de contraseñas o usuarios temporales
     */
    public String generateTemporaryPassword() {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String special = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        String allChars = uppercase + lowercase + numbers + special;

        StringBuilder password = new StringBuilder(12);

        // Asegurar al menos un carácter de cada tipo
        password.append(uppercase.charAt(secureRandom.nextInt(uppercase.length())));
        password.append(lowercase.charAt(secureRandom.nextInt(lowercase.length())));
        password.append(numbers.charAt(secureRandom.nextInt(numbers.length())));
        password.append(special.charAt(secureRandom.nextInt(special.length())));

        // Completar con caracteres aleatorios
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(secureRandom.nextInt(allChars.length())));
        }

        // Mezclar los caracteres
        return shuffleString(password.toString());
    }

    /**
     * Mezcla los caracteres de una cadena para mayor aleatoriedad
     */
    private String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = secureRandom.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }

    /**
     * Obtiene información sobre la fortaleza de la contraseña
     */
    public PasswordStrength getPasswordStrength(String password) {
        if (password == null || password.isBlank()) {
            return PasswordStrength.EMPTY;
        }

        int score = 0;

        // Longitud mínima
        if (password.length() >= 6) score++;
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;

        // Caracteres diversos
        if (password.matches(".*[A-Z].*")) score++; // Mayúscula
        if (password.matches(".*[a-z].*")) score++; // Minúscula
        if (password.matches(".*\\d.*")) score++;   // Número
        if (password.matches(".*[^A-Za-z0-9].*")) score++; // Especial

        if (score >= 6) return PasswordStrength.VERY_STRONG;
        if (score >= 5) return PasswordStrength.STRONG;
        if (score >= 4) return PasswordStrength.MEDIUM;
        if (score >= 3) return PasswordStrength.WEAK;
        return PasswordStrength.VERY_WEAK;
    }

    /**
     * Enum para niveles de fortaleza de contraseña
     */
    public enum PasswordStrength {
        EMPTY("Vacía", 0),
        VERY_WEAK("Muy débil", 1),
        WEAK("Débil", 2),
        MEDIUM("Media", 3),
        STRONG("Fuerte", 4),
        VERY_STRONG("Muy fuerte", 5);

        private final String description;
        private final int level;

        PasswordStrength(String description, int level) {
            this.description = description;
            this.level = level;
        }

        public String getDescription() { return description; }
        public int getLevel() { return level; }
    }

    /**
     * Verifica si un hash BCrypt es válido
     */
    public boolean isValidBcryptHash(String hash) {
        if (hash == null || hash.length() != 60) {
            return false;
        }

        try {
            // BCrypt hash siempre comienza con $2a$, $2b$, $2x$ o $2y$
            return hash.startsWith("$2") && BCrypt.checkpw("test", hash.replace("test", "dummy"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Método de utilidad para logging seguro (no loguear contraseñas reales)
     */
    public String secureLog(String password) {
        if (password == null) return "null";
        if (password.length() <= 2) return "***";
        return password.substring(0, 1) + "***" + password.substring(password.length() - 1);
    }
}