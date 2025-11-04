package co.unicauca.entity.state;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;

public class RechazadoDefinitivamenteState implements FormatoAState {

    @Override
    public FormatoAState evaluar(FormatoA formato, String observaciones) {
        throw new IllegalStateException("Formato rechazado definitivamente - no se puede evaluar");
    }

    @Override
    public FormatoAState aprobar(FormatoA formato) {
        throw new IllegalStateException("Formato rechazado definitivamente - no se puede aprobar");
    }

    @Override
    public FormatoAState rechazar(FormatoA formato, String observaciones) {
        throw new IllegalStateException("Formato ya está rechazado definitivamente");
    }

    @Override
    public FormatoAState reenviar(FormatoA formato) {
        throw new IllegalStateException("Formato rechazado definitivamente - no se puede reenviar");
    }

    @Override
    public boolean puedeEditar() {
        return false;
    }

    @Override
    public boolean puedeReenviar() {
        return false;
    }

    @Override
    public boolean puedeEvaluar() {
        return false;
    }

    @Override
    public EnumEstado toEnumState() {
        return EnumEstado.RECHAZADO_DEFINITIVAMENTE;
    }

    @Override
    public void onEnterState(FormatoA formato) {
        System.out.println("FormatoA " + formato.getId() + " RECHAZADO DEFINITIVAMENTE");
    }
}