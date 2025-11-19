package ar.edu.unq.epersgeist;

import ar.edu.unq.epersgeist.controller.dto.errores.APIError;
import ar.edu.unq.epersgeist.controller.exceptions.*;
import ar.edu.unq.epersgeist.modelo.exceptions.*;
import ar.edu.unq.epersgeist.servicios.exceptions.NoHaySantuarioCorruptoException;
import ar.edu.unq.epersgeist.servicios.exceptions.UbicacionesNoConectadasException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<APIError> buildErrorResponse(HttpStatus status, String description) {
        APIError error = new APIError(status.value(), description);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler({UbicacionNoEncontradaException.class, EspirituNoEncontradoException.class, MediumNoEncontradoException.class})
    public ResponseEntity<APIError> handleNotFound(RuntimeException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UbicacionNoValidaException.class)
    public ResponseEntity<APIError> handleUbicacionNoValida(UbicacionNoValidaException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(TipoNuloException.class)
    public ResponseEntity<APIError> handleTipoNulo(TipoNuloException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ActualizarRecursoException.class)
    public ResponseEntity<APIError> handleActualizarRecurso(ActualizarRecursoException e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(NivelDeConexionFueraDeRangoException.class)
    public ResponseEntity<APIError> handleNivelDeConexionFueraDeRango(NivelDeConexionFueraDeRangoException e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "El nivel de conexión del espiritu está fuera de rango");
    }

    @ExceptionHandler({
            EspirituNoPuedeConectarException.class,
            ExorcistaSinAngelesException.class,
            EspirituNoEsLibreException.class,
            EspirituNoPuedeInvocarseEnUbicacionException.class,
            MediumNoPuedeTenerMasManaQueSuManaMax.class,
            UbicacionesNoConectadasException.class
    })
    public ResponseEntity<APIError> handleNotSensibleModelExceptions(RuntimeException e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(NoHaySantuarioCorruptoException.class)
    public ResponseEntity<APIError> handleNoHaySantuarioCorrupto(NoHaySantuarioCorruptoException e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIError> handleDataIntegrity(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof org.hibernate.exception.ConstraintViolationException cve) {
            String mensaje = cve.getMessage();
            String sql = cve.getSQL();
            String tabla = null;
            if (sql != null) {
                sql = sql.toLowerCase();
                int idx = sql.indexOf("into");
                if (idx != -1) {
                    String[] partes = sql.substring(idx + 5).split("\\s+|\\(");
                    tabla = partes[0]; // nombre de la tabla
                }
            }
            if (mensaje != null && mensaje.contains("(nombre)")) {
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "No se puede crear " + tabla + " con nombre repetido");
            }
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Violación de restricción de datos");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleGeneric(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error interno" /* + e.getMessage()*/ );
    }

}
