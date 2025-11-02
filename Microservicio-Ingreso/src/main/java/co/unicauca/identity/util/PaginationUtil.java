package co.unicauca.identity.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para manejar la paginación en las respuestas de la API
 */
public class PaginationUtil {

    private PaginationUtil() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Genera encabezados HTTP para información de paginación
     */
    public static HttpHeaders generatePaginationHeaders(Page<?> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        headers.add("X-Total-Pages", Integer.toString(page.getTotalPages()));
        headers.add("X-Page-Number", Integer.toString(page.getNumber()));
        headers.add("X-Page-Size", Integer.toString(page.getSize()));
        headers.add("X-Has-Next", Boolean.toString(page.hasNext()));
        headers.add("X-Has-Previous", Boolean.toString(page.hasPrevious()));
        return headers;
    }

    /**
     * Crea un ResponseEntity con datos paginados y encabezados
     */
    public static <T> ResponseEntity<Map<String, Object>> createPaginatedResponse(Page<T> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", page.getContent());
        response.put("pagination", createPaginationInfo(page));

        return ResponseEntity.ok()
                .headers(generatePaginationHeaders(page))
                .body(response);
    }

    /**
     * Crea información de paginación para la respuesta
     */
    public static Map<String, Object> createPaginationInfo(Page<?> page) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("totalItems", page.getTotalElements());
        pagination.put("totalPages", page.getTotalPages());
        pagination.put("currentPage", page.getNumber());
        pagination.put("pageSize", page.getSize());
        pagination.put("hasNext", page.hasNext());
        pagination.put("hasPrevious", page.hasPrevious());
        return pagination;
    }
}