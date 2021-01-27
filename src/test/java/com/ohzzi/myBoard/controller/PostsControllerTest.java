package com.ohzzi.myBoard.controller;

import com.ohzzi.myBoard.controller.dto.PostsResponseDto;
import com.ohzzi.myBoard.controller.dto.PostsSaveRequestDto;
import com.ohzzi.myBoard.controller.dto.PostsUpdateRequestDto;
import com.ohzzi.myBoard.domain.posts.Posts;
import com.ohzzi.myBoard.domain.posts.PostsRepository;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    void 게시글_등록() {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    void 게시글_수정() throws Exception {
        //given
        String title = "title";
        String content = "content";
        Posts savedPost = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("author")
                .build());

        Long id = savedPost.getId();
        String expectedTitle = "updated title";
        String expectedContent = "updated content";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    void 게시글_삭제() throws Exception {
        //given
        String title = "title";
        String content = "content";
        Posts savedPost = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("author")
                .build());

        Long id = savedPost.getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        HttpEntity requestEntity = new HttpEntity(new HttpHeaders());
        ResponseEntity<Long> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        assertThat(postsRepository.findById(id)).isEmpty();

    }

    @Test
    void 게시글_조회() throws Exception {
        //given
        String title = "title";
        String content = "content";
        Posts savedPost = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("author")
                .build());

        Long id = savedPost.getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + id;

        //when
        String body = testRestTemplate.getForObject(url, String.class);
        //ResponseEntity<Long> responseEntity = testRestTemplate.getForEntity(url, Long.class);

        //then
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).contains(title);
        assertThat(body).contains(content);
        assertThat(postsRepository.findById(id).get().getViewCount()).isEqualTo(1);
    }
}