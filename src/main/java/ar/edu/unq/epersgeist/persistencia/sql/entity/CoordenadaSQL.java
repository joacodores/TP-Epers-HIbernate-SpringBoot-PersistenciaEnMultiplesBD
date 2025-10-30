package ar.edu.unq.epersgeist.persistencia.sql.entity;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Coordenada")
//soft delete?
public class CoordenadaSQL {

    @Setter
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Setter
    private double latitud;

    @Setter
    private double longitud;

    public CoordenadaSQL(Coordenada coordenada) {
        this.id = coordenada.getId();
        this.latitud = coordenada.getLatitud();
        this.longitud = coordenada.getLongitud();
    }

    public CoordenadaSQL(double latitud, double longitud) {
        this.latitud =  latitud;
        this.longitud = longitud;
    }


}
