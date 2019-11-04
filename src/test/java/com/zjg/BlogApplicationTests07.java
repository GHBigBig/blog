package com.zjg;

import com.zjg.blog.pojo.Comment;
import com.zjg.blog.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author zjg
 * @create 2019-10-28 22:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests07 {

    @Autowired
    private CommentService commentService;

    //获取博客下的父评论
    @Test
    public void test01() {
        List<Comment> commentByBlogId = commentService.findCommentNotParentByBlogId(1l);
        System.out.println(commentByBlogId);
    }

    //填充子评论
    @Test
    public void test02() {
        List<Comment> commentByBlogId = commentService.findCommentNotParentByBlogId(1l);
        commentService.fillReplyComments(commentByBlogId);
        for (Comment comment : commentByBlogId) {
            System.out.println("子评论 : " + comment.getReplyComments().size());
            System.out.println(comment);
            if (comment.getReplyComments().size()>0) {
                for (Comment replyComment : comment.getReplyComments()) {
                    System.out.println(replyComment);
                }
            }
            System.out.println();
        }
    }
}
