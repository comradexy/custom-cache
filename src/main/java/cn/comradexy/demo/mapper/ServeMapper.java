package cn.comradexy.demo.mapper;

import cn.comradexy.demo.model.domain.Serve;
import org.apache.ibatis.annotations.*;

/**
 * mapper demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: mapper demo
 */
@Mapper
public interface ServeMapper {
    @Select("select * from serve where id=#{id}")
    Serve selectById(Long id);

    @Select("select count(*) from serve")
    int count();
}
