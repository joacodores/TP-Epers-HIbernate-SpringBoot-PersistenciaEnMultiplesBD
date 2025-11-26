package ar.edu.unq.epersgeist.controller.dto.nightBringer;

import ar.edu.unq.epersgeist.modelo.NightBringer;

public record CrearNightBringerDTO(String nombre) {
    public NightBringer aModelo() {return new NightBringer(nombre);}
}
