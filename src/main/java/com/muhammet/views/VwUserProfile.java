package com.muhammet.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VwUserProfile {
    String name;
    String userName;
    String avatar;
    Long followerCount;
    Long followingCount;
    String about;
    Integer bornDate;
    String phone;
    String address;
}
