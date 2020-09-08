package com.xhwl.service.impl;

import com.xhwl.commons.enumPackage.ExceptionEnum;
import com.xhwl.commons.exceptions.XhException;
import com.xhwl.commons.utils.BeanHelper;
import com.xhwl.mapper.UserMapper;
import com.xhwl.pojo.DTO.UserDto;
import com.xhwl.pojo.User;
import com.xhwl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public User queryUserByUserName(String username) {
        User user = new User();
        user.setUsername(username);
       User select = userMapper.selectByPrimaryKey(user);
        if (select == null) {
            throw  new XhException(ExceptionEnum.INVALID_USERNAME);
        }
        return select;
    }

    @Override
    public UserDto queryUserByUserNameAndPass(String userName, String passWord) {
        User user = new User();
        user.setUsername(userName);
        User selectOne = userMapper.selectOne(user);
        //判断用户是否为空，验密，先要更具加密后的密码推断盐值，然后拿着盐值和密码 的明文，加密次数，对输入的明文密码加密，得到新的密码然后比较
        if (selectOne == null || !passwordEncoder.matches(passWord, selectOne.getPassword())) {
            throw new XhException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        return BeanHelper.copyProperties(selectOne, UserDto.class);

    }

    @Override
    public void register(User user, String code) {
        String key = user.getEmail();
        //校验验证码
        if (!redisTemplate.hasKey(key) || !redisTemplate.opsForValue().get(key).equals(code)) {

            throw new XhException(ExceptionEnum.INVALID_EMAIL);

        }
        //对密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //写进数据库，回显操作条数
        int insert = userMapper.insert(user);
        if (insert != 1) {
            throw new XhException(ExceptionEnum.DATA_SAVE_ERROR);
        }
        //用户注册完成后，应该删除redis中存放的验证码，
        //判断这个对应的key是否还有效，然后，如果有效，则判断过期时间是否大于3秒
        if (redisTemplate.hasKey(key) && redisTemplate.getExpire(key, TimeUnit.SECONDS) > 3) {
            redisTemplate.delete(key);
        }

    }
}
