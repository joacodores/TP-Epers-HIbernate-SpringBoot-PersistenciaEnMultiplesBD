package ar.edu.unq.epersgeist.persistencia.neo.entity;

import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Santuario;
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
        if (ubicacion.esSantuario()) {
            this.tipo = TipoUbicacion.SANTUARIO;
        } else {
            this.tipo = TipoUbicacion.CEMENTERIO;
        }
        this.conexiones = ubicacion.getConexiones().stream().map(conexion -> {
                    ConexionPsionicaNeo4J conexionP = new ConexionPsionicaNeo4J();
                    conexionP.setId(conexion.getId());
                    conexionP.setCosto(conexion.getCosto());
                    UbicacionNeo4J destino = new UbicacionNeo4J(conexion.getDestino());
                    conexionP.setDestino(destino);
                    return conexionP;
                })
                .filter(c -> !c.getDestino().getId().equals(this.id))
                .collect(Collectors.toSet());

    }

    public Ubicacion toModel() {
        Ubicacion ubicacion;
        if (this.tipo == TipoUbicacion.SANTUARIO) {
            ubicacion = new Santuario(this.nombre, this.energia);
        } else {
            ubicacion = new Cementerio(this.nombre, this.energia);
        }
        ubicacion.setId(this.id);
        return ubicacion;
    }

}
