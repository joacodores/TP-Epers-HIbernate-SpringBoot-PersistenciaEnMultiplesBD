package ar.edu.unq.epersgeist.controller.exceptions;

public class ActualizarRecursoException extends RuntimeException {

    public ActualizarRecursoException(String message) {
        super("Hubo un error tras actualizar el estado " + message);
    }

}
