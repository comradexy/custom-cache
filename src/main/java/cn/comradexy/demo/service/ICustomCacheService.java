package cn.comradexy.demo.service;

import cn.comradexy.demo.model.dto.UserDTO;

import java.util.List;

/**
 * service demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: service demo
 */
public interface ICustomCacheService {
    UserDTO getUserByUserName(String userName);

    List<UserDTO> getUserByName(String name);

    List<UserDTO> listUser();

}
