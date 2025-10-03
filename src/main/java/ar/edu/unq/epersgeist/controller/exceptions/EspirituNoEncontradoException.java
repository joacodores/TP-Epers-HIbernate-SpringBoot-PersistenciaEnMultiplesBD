package ar.edu.unq.epersgeist.controller.exceptions;

public class EspirituNoEncontradoException extends RuntimeException {

    public EspirituNoEncontradoException(String message) {
        super("No se encontró al espiritu " + message);
    }

}
