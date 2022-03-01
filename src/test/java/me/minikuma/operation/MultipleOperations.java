package me.minikuma.operation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MultipleOperations extends BaseTest {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Test
    void 멀티_다건_등록() {
        // given
        String key = "multiple-key";
        redisTemplate.opsForValue().set(key, 10);
        assertThat(redisTemplate.opsForValue().get(key)).isEqualTo(10);

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
        assertThat(totalCount).isEqualTo(14);
    }

    @Test
    void 레디스_트랜젝션_처리_등록_테스트() {
        // given
        redisTemplate.opsForValue().set("tx1", 10);
        redisTemplate.opsForSet().add("tx2", "tx2_value");
        assertThat(redisTemplate.opsForSet().isMember("tx2", "30")).isFalse();

        // when
        redisTemplate.execute(new SessionCallback<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.watch((K) "tx1"); // tx1 값 변경 확인
                operations.multi(); // 트랜젝션 시작

                // 작업
                operations.opsForValue().increment((K) "tx1", 1); // tx1 값에 1을 증가 시킴
                operations.opsForSet().add((K) "tx2", (V) "tx2_add_value"); // tx2 키에 추가 값 등록

                return operations.exec(); // 트랜젝션 커밋
            }
        });

        // then
        Object tx1Values = redisTemplate.opsForValue().get("tx1");
        Set<Object> tx2Values = redisTemplate.opsForSet().members("tx2");
        Boolean tx2ValueIsChecked = redisTemplate.opsForSet().isMember("tx2", "tx2_add_value");
        LOG.info("tx1 data = {}", tx1Values);
        LOG.info("tx2 data = {}", tx2Values);

        assertThat(tx1Values).isEqualTo(11);
        assertThat(tx2ValueIsChecked).isTrue();
    }
}
