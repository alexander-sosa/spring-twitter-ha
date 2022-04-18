package bo.edu.ucb.arq.twitter.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

public class LoggingRedis<K, V> extends RedisTemplate<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(LoggingRedis.class);

    @Override
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
        try{
            return super.execute(action, exposeConnection, pipeline);
        }
        catch (final Throwable t){
            logger.warn("Error executing cache operation: {}", t.getMessage());
            return null;
        }
    }

    @Override
    public <T> T execute(final RedisScript<T> script, final List<K> keys, final Object... args) {
        try {
            return super.execute(script, keys, args);
        }
        catch(final Throwable t) {
            logger.warn("Error executing cache operation: {}", t.getMessage());
            return null;
        }
    }

    @Override
    public <T> T execute(final RedisScript<T> script, final RedisSerializer<?> argsSerializer, final RedisSerializer<T> resultSerializer, final List<K> keys, final Object... args) {
        try {
            return super.execute(script, argsSerializer, resultSerializer, keys, args);
        }
        catch(final Throwable t) {
            logger.warn("Error executing cache operation: {}", t.getMessage());
            return null;
        }
    }

    @Override
    public <T> T execute(final SessionCallback<T> session) {
        try {
            return super.execute(session);
        }
        catch(final Throwable t) {
            logger.warn("Error executing cache operation: {}", t.getMessage());
            return null;
        }
    }
}
