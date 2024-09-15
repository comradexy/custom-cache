package cn.comradexy.demo.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务访问记录
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: 服务访问记录
 */
@Data
@AllArgsConstructor
@Builder
public class ServeAccess {
    private Long id;
    private Long accessCount;
    private LocalDateTime lastAccessTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
