package ar.edu.unq.epersgeist.config;

import ar.edu.unq.epersgeist.messaging.RealtimeListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPubSubConfig {

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(listenerAdapter, new PatternTopic("ubicacion:*:espiritus"));
        container.addMessageListener(listenerAdapter, new PatternTopic("ubicacion:*:mediums"));

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RealtimeListener listener) {
        return new MessageListenerAdapter(listener, "handleMessage");
    }
}
