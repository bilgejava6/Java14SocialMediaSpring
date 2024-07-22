package com.muhammet.controller;

import com.muhammet.dto.request.AddCommentRequestDto;
import com.muhammet.dto.response.ResponseDto;
import com.muhammet.service.CommentService;
import com.muhammet.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST,RequestMethod.GET})
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    @PostMapping("/add-comment")
    public ResponseEntity<ResponseDto<Boolean>> addComment(@RequestBody AddCommentRequestDto dto){
        commentService.addComment(dto);
        postService.addComment(dto.getPostId());
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().data(true).code(200).message("ok").build());
    }
}
