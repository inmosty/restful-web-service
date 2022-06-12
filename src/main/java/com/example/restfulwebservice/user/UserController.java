package com.example.restfulwebservice.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@Data
public class UserController {

    private UserDaoService service;

    public UserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public List<UserInfo> retrieveAllUsers(){
        List<UserInfo> user = service.findAll();
        return user;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel<UserInfo>> retrieveUser(@PathVariable int id){
        UserInfo user = service.findOne(id);

        if(user == null){
            log.info("user is null");
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        // HATEOAS
        EntityModel entityModel = EntityModel.of(user);

        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkTo.withRel("all-users"));
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping("/users")
    public ResponseEntity<UserInfo> createUser(@Valid @RequestBody UserInfo user){
        UserInfo savedUser = service.save(user);


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
        ResponseEntity<UserInfo> build = ResponseEntity.created(location).build();
        log.info("build = {}",build);
        return build;
    }

    @GetMapping("/users2")
    public ResponseEntity<CollectionModel<EntityModel<UserInfo>>> retrieveUserList2() {
        List<EntityModel<UserInfo>> result = new ArrayList<>();
        List<UserInfo> users = service.findAll();

        for (UserInfo user : users) {
            EntityModel entityModel = EntityModel.of(user);
            entityModel.add(linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel());

            result.add(entityModel);
        }

        return ResponseEntity.ok(CollectionModel.of(result, linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel()));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        UserInfo user = service.deleteById(id);

        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

    }

}
