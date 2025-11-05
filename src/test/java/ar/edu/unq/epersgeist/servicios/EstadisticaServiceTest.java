package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.controller.dto.estadistica.*;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.SensorNormalizado;
import ar.edu.unq.epersgeist.servicios.exceptions.NoHaySantuarioCorruptoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureDataMongo
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

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void prepare() {
        mongoTemplate.dropCollection("epersgeist_normalized");
    }

    @Test
    void santuarioCorruptoTest() {
        Ubicacion avellaneda = ubicacionService.crear(new Santuario("Avellaneda", 10));
        Ubicacion tokyo = ubicacionService.crear(new Santuario("Tokyo", 100));
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
        Ubicacion avellaneda = ubicacionService.crear(new Santuario("Avellaneda", 10));
        Ubicacion tokyo = ubicacionService.crear(new Cementerio("Tokyo", 100));
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
        Ubicacion avellaneda = ubicacionService.crear(new Santuario("Avellaneda", 10));
        // Avellaneda: 4 demonios, 4 ángeles. Diferencia = 0
        for (int i = 1; i <= 4; i++) {
            espirituService.crear(new EspirituDemoniaco(100, "demonio" + i, avellaneda));
        }
        for (int i = 1; i <= 4; i++) {
            espirituService.crear(new EspirituAngelical(100, "angel" + i, avellaneda));
        }
        assertThrows(NoHaySantuarioCorruptoException.class, () -> estadisticaService.santuarioCorrupto());
    }

    @Test
    void obtenerCantidadDeMedicionesPorTipoDeSensorTest() {
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "temperatura", 20.0, "C", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "temperatura", 10.0, "C", "2025-10-30T13:00:00Z"),
                new SensorNormalizado(null, "sensor_3", "presion", 1000.0, "hPa", "2025-10-30T14:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "presion", 1040.0, "hPa", "2025-10-30T15:00:00Z"),
                new SensorNormalizado(null, "sensor_5", "temperatura", 20.0, "C", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_6", "temperatura", 10.0, "C", "2025-10-30T13:00:00Z"),
                new SensorNormalizado(null, "sensor_7", "presion", 1000.0, "hPa", "2025-10-30T14:00:00Z"),
                new SensorNormalizado(null, "sensor_8", "sonido", 104.0, "dB", "2025-10-30T15:00:00Z"),
                new SensorNormalizado(null, "sensor_8", "proximidad", 8040.0, "metros", "2025-10-30T15:00:00Z"),
                new SensorNormalizado(null, "sensor_8", "proximidad", 10040.0, "metros", "2025-10-30T15:00:00Z")
        ));
        List<ReporteCantidadPorTipoDTO> reportes = estadisticaService.obtenerCantidadPorTipoDeSensor();
        assertEquals(4, reportes.size());
        Map<String, Long> cantidadesPorTipo = reportes.stream()
                .collect(Collectors.toMap(ReporteCantidadPorTipoDTO::tipo, ReporteCantidadPorTipoDTO::cantidad));
        assertEquals(1, cantidadesPorTipo.get("sonido"));
        assertEquals(2, cantidadesPorTipo.get("proximidad"));
        assertEquals(3, cantidadesPorTipo.get("presion"));
        assertEquals(4, cantidadesPorTipo.get("temperatura"));
    }

    @Test
    void obtenerCantidadDeMedicionesPorSensorTest() {
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "temperatura", 20.0, "C", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_1", "temperatura", 10.0, "C", "2025-10-30T13:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "presion", 1000.0, "hPa", "2025-10-30T14:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "presion", 1040.0, "hPa", "2025-10-30T15:00:00Z"),
                new SensorNormalizado(null, "sensor_1", "temperatura", 20.0, "C", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_1", "temperatura", 10.0, "C", "2025-10-30T13:00:00Z"),
                new SensorNormalizado(null, "sensor_3", "presion", 1000.0, "hPa", "2025-10-30T14:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "proximidad", 8040.0, "metros", "2025-10-30T15:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "proximidad", 10040.0, "metros", "2025-10-30T15:00:00Z")
        ));
        List<ReporteCantidadPorSensorDTO> reportes = estadisticaService.obtenerCantidadPorSensor();
        assertEquals(4, reportes.size());
        Map<String, Long> cantidadesPorSensor = reportes.stream()
                .collect(Collectors.toMap(ReporteCantidadPorSensorDTO::sensorId, ReporteCantidadPorSensorDTO::cantidad));
        assertEquals(4, cantidadesPorSensor.get("sensor_1"));
        assertEquals(2, cantidadesPorSensor.get("sensor_2"));
        assertEquals(1, cantidadesPorSensor.get("sensor_3"));
        assertEquals(2, cantidadesPorSensor.get("sensor_4"));
    }

    @Test
    void obtenerPromedioPorTipoDeSensorTest() {
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "temperatura", 20.0, "C", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "temperatura", 10.0, "C", "2025-10-30T13:00:00Z"),
                new SensorNormalizado(null, "sensor_3", "presion", 1000.0, "hPa", "2025-10-30T14:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "presion", 1040.0, "hPa", "2025-10-30T15:00:00Z")
        ));
        List<ReportePromedioPorTipoDTO> reportes = estadisticaService.obtenerValoresPromedioPorTipoDeSensor();
        assertEquals(2, reportes.size());
        assertTrue(reportes.stream().anyMatch(r -> r.tipo().equals("temperatura")));
        assertEquals(15.0, reportes.stream().filter(r -> r.tipo().equals("temperatura"))
                .findFirst().orElseThrow().promedioValor());
        assertTrue(reportes.stream().anyMatch(r -> r.tipo().equals("presion")));
        assertEquals(1020.0, reportes.stream().filter(r -> r.tipo().equals("presion"))
                .findFirst().orElseThrow().promedioValor());
    }

    @Test
    void obtenerPromedioPorTipoDeSensorDevuelveArrayVacioCuandoNoHayColeccionTest() {
        List<ReportePromedioPorTipoDTO> reportes = estadisticaService.obtenerValoresPromedioPorTipoDeSensor();
        assertTrue(reportes.isEmpty());
    }

    @Test
    void normalizarDatosDeSensoresTest() {
        estadisticaService.normalizeSensorData();
        List<ReporteCantidadPorTipoDTO> reportes = estadisticaService.obtenerCantidadPorTipoDeSensor();
        assertEquals(4, reportes.size());
        Map<String, Long> cantidadesPorTipo = reportes.stream()
                .collect(Collectors.toMap(ReporteCantidadPorTipoDTO::tipo, ReporteCantidadPorTipoDTO::cantidad));
        assertEquals(53, cantidadesPorTipo.get("presion"));
        assertEquals(56, cantidadesPorTipo.get("sonido"));
        assertEquals(50, cantidadesPorTipo.get("proximidad"));
        assertEquals(41, cantidadesPorTipo.get("temperatura"));
    }

    @Test
    void medicionesAnomalasMaximoTest() {
        // En el phasmophobia podes detectar si hay fantasma en una hab si la temperatura baja de 10ºC
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "temperatura", 15.0, "C", "2025-10-30T10:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "temperatura", -5.0, "C", "2025-10-30T11:00:00Z"),
                new SensorNormalizado(null, "sensor_3", "temperatura", -12.3, "C", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "temperatura", 25.0, "C", "2025-10-30T13:00:00Z")
        ));
        List<SensorNormalizado> anomaliasTemperatura = estadisticaService.obtenerMedicionesAnomalas("temperatura", null, 10.0);
        assertEquals(2, anomaliasTemperatura.size());
        assertTrue(anomaliasTemperatura.stream().allMatch(s ->
                s.getTipo().equals("temperatura") && s.getValor() < 10.0
        ));
        assertTrue(anomaliasTemperatura.stream()
                .map(SensorNormalizado::getSensorId)
                .toList()
                .containsAll(List.of("sensor_2", "sensor_3")));
    }

    @Test
    void medicionesNormalesNoSeMarcanComoAnomalasTest() {
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "temperatura", 15.0, "C", "2025-10-30T10:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "temperatura", -5.0, "C", "2025-10-30T11:00:00Z"),
                new SensorNormalizado(null, "sensor_3", "temperatura", -12.3, "C", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "temperatura", 25.0, "C", "2025-10-30T13:00:00Z")
        ));
        assertTrue(estadisticaService.obtenerMedicionesAnomalas("temperatura", 0.0, null)
                .stream()
                .noneMatch(s -> s.getValor() < 0.0));
    }

    @Test
    void medicionesAnomalasMinimoTest() {
        // Si hay 100 dB o más asumimos que es una anomalía
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "sonido", 10.0, "dB", "2025-10-30T10:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "sonido", 5.0, "dB", "2025-10-30T11:00:00Z"),
                new SensorNormalizado(null, "sensor_3", "sonido", 121.3, "dB", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "sonido", 100.0, "dB", "2025-10-30T13:00:00Z")
        ));
        List<SensorNormalizado> anomaliasSonido = estadisticaService.obtenerMedicionesAnomalas("sonido", 100.0, null);
        assertEquals(2, anomaliasSonido.size());
        assertTrue(anomaliasSonido.stream().allMatch(s ->
                s.getTipo().equals("sonido") && s.getValor() >= 100.0
        ));
        assertTrue(anomaliasSonido.stream()
                .map(SensorNormalizado::getSensorId)
                .toList()
                .containsAll(List.of("sensor_3", "sensor_4")));
    }

    @Test
    void promedioPorSensorTest() {
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "temperatura", 10.0, "C", "2025-11-05T10:00:00Z"),
                new SensorNormalizado(null, "sensor_1", "temperatura", 20.0, "C", "2025-11-05T11:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "presion", 1000.0, "hPa", "2025-11-05T12:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "presion", 1020.0, "hPa", "2025-11-05T13:00:00Z"),
                new SensorNormalizado(null, "sensor_3", "temperatura", 30.0, "C", "2025-11-05T10:00:00Z"),
                new SensorNormalizado(null, "sensor_3", "temperatura", 40.0, "C", "2025-11-05T11:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "presion", 2000.0, "hPa", "2025-11-05T12:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "presion", 2040.0, "hPa", "2025-11-05T13:00:00Z")
        ));
        List<ReportePromedioPorSensorDTO> reportes = estadisticaService.obtenerPromedioPorSensor();
        assertEquals(4, reportes.size());
        Map<String, List<ReportePromedioPorSensorDTO>> porTipo =
                reportes.stream().collect(Collectors.groupingBy(ReportePromedioPorSensorDTO::tipo));
        List<ReportePromedioPorSensorDTO> temperaturas = porTipo.get("temperatura");
        assertEquals(2, temperaturas.size());
        Map<String, ReportePromedioPorSensorDTO> tempPorSensor =
                temperaturas.stream().collect(Collectors.toMap(ReportePromedioPorSensorDTO::sensorId, r -> r));
        assertEquals(15.0, tempPorSensor.get("sensor_1").promedio());
        assertEquals(35.0, tempPorSensor.get("sensor_3").promedio());
        assertEquals(2, tempPorSensor.get("sensor_1").totalMediciones());
        assertEquals(2, tempPorSensor.get("sensor_3").totalMediciones());
        List<ReportePromedioPorSensorDTO> presiones = porTipo.get("presion");
        assertEquals(2, presiones.size());
        Map<String, ReportePromedioPorSensorDTO> presionPorSensor =
                presiones.stream().collect(Collectors.toMap(ReportePromedioPorSensorDTO::sensorId, r -> r));
        assertEquals(1010.0, presionPorSensor.get("sensor_2").promedio());
        assertEquals(2020.0, presionPorSensor.get("sensor_4").promedio());
        assertEquals(2, presionPorSensor.get("sensor_2").totalMediciones());
        assertEquals(2, presionPorSensor.get("sensor_4").totalMediciones());
    }

    @Test
    void promedioDiarioPorTipoTest() {
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "temperatura", 20.0, "C", "2025-10-29T12:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "temperatura", 10.0, "C", "2025-10-29T13:00:00Z"),
                new SensorNormalizado(null, "sensor_3", "temperatura", 30.0, "C", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_4", "presion", 1000.0, "hPa", "2025-10-29T15:00:00Z"),
                new SensorNormalizado(null, "sensor_5", "presion", 1040.0, "hPa", "2025-10-30T15:00:00Z")
        ));
        List<ReportePromedioDiarioPorTipoDTO> reportes = estadisticaService.obtenerPromedioDiarioPorTipo();
        assertEquals(4, reportes.size());
        ReportePromedioDiarioPorTipoDTO tempDia29 = reportes.stream()
                .filter(r -> r.tipo().equals("temperatura") && r.fecha().equals("2025-10-29"))
                .findFirst().orElseThrow();
        assertEquals(15.0, tempDia29.promedio());
    }

    @Test
    void promedioDiarioPorSensorTest() {
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "temperatura", 10.0, "C", "2025-11-05T10:00:00Z"),
                new SensorNormalizado(null, "sensor_1", "temperatura", 20.0, "C", "2025-11-05T11:00:00Z"),
                new SensorNormalizado(null, "sensor_1", "temperatura", 15.0, "C", "2025-11-06T12:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "presion", 1000.0, "hPa", "2025-11-05T10:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "presion", 1020.0, "hPa", "2025-11-05T12:00:00Z")
        ));
        List<ReportePromedioDiarioPorSensorDTO> reportes = estadisticaService.obtenerPromedioDiarioPorSensor();
        assertEquals(3, reportes.size());
        ReportePromedioDiarioPorSensorDTO sensor1dia1 = reportes.stream()
                .filter(r -> r.sensorId().equals("sensor_1") && r.fecha().equals("2025-11-05"))
                .findFirst().orElseThrow();
        assertEquals(15.0, sensor1dia1.promedio());
        assertEquals(10.0, sensor1dia1.minimo());
        assertEquals(20.0, sensor1dia1.maximo());
        assertEquals("temperatura", sensor1dia1.tipo());
        assertEquals("C", sensor1dia1.unidad());
        assertEquals(2, sensor1dia1.totalMediciones());
    }

    @Test
    void sensoresMasVariablesTest() {
        mongoTemplate.insertAll(List.of(
                new SensorNormalizado(null, "sensor_1", "temperatura", 10.0, "C", "2025-10-30T12:00:00Z"),
                new SensorNormalizado(null, "sensor_1", "temperatura", 30.0, "C", "2025-10-30T13:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "temperatura", 15.0, "C", "2025-10-30T14:00:00Z"),
                new SensorNormalizado(null, "sensor_2", "temperatura", 16.0, "C", "2025-10-30T15:00:00Z")
        ));
        List<ReporteVariabilidadSensorDTO> reportes = estadisticaService.obtenerLosDiezSensoresMasVariables();
        assertEquals(2, reportes.size());
        assertEquals("sensor_1", reportes.get(0).sensorId());
        assertTrue(reportes.get(0).desviacion() > reportes.get(1).desviacion());
    }

    @AfterEach
    void cleanup() {
        mongoTemplate.dropCollection("epersgeist_normalized");
        espirituService.eliminarTodo();
        mediumService.eliminarTodo();
        ubicacionService.eliminarTodo();
    }

}
