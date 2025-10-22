package ar.edu.unq.epersgeist.persistencia.neo.entity;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)


@Node(primaryLabel = "Ubicacion")
public class UbicacionNeo4J {


    @Id
    private Long id;
    private String nombre;
    private Integer energia;
    private TipoUbicacion tipo;

    @Relationship(type = "ubicacionesConectadas")
    private Set<ConexionPsionicaNeo4J> conexiones = new HashSet<>();


    public UbicacionNeo4J(Ubicacion ubicacion) {
        this.id = ubicacion.getId();
        this.nombre = ubicacion.getNombre();
        this.energia = ubicacion.getEnergia();
        if (ubicacion.esSantuario()){
            this.tipo = TipoUbicacion.SANTUARIO;
        }else {
            this.tipo = TipoUbicacion.CEMENTERIO;
        }

        this.conexiones = ubicacion.getConexiones().stream().map(conexion -> {
            UbicacionNeo4J ubi =  new UbicacionNeo4J();
            ubi.setId(conexion.getId());
            ubi.setNombre(conexion.getNombre());
            ubi.setEnergia(conexion.getEnergia());
            return ubi;
        })
            .filter(ubi -> !ubi.getId().equals(this.id))
            .collect(Collectors.toSet());
    }
}
