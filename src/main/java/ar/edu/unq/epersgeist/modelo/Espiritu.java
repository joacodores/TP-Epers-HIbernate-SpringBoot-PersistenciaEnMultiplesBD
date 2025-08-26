package ar.edu.unq.epersgeist.modelo;

import lombok.Setter;

import java.io.Serializable;

public class Espiritu implements Serializable {

    @Setter
    private Long id;
    private String tipo;
    private Integer nivelDeConexion;
    private String nombre;

    public Espiritu(String tipo, Integer nivelDeConexion, String nombre) {
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }

    public Medium aumentarConexion(Medium medium) {
        this.nivelDeConexion += 10;
        return medium;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getNivelDeConexion() {
        return nivelDeConexion;
    }

    public String getNombre() {
        return nombre;
    }
}