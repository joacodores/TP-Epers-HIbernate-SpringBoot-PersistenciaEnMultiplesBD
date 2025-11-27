package ar.edu.unq.epersgeist.messaging;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealtimePublisher {

    private static final Logger logger = LoggerFactory.getLogger(RealtimePublisher.class);
    private final RedisTemplate<String, Object> redisTemplate;

    public RealtimePublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publishEspiritu(Long ubicacionId, Espiritu data) {
        String canal = "ubicacion:" + ubicacionId + ":espiritus";
        logger.info("Publicando espíritu {} a Redis en canal: {}", data.getNombre(), canal);
        try {
            redisTemplate.convertAndSend(canal, data);
            logger.info("Espíritu publicado exitosamente a Redis");
        } catch (Exception e) {
            logger.error("Error publicando a Redis: {}", e.getMessage(), e);
        }
    }
}
