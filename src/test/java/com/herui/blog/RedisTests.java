package com.herui.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        redisTemplate.opsForValue().set("test:string",123);

        System.out.println(redisTemplate.opsForValue().get("test:string"));
    }
}