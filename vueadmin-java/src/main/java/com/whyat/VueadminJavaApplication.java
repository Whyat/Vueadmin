package com.whyat;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.whyat.mapper")
public class VueadminJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueadminJavaApplication.class, args);
    }

}
