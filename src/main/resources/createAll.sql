CREATE TABLE IF NOT EXISTS espiritu (
    id SERIAL8 PRIMARY KEY,
    tipo VARCHAR(255),
    NivelDeConexion int NOT NULL,
    nombre VARCHAR(255) UNIQUE
);