package com.example.mybaitsmapper;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author huc
 */
@MapperScan(basePackageClasses = MybaitsMapperApplication.class)
@SpringBootApplication
@Slf4j
public class MybaitsMapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybaitsMapperApplication.class, args);
    }

}
