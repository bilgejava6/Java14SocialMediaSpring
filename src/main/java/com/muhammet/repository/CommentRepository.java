package com.muhammet.repository;

import com.muhammet.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPostIdIn(List<Long> postIds);
}
