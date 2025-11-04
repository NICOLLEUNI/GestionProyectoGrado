package co.unicauca.entity.state;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;

public class EvaluacionState implements FormatoAState {

    @Override
    public FormatoAState evaluar(FormatoA formato, String observaciones) {
        throw new IllegalStateException("El formato ya está en evaluación");
    }

    @Override
    public FormatoAState aprobar(FormatoA formato) {
        // Lógica de aprobación
        formato.setObservations("Aprobado sin observaciones");
        return new AprobadoState();
    }

    @Override
    public FormatoAState rechazar(FormatoA formato, String observaciones) {
        // Lógica de rechazo
        formato.setObservations(observaciones);
        formato.setCounter(formato.getCounter() + 1);
        return new RechazadoState();
    }

    @Override
    public FormatoAState reenviar(FormatoA formato) {
        throw new IllegalStateException("No se puede reenviar un formato en evaluación");
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
        return EnumEstado.ENTREGADO; // Mantenemos ENTREGADO para compatibilidad
    }

    @Override
    public void onEnterState(FormatoA formato) {
        System.out.println("FormatoA " + formato.getId() + " en estado EVALUACION");
    }
}