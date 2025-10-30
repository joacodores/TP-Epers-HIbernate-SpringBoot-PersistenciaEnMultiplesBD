package ar.edu.unq.epersgeist.controller.exceptions;

public class CoordenadaNoEncontradaException extends RuntimeException {
    public CoordenadaNoEncontradaException(String message) {
        super("No se encontró la coordenada " + message);
    }
}
