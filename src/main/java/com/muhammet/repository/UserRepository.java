package com.muhammet.repository;


import com.muhammet.entity.User;
import com.muhammet.views.VwUserAvatar;
import com.muhammet.views.VwUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findOptionalByUserNameAndPassword(String userName, String password);

    List<User> findAllByUserNameContaining(String userName);

    @Query("select new com.muhammet.views.VwUserProfile(u.name,u.userName,u.avatar,u.followerCount,u.followingCount,u.about,u.bornDate,u.phone,u.address) from User u where u.id=?1")
    VwUserProfile getByAuthId(Long id);

    @Query("select new com.muhammet.views.VwUserAvatar(u.id,u.userName,u.avatar) from User u where u.id=?1")
    VwUserAvatar getUserAvatar(Long id);

    @Query("select new com.muhammet.views.VwUserAvatar(u.id,u.userName,u.avatar) from User u ")
    List<VwUserAvatar> getUserAvatarList();
}
