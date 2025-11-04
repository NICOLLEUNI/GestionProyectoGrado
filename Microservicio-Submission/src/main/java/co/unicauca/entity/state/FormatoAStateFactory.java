package co.unicauca.entity.state;

import co.unicauca.entity.EnumEstado;

public class FormatoAStateFactory {

    public static FormatoAState createState(EnumEstado estado) {
        return switch (estado) {
            case ENTREGADO -> new EntregadoState();
            case APROBADO -> new AprobadoState();
            case RECHAZADO -> new RechazadoState();
            case RECHAZADO_DEFINITIVAMENTE -> new RechazadoDefinitivamenteState();
        };
    }
}