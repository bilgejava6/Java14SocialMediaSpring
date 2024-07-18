package com.muhammet.repository;

import com.muhammet.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository  extends JpaRepository<Like,Long> {
}
