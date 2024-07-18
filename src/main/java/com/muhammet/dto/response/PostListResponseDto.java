package com.muhammet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostListResponseDto {
    Long userId;
    String userName;
    String avatar;
    String photo;
    String comment;
    Long likeCount;
    Long commentCount;
    Long sharedDate;
}
