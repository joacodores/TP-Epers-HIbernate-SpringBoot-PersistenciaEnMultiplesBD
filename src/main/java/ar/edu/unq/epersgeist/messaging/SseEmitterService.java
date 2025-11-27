package ar.edu.unq.epersgeist.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseEmitterService {

    private static final Logger logger = LoggerFactory.getLogger(SseEmitterService.class);
    // Map: ubicacionId -> List de emitters
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
    // Map: emitter -> ubicacionId (para cleanup)
    private final Map<SseEmitter, String> emitterToUbicacion = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String ubicacionId) {
        SseEmitter emitter = new SseEmitter(0L); // conexión abierta indefinidamente
        // Agregar este emitter a la lista de la ubicación
        emitters.computeIfAbsent(ubicacionId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitterToUbicacion.put(emitter, ubicacionId);
        logger.info("Cliente conectado a SSE para ubicación {}. Total clientes: {}",
                ubicacionId, emitters.get(ubicacionId).size());
        // Callbacks para limpiar cuando se cierra la conexión
        emitter.onCompletion(() -> removeEmitter(emitter));
        emitter.onTimeout(() -> removeEmitter(emitter));
        emitter.onError((e) -> removeEmitter(emitter));
        return emitter;
    }

    private void removeEmitter(SseEmitter emitter) {
        String ubicacionId = emitterToUbicacion.remove(emitter);
        if (ubicacionId != null) {
            List<SseEmitter> list = emitters.get(ubicacionId);
            if (list != null) {
                list.remove(emitter);
                logger.info("Cliente desconectado de SSE para ubicación {}. Clientes restantes: {}",
                        ubicacionId, list.size());
                if (list.isEmpty()) {
                    emitters.remove(ubicacionId);
                }
            }
        }
    }

    public void send(String ubicacionId, Object data) {
        List<SseEmitter> emitterList = emitters.get(ubicacionId);
        if (emitterList != null && !emitterList.isEmpty()) {
            logger.info("Enviando mensaje SSE a {} clientes de ubicación {}", emitterList.size(), ubicacionId);
            // Enviar a todos los clientes conectados a esta ubicación
            int sentCount = 0;
            for (SseEmitter emitter : emitterList) {
                try {
                    emitter.send(SseEmitter.event().data(data));
                    sentCount++;
                } catch (Exception e) {
                    logger.error("Error enviando SSE a cliente de ubicación {}: {}", ubicacionId, e.getMessage());
                    removeEmitter(emitter);
                }
            }
            logger.info("Mensaje SSE enviado exitosamente a {} clientes de ubicación {}", sentCount, ubicacionId);
        } else {
            logger.warn("No hay clientes SSE conectados para ubicación {}", ubicacionId);
        }
    }

}
