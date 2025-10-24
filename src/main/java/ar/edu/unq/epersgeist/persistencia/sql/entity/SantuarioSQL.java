package ar.edu.unq.epersgeist.persistencia.sql.entity;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@Entity
@DiscriminatorValue("SANTUARIO")
@SQLDelete(sql = "UPDATE Ubicacion SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public class SantuarioSQL extends UbicacionSQL {

    public SantuarioSQL(Ubicacion ubicacion) {
        super(ubicacion);
    }

    public SantuarioSQL(long id, String nombre) {
        super(id, nombre);
    }

}
