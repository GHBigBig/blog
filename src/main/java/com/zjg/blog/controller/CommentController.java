package com.zjg.blog.controller;

import com.zjg.blog.pojo.Comment;
import com.zjg.blog.pojo.User;
import com.zjg.blog.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author zjg
 */
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private static final String COMMENT_LIST = "blog :: commentList";
    private static final String REDIRECT_COMMENT = "redirect:/comments/";

    @Autowired
    private CommentService commentService;
    @Autowired
    private HttpSession session;
    @Value("${blog.comment.portrait}")
    private String portrait;

    @GetMapping("/comments/{blogId}")
    public String blogComment(@PathVariable("blogId")Long blogId, Model model){
        if (blogId>0) {
            List<Comment> comments = commentService.findCommentNotParentByBlogId(blogId);
            commentService.fillReplyComments(comments);
            model.addAttribute("comments", comments);
        }

        return COMMENT_LIST;
    }

    @PostMapping("/comments")
    public String addComment(Comment comment, Model model){
        comment.setCommentCreateTime(new Date());
        //由于评论表有自关联，如果是父评论那么 commentParentId 应为 null
        if (comment.getCommentParentId()==-1) {
            comment.setCommentParentId(null);
        }

        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setCommentPortrait(user.getUserPortrait());
            comment.setCommentIsAdmin(true);
        }else {
            comment.setCommentPortrait(portrait);
            comment.setCommentIsAdmin(false);
        }

        logger.debug("新增的评论: {}", comment);
        commentService.saveComment(comment);

        return REDIRECT_COMMENT + comment.getCommentBlogId();
    }
}
