package ar.edu.unq.epersgeist.servicios.exceptions;

public class UbicacionesNoConectadasException extends RuntimeException {

    public UbicacionesNoConectadasException(Long idOrigen, Long idDestino) {
        super(String.format("No existe camino que vaya de la ubicación %s a %s", idOrigen, idDestino));
    }

}
