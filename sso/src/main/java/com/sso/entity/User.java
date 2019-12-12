package com.sso.entity;

import lombok.Data;

/**
 * @ClassName User
 * @Author 吴灿洪
 * @Description
 * @Date 2019/12/6 17:56
 * @Version 1.0
 */
@Data
public class User {

    private Integer id;
    private String username;
    private String password;
    private String role;
}

