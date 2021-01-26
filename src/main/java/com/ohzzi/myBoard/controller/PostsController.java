package com.ohzzi.myBoard.controller;

import com.ohzzi.myBoard.controller.dto.PostsResponseDto;
import com.ohzzi.myBoard.controller.dto.PostsSaveRequestDto;
import com.ohzzi.myBoard.controller.dto.PostsUpdateRequestDto;
import com.ohzzi.myBoard.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long writePost(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.createPost(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.updatePost(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto readPost(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long deletePost(@PathVariable Long id) {
        return postsService.deletePost(id);
    }
}
