package com.ohzzi.myBoard.service.posts;

import com.ohzzi.myBoard.controller.dto.PostsResponseDto;
import com.ohzzi.myBoard.controller.dto.PostsSaveRequestDto;
import com.ohzzi.myBoard.controller.dto.PostsUpdateRequestDto;
import com.ohzzi.myBoard.domain.posts.Posts;
import com.ohzzi.myBoard.domain.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long createPost(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long updatePost(Long id, PostsUpdateRequestDto requestDto) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + id));
        post.updatePost(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + id));
        return new PostsResponseDto(post);
    }
}
