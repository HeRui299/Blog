package com.herui.blog.service.Impl;
import com.herui.blog.dao.CommentRepository;
import com.herui.blog.pojo.Comment;
import com.herui.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by limi on 2017/10/22.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        // 排序根据createTime，默认升序
        Sort sort =  Sort.by("createTime");
        // 查询所有的父级评论，也就是查询数据库中parent_comment_id中为null的数据
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        // 调用eachComment方法，把父级评论传进去
        return eachComment(comments);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        // 复制一个新的顶级评论列表
        List<Comment> commentsView = new ArrayList<>();

        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) { // 循环遍历父级评论
            // 获取每个父级评论的子评论
            List<Comment> replys1 = comment.getReplayComments();
            // 循环遍历每个子评论
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplayComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//将回复评论添加到临时存放集合
        if (comment.getReplayComments().size()>0) { // 如果有回复评论中又有子评论,走进代码块,防空指针
            List<Comment> replys = comment.getReplayComments(); // 获取回复评论的子评论，在页面上就相当于，你回复了博主，小白又回复了你
            for (Comment reply : replys) { // 循环遍历子评论，添加到tempReplay集合中(由于页面只有两级评论，所以把子级评论，和子级的子级评论放在一个list中，也就是同一层了)
                tempReplys.add(reply); // 添加评论到集合中
                if (reply.getReplayComments().size()>0) { // 递归去查找当前的评论是否有子评论，有就继续找，没有就不找了
                    recursively(reply);
                }
            }
        }
    }


    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
}