package co.unicauca.infra.dto;



import java.time.LocalDate;


public record FormatoAVersionRequest (

     Long id,
     int numVersion,
     LocalDate fecha,
     String title,
     String modalidad,
     String state,
     String observations,
     int counter,
     int IdformatoA
     )
{}
