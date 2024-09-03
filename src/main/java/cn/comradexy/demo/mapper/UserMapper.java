package cn.comradexy.demo.mapper;

import cn.comradexy.demo.model.domain.UserDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * mapper demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: mapper demo
 */
@Mapper
public interface UserMapper {
    @Insert("insert into users(username, password, email, create_time, update_time) " +
            "values(#{username}, #{name}, #{password}, #{email}, #{createTime}, #{updateTime})")
    void insert(UserDO user);

    @Update("update users set username=#{username}, name=#{name}, password=#{password}, email=#{email}, " +
            "update_time=#{updateTime} " +
            "where id=#{id}")
    void update(UserDO user);

    @Delete("delete from users where id=#{id}")
    void delete(Long id);

    @Select("select * from users where id=#{id}")
    UserDO queryById(Long id);

    @Select("select * from users where username=#{username}")
    UserDO queryByUsername(String username);

    @Select("select * from users where name=#{name}")
    List<UserDO> queryByName(String name);

    @Select("select * from users")
    List<UserDO> queryAll();

    @Select("select count(*) from users")
    int count();
}
