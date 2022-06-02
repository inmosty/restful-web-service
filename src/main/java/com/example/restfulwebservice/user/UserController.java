package com.example.restfulwebservice.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private UserDaoService service;

    public UserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        List<User> user = service.findAll();
        return user;
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id){
        User user = service.findOne(id);

        if(user == null){
            log.info("user is null");
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }
        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = service.save(user);


        // 사용자에게 요청 값을 변환해주기
        // fromCurrentRequest() :현재 요청되어진 request값을 사용한다는 뜻
        // path : 반환 시켜줄 값
        // savedUser.getId() : {id} 가변변수에 새롭게 만들어진 id값 저장
        // toUri() : URI형태로 변환
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        log.info("location = {}",location);
        ResponseEntity<User> build = ResponseEntity.created(location).build();
        log.info("build = {}",build);
        return build;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        User user = service.deleteById(id);

        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

    }

}
