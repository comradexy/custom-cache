package cn.comradexy.demo.mapper;

import cn.comradexy.demo.model.domain.Serve;
import cn.comradexy.demo.separation.OperateType;
import cn.comradexy.demo.separation.Separated;
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
public interface ServeMapper {
    /*
     *`id`             BIGINT         NOT NULL COMMENT '服务id',
     *`serve_item_id`  BIGINT         NOT NULL COMMENT '服务项id',
     *`region_id`      BIGINT         NOT NULL COMMENT '区域id',
     *`city_code`      VARCHAR(255)   NOT NULL COMMENT '城市编码',
     *`sale_status`    INT            NOT NULL DEFAULT '0' COMMENT '售卖状态，0：草稿，1下架，2上架',
     *`price`          DECIMAL(10, 2) NOT NULL COMMENT '价格',
     *`is_hot`         INT            NOT NULL DEFAULT '0' COMMENT '是否为热门，0非热门，1热门',
     *`hot_time_stamp` BIGINT                  DEFAULT NULL COMMENT '更新为热门的时间戳',
     *`create_time`    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     *`update_time`    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     *`create_by`      BIGINT                  DEFAULT NULL COMMENT '创建者',
     *`update_by`      BIGINT                  DEFAULT NULL COMMENT '更新者',
     */

    @Separated(operateType = OperateType.SELECT_ONE)
    @Select("select * from serve where id=#{id}")
    Serve selectById(Long id);

    @Separated(operateType = OperateType.SELECT_BATCH)
    @Select("select * from serve where serve_item_id=#{serveItemId} and region_id=#{regionId}")
    List<Serve> selectByItemAndRegion(@Param("serveItemId") Long serveItemId, @Param("regionId") Long regionId);

    @Separated(operateType = OperateType.SELECT_ONE_FOR_UPDATE)
    @Select("select * from serve where id=#{id} for update")
    Serve selectByIdForUpdate(Long id);

    @Separated(operateType = OperateType.SELECT_BATCH_FOR_UPDATE)
    @Select("select * from serve where serve_item_id=#{serveItemId} and region_id=#{regionId} for update")
    List<Serve> selectByItemAndRegionForUpdate(@Param("serveItemId") Long serveItemId, @Param("regionId") Long regionId);

    @Separated(operateType = OperateType.INSERT)
    @Insert("insert into serve(id, serve_item_id, region_id, city_code, sale_status, price, is_hot, hot_time_stamp, " +
            "create_time, update_time, create_by, update_by) values(#{id}, #{serveItemId}, #{regionId}, #{cityCode}, " +
            "#{saleStatus}, #{price}, #{isHot}, #{hotTimeStamp}, #{createTime}, #{updateTime}, #{createBy}, " +
            "#{updateBy})")
    void insert(Serve serve);

    @Separated(operateType = OperateType.UPDATE)
    @Update("update serve set serve_item_id=#{serveItemId}, region_id=#{regionId}, city_code=#{cityCode}, " +
            "sale_status=#{saleStatus}, price=#{price}, is_hot=#{isHot}, hot_time_stamp=#{hotTimeStamp}, " +
            "update_time=#{updateTime}, update_by=#{updateBy} where id=#{id}")
    void update(Serve serve);

    @Separated(operateType = OperateType.DELETE)
    @Delete("delete from serve where id=#{id}")
    void deleteById(Long id);

    @Separated(operateType = OperateType.COUNT)
    @Select("select count(*) from serve")
    int count();
}
