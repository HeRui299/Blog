package com.herui.blog.dao;

import com.herui.blog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {
    // 最新推荐博客
    @Query("select b from Blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    // 根据标题或者内容模糊查询
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);

    @Query("SELECT function('DATE_FORMAT',b.updateTime,'%Y') as year from Blog b GROUP BY year ORDER BY function('date_format',b.updateTime,'%Y') DESC")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);
    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    void updateViews(Long id);
}