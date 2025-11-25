package ar.edu.unq.epersgeist.controller.exceptions;


public class EspirituAngelicalException extends RuntimeException {
    public EspirituAngelicalException(String message) {
        super("Un espiritu angelical no puede poseer un medium" + message);
    }
}