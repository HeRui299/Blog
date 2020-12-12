package com.herui.blog;

import com.herui.blog.util.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class BlogApplicationTests {
    public static void main(String[] args) {
        int sum = 0;
        for (int i = 1; i <= 500; i++) {
            sum+=i;
        }
        System.out.println(sum);
    }
}