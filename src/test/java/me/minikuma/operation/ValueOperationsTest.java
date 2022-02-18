package me.minikuma.operation;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueOperationsTest extends BaseTest {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Test
    void string_value_operations_기본_문자열_테스트() {
        // given
        String key = "test1";
        String value = "test-value1";

        // when
        ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);

        Object findValue = operations.get(key);
        log.info("string value operations > key 로 조회된 value : {}", findValue);

        // then
        assertThat(value).isEqualTo(findValue);
    }

    @Test
    void string_value_hash_operations_기본_해시_문자열() {
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

        assertThat(findValue).isEqualTo(value);
    }

    @Test
    void 레디스_리스트_조작_테스트() {
        // given
        String masterKey = "master";

        // when
        ListOperations<Object, Object> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(masterKey, "G");
        listOperations.rightPush(masterKey, "O");
        listOperations.rightPush(masterKey, "O");
        listOperations.rightPush(masterKey, "D");

        List<String> expectedValue = Arrays.asList("G", "O", "O", "D");

        // then
        assertThat(listOperations.size(masterKey)).isEqualTo(4);
        assertThat(listOperations.range(masterKey, 0, 3)).isEqualTo(expectedValue);
    }

    @Test
    void 정렬된_set_조작_테스트() {
        // given
        String masterKey = "SCORE";

        // when
        ZSetOperations<Object, Object> zSet = redisTemplate.opsForZSet();
        zSet.add(masterKey, "수학", 100);
        zSet.add(masterKey, "언어", 10);
        zSet.add(masterKey, "문학", 9);
        zSet.add(masterKey, "과학", 99);

        // then
        Set<Object> score = zSet.range(masterKey, 0, 3);

        // 문학 언어 과학 수학
        assertThat(score.size()).isEqualTo(4);
        assertThat(score.toArray()[0]).isEqualTo("문학");
    }

    @Test
    void 멀티_실행_테스트() {

    }
}
