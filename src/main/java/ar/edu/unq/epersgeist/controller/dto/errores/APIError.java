package ar.edu.unq.epersgeist.controller.dto.errores;

public record APIError(
        Integer statusCode,
        String description
) {

}
