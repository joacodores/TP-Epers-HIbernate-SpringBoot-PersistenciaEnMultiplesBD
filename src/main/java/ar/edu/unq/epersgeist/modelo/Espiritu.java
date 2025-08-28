package ar.edu.unq.epersgeist.modelo;

import java.io.Serializable;

public class Espiritu implements Serializable {

    private Long id;
    private String tipo;
    private Integer nivelDeConexion;
    private String nombre;
    private final Integer maxNivelDeConexion = 100;
    private final Integer minNivelDeConexion = 0;

    public Espiritu(String tipo, Integer nivelDeConexion, String nombre) {
        this.tipo = tipo;
        validarNivelDeConexion(nivelDeConexion);
        this.nombre = nombre;
    }

    private void validarNivelDeConexion(Integer nivelDeConexion) throws RuntimeException {
        if (nivelDeConexion < minNivelDeConexion || nivelDeConexion > maxNivelDeConexion) {
            throw new RuntimeException(String.format("El nivel de conexión para el espiritu %s es inválido (debe ser un número entre %s y %s)", this.nombre, this.minNivelDeConexion, this.maxNivelDeConexion));
        }
        this.nivelDeConexion = nivelDeConexion;
    }

    public Medium aumentarConexion(Medium medium) {
        this.nivelDeConexion += 10;
        if (this.nivelDeConexion >= 100) this.nivelDeConexion = 100;
        return medium;
    }

    public void setId(Long newId){
        id = newId;
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