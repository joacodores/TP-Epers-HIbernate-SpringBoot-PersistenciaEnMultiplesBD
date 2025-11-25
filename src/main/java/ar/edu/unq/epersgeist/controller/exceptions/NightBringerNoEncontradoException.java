package ar.edu.unq.epersgeist.controller.exceptions;

public class NightBringerNoEncontradoException extends RuntimeException {
    public NightBringerNoEncontradoException(String message) {
        super("No se encontró al nightbringer " + message);
    }
}
