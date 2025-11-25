package ar.edu.unq.epersgeist.messaging;

import org.springframework.stereotype.Service;

@Service
public class RealtimeListener {

    private final SseEmitterService emitterService;

    public RealtimeListener(SseEmitterService emitterService) {
        this.emitterService = emitterService;
    }


    public void handleMessage(Object mensaje, String canal) {
        try {
            String[] partes = canal.split(":");
            String ubicacionId = partes[1];

            emitterService.send(ubicacionId, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

