package com.herui.blog;

import com.herui.blog.util.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class BlogApplicationTests {
    public static void main(String[] args) {
        String zhouxiaoting = MD5Utils.code("zhouxiaoting");
        System.out.println(zhouxiaoting);
    }
}