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

    @Select("select * from serve where id=#{id}")
    Serve selectById(Long id);

    @Insert("insert into serve(id, serve_item_id, region_id, city_code, sale_status, price, is_hot, hot_time_stamp, " +
            "create_time, update_time, create_by, update_by) values(#{id}, #{serveItemId}, #{regionId}, #{cityCode}, " +
            "#{saleStatus}, #{price}, #{isHot}, #{hotTimeStamp}, #{createTime}, #{updateTime}, #{createBy}, " +
            "#{updateBy})")
    void insert(Serve serve);

    @Select("select count(*) from serve")
    int count();
}
