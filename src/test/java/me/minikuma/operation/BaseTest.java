package me.minikuma.operation;

import me.minikuma.config.EmbeddedRedisConfig;
import me.minikuma.config.EmbeddedRedisServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;

@DataRedisTest
@Import({EmbeddedRedisConfig.class, EmbeddedRedisServer.class})
public class BaseTest {
    final Logger LOG = LoggerFactory.getLogger(this.getClass());
}
