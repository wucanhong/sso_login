package com.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName SpringSSOBoot
 * @Author 吴灿洪
 * @Description
 * @Date 2019/12/6 16:58
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan(value = "com.sso.mapper")
public class SpringSSOBoot {

    public static void main(String[] args) {
        SpringApplication.run(SpringSSOBoot.class, args);
    }
}


