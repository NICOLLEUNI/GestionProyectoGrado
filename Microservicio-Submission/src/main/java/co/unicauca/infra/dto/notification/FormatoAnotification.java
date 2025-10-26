package co.unicauca.infra.dto.notification;
import java.util.List;

public record FormatoAnotification(
        Long formatoAId,
        String titulo,
        List<String> estudiantesEmails, // ← Para buscar programa académico
        String directorEmail // ←
) { }
