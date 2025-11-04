package co.unicauca.entity.state;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;

public class RechazadoState implements FormatoAState {

    @Override
    public FormatoAState evaluar(FormatoA formato, String observaciones) {
        throw new IllegalStateException("No se puede evaluar un formato rechazado");
    }

    @Override
    public FormatoAState aprobar(FormatoA formato) {
        throw new IllegalStateException("No se puede aprobar un formato rechazado");
    }

    @Override
    public FormatoAState rechazar(FormatoA formato, String observaciones) {
        throw new IllegalStateException("El formato ya está rechazado");
    }

    @Override
    public FormatoAState reenviar(FormatoA formato) {
        System.out.println("🔍 RechazadoState.reenviar() - Counter: " + formato.getCounter());

        if (formato.getCounter() >= 3) {
            System.out.println("🚫 Counter >= 3 - Pasando a RECHAZADO_DEFINITIVAMENTE");
            return new RechazadoDefinitivamenteState();
        }

        System.out.println("✅ Counter < 3 - Reenviando a ENTREGADO");
        formato.setObservations("");
        return new EntregadoState();
    }

    @Override
    public boolean puedeEditar() {
        return true;
    }

    @Override
    public boolean puedeReenviar() {
        return true;
    }

    @Override
    public boolean puedeEvaluar() {
        return false;
    }

    @Override
    public EnumEstado toEnumState() {
        return EnumEstado.RECHAZADO;
    }

    @Override
    public void onEnterState(FormatoA formato) {
        System.out.println("FormatoA " + formato.getId() + " RECHAZADO - Intentos: " + formato.getCounter());
    }
}