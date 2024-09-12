package cn.comradexy.demo.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务归档数据
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: 服务归档数据
 */
@Data
@AllArgsConstructor
@Builder
public class ServeArchive {
    /*
     *`id`               BIGINT NOT NULL COMMENT '服务id',
     *`access_count`     INT   DEFAULT 0 COMMENT '访问次数',
     *`last_access_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后访问时间',
     *`storage_type`     VARCHAR(10) COMMENT '存储类型（HOT / COLD）',
     */

    private Long id;
    private Long accessCount;
    private LocalDateTime lastAccessTime;
    private String storageType;
}
