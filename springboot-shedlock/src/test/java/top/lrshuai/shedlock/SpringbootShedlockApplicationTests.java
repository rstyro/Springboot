package top.lrshuai.shedlock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

@SpringBootTest
class SpringbootShedlockApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
        TestObject testObject = TestObject.builder()
                .id("100")
                .age(18)
                .name("rstyro")
                .createTime(LocalDateTime.now())
                .build();
        redisTemplate.opsForValue().set("test", testObject);
        TestObject obj = (TestObject) redisTemplate.opsForValue().get("test");
        System.out.println("obj="+obj);
        System.out.println("obj="+obj.toString());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestObject {
        private String id;
        private String name;
        private int age;
        private LocalDateTime createTime;
    }

}
