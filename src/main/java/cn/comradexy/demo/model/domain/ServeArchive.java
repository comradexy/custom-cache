package cn.comradexy.demo.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务归档记录
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: 服务归档记录
 */
@Data
@AllArgsConstructor
@Builder
public class ServeArchive {
    private Long id;
    private String storageType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
