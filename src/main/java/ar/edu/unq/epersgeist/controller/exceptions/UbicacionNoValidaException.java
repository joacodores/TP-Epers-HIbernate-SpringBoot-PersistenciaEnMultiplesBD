package ar.edu.unq.epersgeist.controller.exceptions;

public class UbicacionNoValidaException extends RuntimeException {

    public UbicacionNoValidaException(String message) {
        super("La ubicación no es válida " + message);
    }

}
