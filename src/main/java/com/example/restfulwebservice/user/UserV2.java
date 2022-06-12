package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
//@JsonIgnoreProperties(value={"password"})
@JsonFilter("UserInfoV2")
@AllArgsConstructor
@NoArgsConstructor
public class UserV2 extends UserInfo{
    private String grade;

}
