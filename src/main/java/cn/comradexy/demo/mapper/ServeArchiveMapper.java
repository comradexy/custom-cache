package cn.comradexy.demo.mapper;

import cn.comradexy.demo.model.domain.ServeArchive;
import org.apache.ibatis.annotations.Delete;
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
    ServeArchive selectById(Long id);

    @Update("update serve_archive set storage_type=#{storageType} where id=#{id}")
    void updateStorageType(ServeArchive serveArchive);

    @Delete("delete from serve_archive where id=#{id}")
    void deleteById(Long id);

    @Select("select count(*) from serve_archive where storage_type='COLD'")
    int countCold();
}
