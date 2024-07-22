package com.muhammet.service;

import com.muhammet.config.JwtManager;
import com.muhammet.dto.request.AddCommentRequestDto;
import com.muhammet.dto.response.CommentResponseDto;
import com.muhammet.entity.Comment;
import com.muhammet.entity.Like;
import com.muhammet.entity.User;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final JwtManager jwtManager;
    private final UserService userService;
    public void addComment(AddCommentRequestDto dto) {
        Optional<Long> userId =  jwtManager.getAuthId(dto.getToken());
        if(userId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        repository.save(Comment.builder()
                        .comment(dto.getComment())
                        .date(System.currentTimeMillis())
                        .postId(dto.getPostId())
                        .userId(userId.get())
                .build());
    }

    public HashMap<Long, List<CommentResponseDto>> getAllCommentListByPostIds(List<Long> postIds){
        List<Comment> comments = repository.findAllByPostIdIn(postIds);
        List<Long> userIds = comments.stream().map(Comment::getUserId).toList();
        Map<Long, User> userMap = userService.findAllByIdsMap(userIds);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        comments.forEach(c->{
            commentResponseDtos.add(
                    CommentResponseDto.builder()
                            .postId(c.getPostId())
                            .avatar(userMap.get(c.getUserId()).getAvatar())
                            .comment(c.getComment())
                            .commentId(c.getId())
                            .date(c.getDate())
                            .userId(c.getUserId())
                            .userName(userMap.get(c.getUserId()).getUserName())
                            .build()
            );
        });
        return commentResponseDtos.stream().collect(Collectors.groupingBy(
           CommentResponseDto::getPostId,
           HashMap::new,
           Collectors.toList()
        ));
    }
}
