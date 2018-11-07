package com.wsgcya.coder.common.config.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Administrator
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=1900)
public class RedisSessionConfig {
}
