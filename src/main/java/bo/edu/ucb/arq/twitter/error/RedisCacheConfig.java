package bo.edu.ucb.arq.twitter.error;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;

public class RedisCacheConfig extends CachingConfigurerSupport implements CachingConfigurer {
    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }
}
