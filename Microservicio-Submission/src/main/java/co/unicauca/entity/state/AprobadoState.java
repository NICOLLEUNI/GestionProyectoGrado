package co.unicauca.entity.state;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;

public class AprobadoState implements FormatoAState {

    @Override
    public FormatoAState evaluar(FormatoA formato, String observaciones) {
        throw new IllegalStateException("No se puede evaluar un formato aprobado");
    }

    @Override
    public FormatoAState aprobar(FormatoA formato) {
        throw new IllegalStateException("El formato ya está aprobado");
    }

    @Override
    public FormatoAState rechazar(FormatoA formato, String observaciones) {
        throw new IllegalStateException("No se puede rechazar un formato aprobado");
    }

    @Override
    public FormatoAState reenviar(FormatoA formato) {
        throw new IllegalStateException("No se puede reenviar un formato aprobado");
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
        return EnumEstado.APROBADO;
    }

    @Override
    public void onEnterState(FormatoA formato) {
        System.out.println("FormatoA " + formato.getId() + " APROBADO - Listo para anteproyecto");
        // Aquí podrías disparar evento para crear anteproyecto
    }
}