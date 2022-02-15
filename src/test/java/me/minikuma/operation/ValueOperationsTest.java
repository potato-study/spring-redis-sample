package me.minikuma.operation;

import me.minikuma.config.EmbeddedRedisConfig;
import me.minikuma.config.EmbeddedRedisServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@DataRedisTest
@Import({EmbeddedRedisConfig.class, EmbeddedRedisServer.class})
public class ValueOperationsTest {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Test
    @DisplayName("Value Operation Test (기본 문자열)")
    void string_value_operations() {
        // given
        String key = "test1";
        String value = "test-value1";

        // when
        ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);

        Object findValue = operations.get(key);
        log.info("string value operations > key 로 조회된 value : {}", findValue);

        // then
        Assertions.assertThat(value).isEqualTo(findValue);
    }

    @Test
    @DisplayName("Hash Value Operation Test (기본 문자열")
    void string_value_hash_operations() {
        // given
        String key = "test-hash-key1";
        String value = "test-hash-value1";
        String hashKey = "test-hash1";

        // when
        HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, hashKey, value);

        // then
        Object findValue = hashOperations.get(key, hashKey);
        log.info("string hash value operations > key, hash key 로 조회된 value : {}", findValue);

        Assertions.assertThat(findValue).isEqualTo(value);
    }
}
