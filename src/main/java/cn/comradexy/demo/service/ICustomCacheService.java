package cn.comradexy.demo.service;

import cn.comradexy.demo.model.domain.User;

import java.util.List;

/**
 * service demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: service demo
 */
public interface ICustomCacheService {
    User getUserByUserName(String userName);

    List<User> getUserByName(String name);

    List<User> listUser();

}
