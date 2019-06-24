package com.fgcui.blog.service;

import com.fgcui.blog.po.Comment;
import com.fgcui.blog.repository.CommentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = new Sort(Sort.Direction.ASC, "createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(comments);
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

    public List<Comment> eachComment(List<Comment> comments) {
        ArrayList<Comment> commentsView = new ArrayList<>();

        for (Comment comment : comments ) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }

        combineChildren(commentsView);
        return commentsView;
    }


    private void combineChildren(ArrayList<Comment> commentsView) {
        for (Comment comment: commentsView) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply1 : replys) {
                recursively(reply1);
            }
            comment.setReplyComments(tempReplys);

            tempReplys =  new ArrayList<>();
        }
    }

    private List<Comment> tempReplys = new ArrayList<>();


    private void recursively(Comment comment) {
        tempReplys.add(comment);

        if (comment.getReplyComments().size() > 0) {
            List<Comment> replyComments = comment.getReplyComments();
            for (Comment reply : replyComments) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }
}
