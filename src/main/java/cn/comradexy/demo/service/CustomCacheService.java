package cn.comradexy.demo.service;

import cn.comradexy.demo.mapper.UserMapper;
import cn.comradexy.demo.model.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * service demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: service demo
 */
@Service
public class CustomCacheService implements ICustomCacheService {

    @Resource
    private UserMapper userMapper;

    public User getUserByUserName(String userName) {
        return userMapper.queryByUsername(userName);
    }

    public List<User> getUserByName(String name) {
        return userMapper.queryByName(name);
    }

    public List<User> listUser() {
        return userMapper.queryAll();
    }

}
