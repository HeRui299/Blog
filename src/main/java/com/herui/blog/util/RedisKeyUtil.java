package com.herui.blog.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX = "bl";

    // 某个帖子的赞
    public static String getBlogLikeKey(int blogId){
        return PREFIX + SPLIT + blogId;
    }

}