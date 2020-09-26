package com.herui.blog.service.Impl;
import com.herui.blog.dao.BlogRepository;
import com.herui.blog.exception.NotFoundException;
import com.herui.blog.pojo.Blog;
import com.herui.blog.pojo.Type;
import com.herui.blog.service.BlogService;
import com.herui.blog.util.MarkdownUtils;
import com.herui.blog.util.MyBeanUtils;
import com.herui.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.*;
import java.util.*;
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog b = blogRepository.getOne(id);
        if (b == null){
            throw new NotFoundException("该博客不存在");
        }
        Blog a = new Blog();
        BeanUtils.copyProperties(b,a);
        a.setContent(MarkdownUtils.markdownToHtmlExtensions(b.getContent()));
        blogRepository.updateViews(id);
        return a;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
       return blogRepository.findAll(new Specification<Blog>(){
           @Override
           public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
               List<Predicate> predicates = new ArrayList<>();
               if (!"".equals(blog.getTitle()) && blog.getTitle()!=null){
                   predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
               }
               if (blog.getTypeId()!=null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
               }
               if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<String>get("recommend"),blog.isRecommend()));
               }
               cq.where(predicates.toArray(new Predicate[predicates.size()]));
               return null;
           }
       },pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId()==null) {
            blog.setViews(0);
            blog.setCreatTime(new Date());
            blog.setUpdateTime(new Date());
        }else{
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b==null){
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        // 先查询有哪些年
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for(String year:years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findByQuery("%"+query+"%",pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Long id) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),id);
            }
        },pageable);
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}