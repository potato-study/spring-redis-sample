package me.minikuma.operation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

public class StringRedisTemplateTest extends BaseTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void 문자열레디스템플릿_콜백_테스트() {
        // given
        String key = "key1";
        String value = "value1";

        // when
        Object object = stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                ((StringRedisConnection) connection).set(key, value);
                return ((StringRedisConnection) connection).get(key);
            }
        });

        // then
        String expectedValue = "value1";
        Assertions.assertThat(object).isEqualTo(expectedValue);
    }
}
