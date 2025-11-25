package ar.edu.unq.epersgeist.messaging;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEmitterService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String ubicacionId) {
        SseEmitter emitter = new SseEmitter(0L); // conexión abierta
        emitters.put(ubicacionId, emitter);

        emitter.onCompletion(() -> emitters.remove(ubicacionId));
        emitter.onTimeout(() -> emitters.remove(ubicacionId));
        emitter.onError((e) -> emitters.remove(ubicacionId));

        return emitter;
    }

    public void send(String ubicacionId, Object data) {
        SseEmitter emitter = emitters.get(ubicacionId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(data));
            } catch (Exception e) {
                emitters.remove(ubicacionId);
            }
        }
    }
}
