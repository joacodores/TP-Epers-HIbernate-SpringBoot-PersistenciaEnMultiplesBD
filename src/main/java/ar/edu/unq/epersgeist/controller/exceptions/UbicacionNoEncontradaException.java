package ar.edu.unq.epersgeist.controller.exceptions;

public class UbicacionNoEncontradaException extends RuntimeException {

    public UbicacionNoEncontradaException(String message) {
        super("No se encontró la ubicación " + message);
    }

}
