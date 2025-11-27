package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.sql.entity.NightBringerSQL;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NightBringer {

    private String nombre;
    private Long id;
    @JsonManagedReference("nb-espiritus")
    private List<EspirituDemoniaco> espiritus = new ArrayList<>();

    public NightBringer(String nombre){
        this.nombre = nombre;
    }

    public NightBringer (NightBringerSQL nightBringerSQL) {
        this.id = nightBringerSQL.getId();
        this.nombre = nightBringerSQL.getNombre();

        if(nightBringerSQL.getEspiritus()!=null){
            nightBringerSQL.getEspiritus().stream().map(eSQL -> {
                EspirituDemoniaco espiritu = new EspirituDemoniaco(eSQL.getId(), eSQL.getNombre());
                espiritu.setNivelDeConexion(eSQL.getNivelDeConexion());
                Ubicacion ubicacion = new Cementerio(eSQL.getUbicacion());
                espiritu.setUbicacion(ubicacion);
                espiritus.add(espiritu);
                return espiritu;
            });
        }


    }

    public EspirituDemoniaco spawnearEspirituEnUbicacion(Ubicacion ubicacion, String nombre){
        EspirituDemoniaco e = new EspirituDemoniaco(50, nombre, ubicacion );
        e.setNightBringer(this);
        this.espiritus.add(e);
        return e;
    }
}