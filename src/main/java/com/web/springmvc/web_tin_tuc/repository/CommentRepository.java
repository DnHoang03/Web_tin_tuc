package com.web.springmvc.web_tin_tuc.repository;

import com.web.springmvc.web_tin_tuc.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByNewsId(int NewsId);
}
