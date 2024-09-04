package cn.comradexy.demo.mapper;

import cn.comradexy.demo.model.domain.User;
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
    void insert(User user);

    @Update("update users set username=#{username}, name=#{name}, password=#{password}, email=#{email}, " +
            "update_time=#{updateTime} " +
            "where id=#{id}")
    void update(User user);

    @Delete("delete from users where id=#{id}")
    void delete(Long id);

    @Select("select * from users where id=#{id}")
    User queryById(Long id);

    @Select("select * from users where username=#{username}")
    User queryByUsername(String username);

    @Select("select * from users where name=#{name}")
    List<User> queryByName(String name);

    @Select("select * from users")
    List<User> queryAll();

    @Select("select count(*) from users")
    int count();
}
