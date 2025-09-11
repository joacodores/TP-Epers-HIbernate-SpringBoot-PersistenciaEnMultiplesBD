package ar.edu.unq.epersgeist.servicios.enums;

public enum Direccion {
    ASCENDENTE("asc"),
    DESCENDENTE("desc");

    private final String qdir;

    Direccion(String qdir) {
        this.qdir = qdir;
    }

    public String getQueryDir() {
        return qdir;
    }
}
