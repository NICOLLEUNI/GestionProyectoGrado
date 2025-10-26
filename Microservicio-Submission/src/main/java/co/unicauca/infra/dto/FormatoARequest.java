package co.unicauca.infra.dto;


public record FormatoARequest (

        //Es lo que recibo cuando se evalua un formatoA por el coordinador
        Long id,
        String title,
        String state,
        String observations,
        String counter

) { }
