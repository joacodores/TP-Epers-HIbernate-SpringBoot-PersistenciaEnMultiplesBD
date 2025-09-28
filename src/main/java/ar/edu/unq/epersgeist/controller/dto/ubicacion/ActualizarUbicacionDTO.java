package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record ActualizarUbicacionDTO(String nombre, Integer energia) {
    public Ubicacion aModelo() {
        /* TODO: Luego de terminar la migración:
        if (tipo == TipoUbicacionDTO.SANTUARIO){
            return new Santuario(nombre, energia);
        } else if (tipo == TipoUbicacionDTO.CEMENTERIO) {
            return new Cementerio(nombre, energia);
        } else {
            return new Ubicacion();
        }
        */
        return new Ubicacion(nombre);
    }

}
