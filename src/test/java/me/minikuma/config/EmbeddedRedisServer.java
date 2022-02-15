package me.minikuma.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class EmbeddedRedisServer {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RedisServer redisServer;

    public EmbeddedRedisServer(@Value("${spring.redis.port}") int port) {
        this.redisServer = new RedisServer(port);
        log.info("Create Embedded Redis Server Ready >>>> ");
    }

    @PostConstruct
    public void start() {
        redisServer.start();
        log.info("Embedded Redis Server Start >>>> ");
    }

    @PreDestroy
    public void stop() {
        redisServer.stop();
        log.info("Embedded Redis Server Shutdown >>>> ");
    }
}
