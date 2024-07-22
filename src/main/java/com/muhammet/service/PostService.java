package com.muhammet.service;

import com.muhammet.config.JwtManager;
import com.muhammet.dto.request.CreatePostRequestDto;
import com.muhammet.dto.response.CommentResponseDto;
import com.muhammet.dto.response.PostListResponseDto;
import com.muhammet.entity.Like;
import com.muhammet.entity.Post;
import com.muhammet.entity.User;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.repository.PostRepository;
import com.muhammet.views.VwUserAvatar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final UserService userService;
    private final JwtManager jwtManager;
    private final CommentService commentService;
    public void createPost(CreatePostRequestDto dto) {
        Optional<Long> userId = jwtManager.getAuthId(dto.getToken());
        if(userId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        repository.save(Post.builder()
                .comment(dto.getComment())
                .commentCount(0L)
                .likeCount(0L)
                .photo(dto.getUrl())
                .sharedDate(System.currentTimeMillis())
                .userId(userId.get())
                .build());
    }

    public List<PostListResponseDto> getPostList(String token) {
        Optional<Long> userId = jwtManager.getAuthId(token);
        if(userId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        List<Post> postList = repository.findAll();
        List<PostListResponseDto> result = new ArrayList<>();
        List<Long> userIds = postList.stream().map(Post::getUserId).toList();
        List<Long> postIds = postList.stream().map(Post::getId).toList();
        Map<Long, User> mapUserList = userService.findAllByIdsMap(userIds);
        Map<Long,List<CommentResponseDto>> commentList = commentService.getAllCommentListByPostIds(postIds);
        //List<VwUserAvatar> userAvatarList = userService.getUserAvatarList(); // 20K+
        postList.forEach(p->{
           // VwUserAvatar userAvatar = userAvatarList.stream().filter(x-> x.getId().equals(p.getUserId())).findFirst().get();
            result.add(PostListResponseDto.builder()
                    .postId(p.getId())
                    .avatar(mapUserList.get(p.getUserId()).getAvatar())
                    .userName(mapUserList.get(p.getUserId()).getUserName())
                    .userId(p.getUserId())
                    .sharedDate(p.getSharedDate())
                    .comment(p.getComment())
                    .commentCount(p.getCommentCount())
                    .likeCount(p.getLikeCount())
                    .photo(p.getPhoto())
                    .commentList(commentList.get(p.getId()))
                    .build());
        });

        return result.stream().sorted(Comparator.comparingLong(PostListResponseDto::getSharedDate)).toList().reversed();
    }

    public void addComment(Long postId) {
        Optional<Post> postOptional = repository.findById(postId);
        if (postOptional.isPresent()){
            Post post = postOptional.get();
            if (post.getCommentCount()!=null)
                post.setCommentCount(post.getCommentCount()+1);
            else
                post.setCommentCount(1L);
        }
    }
}

