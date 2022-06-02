package com.example.restfulwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestfulWebServiceApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(RestfulWebServiceApplication.class, args);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("e = " + e);
        }
    }

}
