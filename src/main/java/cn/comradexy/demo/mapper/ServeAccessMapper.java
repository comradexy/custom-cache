package cn.comradexy.demo.mapper;

import cn.comradexy.demo.model.domain.ServeAccess;
import org.apache.ibatis.annotations.*;

/**
 * mapper demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: mapper demo
 */
@Mapper
public interface ServeAccessMapper {
    @Select("select * from serve_access where id=#{id}")
    ServeAccess selectById(Long id);

    @Delete("delete from serve_access where id=#{id}")
    void deleteById(Long id);

    @Insert("insert into serve_access (id, access_count, last_access_time) " +
            "values (#{id}, #{accessCount}, #{lastAccessTime})")
    void insert(ServeAccess serveAccess);

    @Update("update serve_access set access_count=#{accessCount}, last_access_time=#{lastAccessTime} where id=#{id}")
    void update(ServeAccess serveAccess);
}
