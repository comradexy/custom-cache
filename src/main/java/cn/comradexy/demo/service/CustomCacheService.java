package cn.comradexy.demo.service;

import cn.comradexy.demo.mapper.UserMapper;
import cn.comradexy.demo.model.domain.UserDO;
import cn.comradexy.demo.model.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserMapper userMapper;

    public UserDTO getUserByUserName(String userName) {
        UserDO user = userMapper.queryByUsername(userName);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public List<UserDTO> getUserByName(String name) {
        List<UserDO> users = userMapper.queryByName(name);
        List<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTOS.add(userDTO);
        });
        return userDTOS;
    }

    public List<UserDTO> listUser() {
        List<UserDO> users = userMapper.queryAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTOS.add(userDTO);
        });
        return userDTOS;
    }

}
