package com.herui.blog.service;

import com.herui.blog.pojo.Blog;
import com.herui.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

    Page<Blog> listBlog(Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();

    Page<Blog> listBlog(Pageable pageable,String query);

    Page<Blog> listBlog(Pageable pageable, Long id);

    Long countBlog();
}