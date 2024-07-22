package com.muhammet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponseDto {
    Long postId;
    Long commentId;
    Long userId;
    String userName;
    String avatar;
    String comment;
    Long date;
}
