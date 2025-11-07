package ar.edu.unq.epersgeist.persistencia.mongo.entity;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@Document("ubicaciones")
public class UbicacionMongo {
    @Id
    private String id;
    private String nombre;
    private List<CoordenadaMongo> coordenadas;
//    private GeoJsonPolygon polygon;

    public UbicacionMongo() {};

    public UbicacionMongo(Ubicacion ubicacion) {
        this.id = String.valueOf(ubicacion.getId());
        this.nombre = ubicacion.getNombre();
       this.coordenadas = ubicacion.getCoordenadas().stream()
                .map(coordenada -> new CoordenadaMongo(coordenada.getLatitud(), coordenada.getLongitud())).collect(Collectors.toList());
//        List<Point> puntos = ubicacion.getCoordenadas().stream()
//                .map(c -> new Point(c.getLongitud(), c.getLatitud())) // lon, lat
//                .toList();
//
//        this.polygon = new GeoJsonPolygon(puntos);
    }
}