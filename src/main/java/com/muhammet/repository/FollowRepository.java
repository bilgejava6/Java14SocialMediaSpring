package com.muhammet.repository;

import com.muhammet.entity.Follow;
import com.muhammet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository  extends JpaRepository<Follow,Long> {
    Optional<Follow> findOptionalByUserIdAndFollowId(Long userId, Long followId);
}
