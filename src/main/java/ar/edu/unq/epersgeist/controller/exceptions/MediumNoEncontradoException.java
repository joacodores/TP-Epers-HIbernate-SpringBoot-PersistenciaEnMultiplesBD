package ar.edu.unq.epersgeist.controller.exceptions;

public class MediumNoEncontradoException extends RuntimeException {

    public MediumNoEncontradoException(String message) {
        super("No se encontró al medium " + message);
    }

}
