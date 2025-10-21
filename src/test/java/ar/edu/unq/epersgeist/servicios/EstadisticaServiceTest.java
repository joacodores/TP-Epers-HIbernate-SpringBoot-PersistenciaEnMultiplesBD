package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.servicios.exceptions.NoHaySantuarioCorruptoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class EstadisticaServiceTest {

    @Autowired
    private EstadisticaService estadisticaService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private UbicacionService ubicacionService;

    @Test
    void santuarioCorruptoTest() {
        Ubicacion avellaneda = ubicacionService.crear(new Santuario("Avellaneda", 10, 10L));
        Ubicacion tokyo = ubicacionService.crear(new Santuario("Tokyo", 100, 10L));
        // Avellaneda: 5 demonios, 4 ángeles. Diferencia = 1
        for (int i = 1; i <= 5; i++) {
            espirituService.crear(new EspirituDemoniaco(100, "demonio" + i, avellaneda));
        }
        for (int i = 1; i <= 4; i++) {
            espirituService.crear(new EspirituAngelical(100, "angel" + i, avellaneda));
        }
        // Tokyo: 3 demonios, 1 ángel. Diferencia = 2
        Long demonio6Id = espirituService.crear(new EspirituDemoniaco(100, "demonio6", tokyo)).getId();
        for (int i = 7; i <= 8; i++) {
            espirituService.crear(new EspirituDemoniaco(100, "demonio" + i, tokyo));
        }
        espirituService.crear(new EspirituAngelical(100, "angel5", tokyo));
        Long spinettaId = mediumService.crear(new Medium("El flaco Spinetta", 100, 100, tokyo)).getId();
        espirituService.conectar(demonio6Id, spinettaId);
        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();
        assertEquals("Tokyo", reporte.nombreSantuario());
        assertEquals(spinettaId, reporte.mediumConMasDemonios().getId());
        assertEquals(3, reporte.cantDemoniosTotal());
        assertEquals(2, reporte.cantDemoniosLibres());
    }

    @Test
    void santuarioMasCorruptoNoPuedeSerUnCementerioTest() {
        Ubicacion avellaneda = ubicacionService.crear(new Santuario("Avellaneda", 10, 10L));
        Ubicacion tokyo = ubicacionService.crear(new Cementerio("Tokyo", 100, 10L));
        // Avellaneda: 5 demonios, 4 ángeles. Diferencia = 1
        for (int i = 1; i <= 5; i++) {
            espirituService.crear(new EspirituDemoniaco(100, "demonio" + i, avellaneda));
        }
        for (int i = 1; i <= 4; i++) {
            espirituService.crear(new EspirituAngelical(100, "angel" + i, avellaneda));
        }
        // Tokyo: 3 demonios, 1 ángel. Diferencia = 2. PERO ES UN CEMENTERIO -> NO PUEDE SER EL SANTUARIO MAS CORRUPTO
        for (int i = 6; i <= 8; i++) {
            espirituService.crear(new EspirituDemoniaco(100, "demonio" + i, tokyo));
        }
        espirituService.crear(new EspirituAngelical(100, "angel5", tokyo));
        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();
        assertEquals("Avellaneda", reporte.nombreSantuario());
        assertEquals(5, reporte.cantDemoniosTotal());
        assertEquals(5, reporte.cantDemoniosLibres());
    }

    @Test
    void siNoExisteSantuarioConMasDemoniosQueAngelesSeLanzaExcepcionTest() {
        Ubicacion avellaneda = ubicacionService.crear(new Santuario("Avellaneda", 10, 10L));
        // Avellaneda: 4 demonios, 4 ángeles. Diferencia = 0
        for (int i = 1; i <= 4; i++) {
            espirituService.crear(new EspirituDemoniaco(100, "demonio" + i, avellaneda));
        }
        for (int i = 1; i <= 4; i++) {
            espirituService.crear(new EspirituAngelical(100, "angel" + i, avellaneda));
        }
        assertThrows(NoHaySantuarioCorruptoException.class, () -> estadisticaService.santuarioCorrupto());
    }

    @AfterEach
    void cleanup() {
        espirituService.eliminarTodo();
        mediumService.eliminarTodo();
        ubicacionService.eliminarTodo();
    }

}
