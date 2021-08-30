package com.huakai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
@MapperScan("com.huakai.mapper")
public class App {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Hello World";
    }


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
