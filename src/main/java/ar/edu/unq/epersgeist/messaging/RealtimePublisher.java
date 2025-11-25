package ar.edu.unq.epersgeist.messaging;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealtimePublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public RealtimePublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publishEspiritus(Long ubicacionId, List<Espiritu> data) {
        redisTemplate.convertAndSend("ubicacion:" + ubicacionId + ":espiritus", data);
    }
}
