package co.unicauca.infra.dto;



import java.time.LocalDate;


public record FormatoAVersionRequest (

     String id,
     int numVersion,
     LocalDate fecha,
     String titulo,
     String modalidad,
     String estado,
     String observaciones,
     int counter,
     int IdformatoA
     )
{}
