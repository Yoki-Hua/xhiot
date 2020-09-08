package com.xhwl.service;

import com.xhwl.pojo.DTO.UserDto;
import com.xhwl.pojo.User;

public interface UserService {
    void register(User user, String code);

    UserDto queryUserByUserNameAndPass(String userName,String passWord);

    User queryUserByUserName(String username);
}
