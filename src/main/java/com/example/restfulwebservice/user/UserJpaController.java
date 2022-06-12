package com.example.restfulwebservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/*
HATEOAS는 RESTful API 구현의 최종 단계라 보시면 됩니다. 각각의 API 요청에 대한 처리를 한 다음,
해당 리소스에 대한 상태를 나타낼 수 있는 링크(Hypermedia)를 추가해 줌으로써,
클라이언트에서는 한번의 요청으로 해당 리소스에서 할수 있는 다양한 정보를 같이 얻을 수 있게 됩니다.
 예를 들어, 회원 가입 후에, 상세보기를 위해 어디로 이동하면 되는지에 대한 정보와 전체 회원목록 정보를 같이 얻을 수 있게 됩니다.
ResponseEntity는 URI를 반환한다는 의미보다는, 요청한 클라이언트에게 리소스의 값이나 추가 헤더정보를 전달해주는 객체입니다.
*/

@RestController
@RequestMapping("/jpa")
public class UserJpaController {
    //@Autowired
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    public UserJpaController(UserRepository userRepository,PostRepository postRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/users")
    public List<UserInfo> retrieveAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public UserInfo retrieveUser(int id){
        Optional<UserInfo> user = userRepository.findById(id);
        if(user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

       /* EntityModel<User> userModel = new EntityModel<>(user.get());
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        userModel.add(linkTo.withRel("all-users"));
      */
        return user.get();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<UserInfo> createUser(@Valid @RequestBody UserInfo user){
        UserInfo savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id){
        Optional<UserInfo> user = userRepository.findById(id);
        if(user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }
        return user.get().getPosts();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id,@RequestBody Post post){
        Optional<UserInfo> user = userRepository.findById(id);
        if(user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        post.setUser(user.get());

        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
