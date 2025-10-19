package ar.edu.unq.epersgeist.persistencia.sql.entity;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE Espiritu SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public class EspirituDemoniacoSQL extends EspirituSQL {
    public EspirituDemoniacoSQL(Espiritu espiritu) {
        super(espiritu);
    }

    public EspirituDemoniacoSQL(Long id, String nombre) {super(id, nombre);}
}
