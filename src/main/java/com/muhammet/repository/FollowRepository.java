package com.muhammet.repository;

import com.muhammet.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository  extends JpaRepository<Follow,Long> {
}
