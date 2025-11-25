package ar.edu.unq.epersgeist.modelo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LightBringer {

    private String nombre;
    private Long id;
    private int fuerzaDeAtaque;

    public LightBringer(String nombre, int fuerzaDeAtaque) {
        this.nombre = nombre;
        this.fuerzaDeAtaque = fuerzaDeAtaque;
    }

    public void iluminarMedium(Medium medium){
        medium.iluminar(this.fuerzaDeAtaque);
    }


}