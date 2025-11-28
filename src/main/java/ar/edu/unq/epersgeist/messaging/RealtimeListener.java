package ar.edu.unq.epersgeist.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class RealtimeListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RealtimeListener.class);
    private final SseEmitterService emitterService;
    private final RedisSerializer<Object> serializer;

    public RealtimeListener(SseEmitterService emitterService, RedisSerializer<Object> genericSerializer) {
        this.emitterService = emitterService;
        this.serializer = genericSerializer;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String canal = new String(message.getChannel());
            logger.info("Mensaje Redis recibido en canal: {}", canal);
            
            String[] partes = canal.split(":");
            if (partes.length >= 3) {
                String ubicacionId = partes[1];
                
                // Deserializar el mensaje
                Object deserializedMessage = serializer.deserialize(message.getBody());
                
                logger.info("Reenviando mensaje a ubicación {} via SSE: {}", ubicacionId, deserializedMessage);
                emitterService.send(ubicacionId, deserializedMessage);
            } else {
                logger.error("Formato de canal inválido: {}", canal);
            }
        } catch (Exception e) {
            logger.error("Error procesando mensaje de Redis: {}", e.getMessage(), e);
            e.printStackTrace();
        }
    }


    public void handleMessage(Object mensaje, String canal) {
        try {
            logger.info("Mensaje Redis recibido en canal (método handleMessage): {}", canal);
            String[] partes = canal.split(":");
            String ubicacionId = partes[1];
            
            logger.info("Reenviando mensaje a ubicación {} via SSE", ubicacionId);
            emitterService.send(ubicacionId, mensaje);
        } catch (Exception e) {
            logger.error("Error procesando mensaje de Redis: {}", e.getMessage(), e);
            e.printStackTrace();
        }
    }
}

