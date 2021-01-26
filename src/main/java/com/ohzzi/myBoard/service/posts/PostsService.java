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

    // Create
    @Transactional
    public Long createPost(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // Update
    @Transactional
    public Long updatePost(Long id, PostsUpdateRequestDto requestDto) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + id));
        post.updatePost(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    // Delete
    @Transactional
    public Long deletePost(Long id) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + id));
        postsRepository.delete(post);
        // 존재하는 게시물인지 확인하기 위해서 findById 한 이후 포스트 자체를 삭제
        
        return id;
    }

    // Read
    public PostsResponseDto findById(Long id) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + id));
        return new PostsResponseDto(post);
    }
}
