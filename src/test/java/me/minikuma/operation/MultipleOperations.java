package me.minikuma.operation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

public class MultipleOperations extends BaseTest {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Test
    void 멀티_다건_등록() {
        // given
        String key = "multiple-key";
        redisTemplate.opsForValue().set(key, 10);
        Assertions.assertThat(redisTemplate.opsForValue().get(key)).isEqualTo(10);

        // when
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                // multiple logic
                for (int i = 0; i < 4; i++) {
                    redisTemplate.opsForValue().increment(key, 1);
                }
                return null;
            }
        });

        // then
        int totalCount = (int) redisTemplate.opsForValue().get(key);
        Assertions.assertThat(totalCount).isEqualTo(14);
    }
}
