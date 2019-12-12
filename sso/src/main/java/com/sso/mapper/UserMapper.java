package com.sso.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserMapper
 * @Author 吴灿洪
 * @Description
 * @Date 2019/12/6 17:57
 * @Version 1.0
 */
@Repository
public interface UserMapper {

    //获取密码
    String getPassword(@Param("username")String username);


    //获取角色权限
    String getRole(@Param("username") String username);

}
