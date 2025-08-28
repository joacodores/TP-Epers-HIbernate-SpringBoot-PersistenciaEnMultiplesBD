CREATE TABLE IF NOT EXISTS espiritu (
    id SERIAL8 PRIMARY KEY,
    tipo VARCHAR(255),
    niveldeconexion int NOT NULL,
    nombre VARCHAR(255)
);