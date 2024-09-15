package cn.comradexy.demo.mapper;

import cn.comradexy.demo.model.domain.ServeAccess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * mapper demo
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: mapper demo
 */
@Mapper
public interface ServeArchiveMapper {
    @Select("select * from serve_archive where id=#{id}")
    ServeAccess selectById(Long id);

    @Update("update serve_archive set access_count=#{accessCount}, last_access_time=#{lastAccessTime}, " +
            "storage_type=#{storageType} where id=#{id}")
    void update(ServeAccess serveAccess);

    @Select("select count(*) from serve_archive")
    long count();
}
