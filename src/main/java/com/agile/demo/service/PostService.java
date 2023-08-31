package com.agile.demo.service;

import com.agile.demo.dto.PostDto;
import com.agile.demo.model.PostEntity;
import com.agile.demo.persistence.PostReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {


    @Autowired
    PostReposity postReposity;

    public PostEntity createPost(PostDto postDto){
        PostEntity postEntity = new PostEntity();
        postEntity.setNumber(postDto.getNumber());
        postEntity.setTitle(postDto.getTitle());
        postEntity.setContext(postDto.getContext());
        postEntity.setWriter(postDto.getWriter());
        postEntity.setPassword(postDto.getPassword());

        return postReposity.save(postEntity);
    }

    public List<PostEntity> getAllPost() {
        List<PostEntity> postEntities = postReposity.findByDeleteAtIsNull(); // delete가 null인 경우만 출력하기

        return postEntities;
    }

    public Optional<PostEntity> getOnePost(Long number) {
        Optional<PostEntity> postEntities = postReposity.findById(number); // delete가 null인 경우만 출력하기

        return postEntities;
    }

    public List<PostEntity> searchPost(String text) {
        List<PostEntity> postEntities = postReposity.findByDeleteAtIsNullAndTitle(text); // delete가 null인 경우만 출력하기

        return postEntities;
    }

    public PostEntity updatePost(PostDto postDto, Long number){
        //1. 비밀번호가 일치하는지 확인
        PostEntity postEntity= postReposity.getById(number);
        System.out.println(postEntity.toString());
        if(postEntity.getPassword().equals(postDto.getPassword()))
        {
        //2. 내용을 수정함
        PostEntity postEntity1 = postReposity.getById(number);
        postEntity1.setTitle(postDto.getTitle());
        postEntity1.setContext(postDto.getContext());
        postEntity1.setWriter(postDto.getWriter());
        postEntity1.setPassword(postDto.getPassword());

        return postReposity.save(postEntity1);
        }
        return null;
    }

    public PostEntity deletePost(PostDto postDto,Long number){
        //1. 비밀번호가 일치하는지 확인 
        PostEntity postEntity= postReposity.getById(number);
        if(postEntity.getPassword().equals(postDto.getPassword())) {

            //2. deleteAt에 삭제 날짜 넣기
            PostEntity postEntity1 = postReposity.getById(number);
            postEntity1.setDeleteAt(LocalDateTime.now());
            return postReposity.save(postEntity1);
            
            //3. 댓글도 같이 삭제 - 날짜 기입
        }
        return null;
    }
}
