DROP TABLE IF EXISTS `serve`;
CREATE TABLE `serve`
(
    `id`             BIGINT         NOT NULL COMMENT '服务id',
    `serve_item_id`  BIGINT         NOT NULL COMMENT '服务项id',
    `region_id`      BIGINT         NOT NULL COMMENT '区域id',
    `city_code`      VARCHAR(255)   NOT NULL COMMENT '城市编码',
    `sale_status`    INT            NOT NULL DEFAULT '0' COMMENT '售卖状态，0：草稿，1下架，2上架',
    `price`          DECIMAL(10, 2) NOT NULL COMMENT '价格',
    `is_hot`         INT            NOT NULL DEFAULT '0' COMMENT '是否为热门，0非热门，1热门',
    `hot_time_stamp` BIGINT                  DEFAULT NULL COMMENT '更新为热门的时间戳',
    `create_time`    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`      BIGINT                  DEFAULT NULL COMMENT '创建者',
    `update_by`      BIGINT                  DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `区域id` (`region_id`) USING BTREE,
    KEY `服务id` (`serve_item_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='服务表';


DROP TABLE IF EXISTS `serve_archive`;
CREATE TABLE `serve_archive`
(
    `id`               INT NOT NULL COMMENT '服务id',
    `access_count`     BIGINT   DEFAULT 0 COMMENT '访问次数',
    `last_access_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后访问时间',
    `storage_type`     VARCHAR(10) COMMENT '存储类型（HOT / COLD）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='服务归档表';