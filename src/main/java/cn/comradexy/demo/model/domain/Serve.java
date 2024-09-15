package cn.comradexy.demo.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 服务实体类
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: 服务实体类
 */
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Serve {
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

    private Long id;
    private Long serveItemId;
    private Long regionId;
    private String cityCode;
    private Integer saleStatus;
    private Double price;
    private Integer isHot;
    private Long hotTimeStamp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createBy;
    private Long updateBy;
}
