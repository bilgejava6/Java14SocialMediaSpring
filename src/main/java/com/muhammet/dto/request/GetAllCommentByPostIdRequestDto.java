package com.muhammet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllCommentByPostIdRequestDto {
    String token;
    Long postId;
    int size = 10;
    int page = 0;
}
