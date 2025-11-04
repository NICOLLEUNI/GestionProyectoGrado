package co.unicauca.entity.state;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;

public class EntregadoState implements FormatoAState {

    @Override
    public FormatoAState evaluar(FormatoA formato, String observaciones) {
        formato.setObservations(observaciones);
        return new EvaluacionState();
    }

    @Override
    public FormatoAState aprobar(FormatoA formato) {
        throw new IllegalStateException("No se puede aprobar un formato que no está en evaluación");
    }

    @Override
    public FormatoAState rechazar(FormatoA formato, String observaciones) {
        throw new IllegalStateException("No se puede rechazar un formato que no está en evaluación");
    }

    @Override
    public FormatoAState reenviar(FormatoA formato) {
        throw new IllegalStateException("No se puede reenviar un formato que no ha sido rechazado");
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
        return true;
    }

    @Override
    public EnumEstado toEnumState() {
        return EnumEstado.ENTREGADO;
    }

    @Override
    public void onEnterState(FormatoA formato) {
        // Lógica cuando entra a estado ENTREGADO
        System.out.println("FormatoA " + formato.getId() + " en estado ENTREGADO");
    }
}