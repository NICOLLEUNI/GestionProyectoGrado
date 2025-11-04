package co.unicauca.entity.state;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;

public interface FormatoAState {

    // Transiciones de estado
    FormatoAState evaluar(FormatoA formato, String observaciones);
    FormatoAState aprobar(FormatoA formato);
    FormatoAState rechazar(FormatoA formato, String observaciones);
    FormatoAState reenviar(FormatoA formato);

    // Validaciones específicas por estado
    boolean puedeEditar();
    boolean puedeReenviar();
    boolean puedeEvaluar();

    // Conversión a Enum para persistencia
    EnumEstado toEnumState();

    // Comportamientos específicos
    void onEnterState(FormatoA formato);
}