## Spring Redis Sample

---

### Introduce

Redis 를 스프링에서 어떻게 지원하고 있는 지 살펴보고 실제 사용의 예를 테스트 코드 작성(TDD) 을 통해 알아보도록 한다. (Spring Redis 2.6.1 기준)

### 실행 방법
```./gradlew test```    

### Tech. Stacks
* Embedded Redis
* Spring Boot 2.6.3
* H2 Database (In-Memory)

### Test 수행
* RedisTemplate Operations
  * Object - opsForValue
  * Object(Hash) - opsForHash
  * Object(List) - opsForList
  * Object(sorted set) - opsForZSet


### Feature  
* Connector 변화: Spring Redis 2.6+ 부터는 Lettuce, Jedis Connector 사용

---    

여러 가지 Redis Operations 검증을 위해 지속적으로 테스트 코드를 작성해서 올릴 예정입니다.

