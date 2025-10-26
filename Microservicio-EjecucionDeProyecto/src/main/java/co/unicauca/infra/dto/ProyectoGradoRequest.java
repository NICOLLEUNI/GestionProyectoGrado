package co.unicauca.infra.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;



public record ProyectoGradoRequest(

   Long id,
   String nombre,
   LocalDate fecha,
   List<String> estudiantesEmail,
   Long idFormatoA

)
{}


